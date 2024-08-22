package com.kelviniyalo.youverifystorekelviniyalo.data.adapter

import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.common.Helper
import com.kelviniyalo.youverifystorekelviniyalo.data.model.product.Product
import com.kelviniyalo.youverifystorekelviniyalo.databinding.ItemLayoutCartBinding


class CartItemsAdapter(
    private val onItemClicked: (itemAtPosition: Product) -> Unit
) : ListAdapter<Product, CartItemsAdapter.CartItemVH>(object :
    DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemVH {
        //inflate the view to be used by the payment option view holder
        val binding =
            ItemLayoutCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemVH(binding)

    }

    override fun getItemCount(): Int = currentList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CartItemVH, position: Int) {
        val itemAtPosition = currentList[position]
        holder.bind(itemAtPosition)
    }


    inner class CartItemVH(
        private val binding: ItemLayoutCartBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(product: Product) {
            with(binding) {
                val imageResult = Helper.byteArrayToBitmap(product.imageByteArray!!)
                productImg.setImageBitmap(imageResult)
                rootView.backgroundTintList = ColorStateList.valueOf(product.productColor)
                productTittle.text = product.title
                productBrand.text = buildString {
                    append("Brand: ")
                    append(product.brand)
                }
                productAmount.text = root.context.getString(
                    R.string.naira,
                    Helper.formatAmount(product.price.toString())
                )
                productShipment.text = product.shippingInformation
                productQuantity.text = buildString {
                    append("Qty: ")
                    append(product.itemCount.toString())
                }

                deleteItem.setOnClickListener {
                    onItemClicked.invoke(product)
                }
            }
        }

    }
}