package com.example.sampletask2.utils.binding

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sampletask2.R


@SuppressLint("CheckResult")
@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, url: String?) {
    val requestOptions = RequestOptions()
    requestOptions.placeholder(R.drawable.placeholder)
    url?.let { Glide.with(view.context).load(it).apply(requestOptions).into(view) }
}

@BindingAdapter("app:formatTabName")
fun formatTabName(textView: TextView, tabName: String?) {
    tabName?.let {
        if (tabName == textView.context.getString(R.string.HOME)) textView.text = tabName
        else textView.text = textView.context.getString(
            R.string.HASHTAG,
            tabName.replace("\\s".toRegex(), "")
        )
    }
}