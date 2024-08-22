package com.kelviniyalo.youverifystorekelviniyalo.common

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kelviniyalo.youverifystorekelviniyalo.R
import com.kelviniyalo.youverifystorekelviniyalo.data.adapter.ProductsAdapter
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat

object Helper {

    fun View.showMessage(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }

    fun formatAmount(value: Any): String {
        val valueToBeFormatted: Number = if (value is String) {
            value.toDouble()
        } else {
            value as Number
        }
        val df = DecimalFormat("##,###,##0.00")
        return df.format(valueToBeFormatted)
    }

    fun View.startMoveUpAnimation(context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.move_up)
        startAnimation(animation)
    }

    fun View.startSlideInAnimation(context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left_right)
        startAnimation(animation)
    }

    fun View.startBounceAnimation(context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.btn_bounce)
        startAnimation(animation)
    }

    fun showSuccessAlertDialog(context: Activity, message: String? = null,onContinue:() ->Unit): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.success_alert, null)
        val dialog = MaterialAlertDialogBuilder(context)
            .setView(view)
            .setCancelable(false)
            .show()
        val text = view.findViewById<TextView>(R.id.message_text)
        val continueBtn = view.findViewById<TextView>(R.id.continue_button)
        if (message?.isNullOrEmpty() == false) {
            text.text = message
        }
        continueBtn.setOnClickListener {
            dialog.dismiss()
            onContinue.invoke()
        }

        return dialog
    }

    private fun getProgressDrawable(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 10f
            centerRadius = 50f
            start()
        }
    }
    fun ImageView.loadImage(uri: String, context: Context, getImageResult:(Bitmap) -> Unit = {}) {
        val option = RequestOptions()
            .placeholder(getProgressDrawable(context))
            .error(R.drawable.warning_24)
        Glide.with(context)
            .setDefaultRequestOptions(option)
            .asBitmap()
            .load(uri)
            .fitCenter()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                    this@loadImage.setImageBitmap(resource)
                    getImageResult.invoke(resource)
                }


                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle placeholder
                }
            })
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

     fun createGridLayoutManager(context: Context,productsAdapter:ProductsAdapter): GridLayoutManager {
        return GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (productsAdapter.getItemViewType(position)) {
                        ProductsAdapter.VIEW_TYPE_HEADER -> spanCount
                        ProductsAdapter.VIEW_TYPE_PRODUCT_ITEM -> 1
                        else -> 1
                    }
                }
            }
        }
    }

}