package com.kelviniyalo.youverifystorekelviniyalo.data.adapter

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper.loadImage
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper.startBounceAnimation
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.databinding.ItemLayoutProductBinding
import com.kelviniyalo.youverifystorekelviniyalo.databinding.LayoutProductHeaderItemsBinding

class ProductsAdapter(
    private val onListItems: (items: Product) -> Unit ={},
    private val onItemClicked: (itemAtPosition: Product) -> Unit
) :
    ListAdapter<Product, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun submitList(list: List<Product>?) {
        val adapterList = list?.groupBy { it.category }
            ?.flatMap { (category, products) ->
                mutableListOf(Product(isHeader = true, cat = category)).apply { addAll(products) }
            } ?: mutableListOf()
        super.submitList(adapterList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null

        when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding =
                    LayoutProductHeaderItemsBinding.inflate(LayoutInflater.from(parent.context))
                viewHolder = HeaderVH(binding)
            }

            VIEW_TYPE_PRODUCT_ITEM -> {
                val binding = ItemLayoutProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                viewHolder = ProductsVH(binding)
            }
        }
        return viewHolder!!
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].isHeader) VIEW_TYPE_HEADER else VIEW_TYPE_PRODUCT_ITEM
    }

    override fun getItemCount(): Int = currentList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemAtPosition = currentList[position]
        if (itemAtPosition.isHeader) {
            (holder as HeaderVH).bind(itemAtPosition.cat.toString())
        } else {
            (holder as ProductsVH).bind(itemAtPosition)
        }
    }


    inner class ProductsVH(
        private val binding: ItemLayoutProductBinding,
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(product: Product) {
            with(binding) {
                var imageBitmap: Bitmap? = null
                var imageColor: Int? = 0
                productImg.loadImage(product.thumbnail.toString(), root.context) {
                    imageBitmap = it
                    Palette.from(it).generate { palette ->
                        palette?.getMutedColor(root.context.getColor(R.color.white))
                            ?.let { vibrantColor ->
                                imageColor = vibrantColor
                                val colorStateList = ColorStateList.valueOf(vibrantColor)
                                rootView.backgroundTintList = colorStateList
                            }
                    }
                }
                val item = product.copy(
                    productColor = imageColor!!,
                    imageByteArray = imageBitmap?.let { bitMap -> Helper.bitmapToByteArray(bitMap) })

                onListItems.invoke(item)
                productTittle.text = product.title
                productPrice.text = root.context.getString(
                    R.string.naira,
                    Helper.formatAmount(product.price.toString())
                )
                discount.text = buildString {
                    append("-")
                    append(product.discountPercentage.toString())
                }

                onListItems.invoke(item)
                root.setOnClickListener {
                    onItemClicked.invoke(
                        product.copy(
                            productColor = imageColor!!,
                            imageByteArray = imageBitmap?.let { bitMap ->
                                Helper.bitmapToByteArray(bitMap)
                            })
                    )
                    rootView.startBounceAnimation(it.context)
                }
            }
        }
    }

    inner class HeaderVH(private val binding: LayoutProductHeaderItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            binding.headerDate.text = category.split(" ").joinToString(" ") {
                it.replaceFirstChar(Char::uppercase)
            }
        }
    }


    companion object {
        const val VIEW_TYPE_PRODUCT_ITEM = 0
        const val VIEW_TYPE_HEADER = 1

    }

}