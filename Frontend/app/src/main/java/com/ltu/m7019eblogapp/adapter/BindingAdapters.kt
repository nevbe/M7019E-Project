package com.ltu.m7019eblogapp.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImageFromUrl")
fun bindImage(imgView: ImageView, imgUrl: String){
    imgUrl.let {
        Glide.with(imgView).load(imgUrl).into(imgView)
    }
}