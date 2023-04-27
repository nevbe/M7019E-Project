package com.ltu.m7019eblogapp.model

//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize

//@Parcelize
data class Post(
    var id: String,
    var title: String,
    var created_at: String,
    var content: String,
    var user: User,
    var category: Category,
    var tags: List<Tag>,
    var media: String,
) //: Parcelable