package com.ltu.m7019eblogapp.model
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag (
    @Json(name = "_id")
    var id: String,
    @Json(name = "name")
    var name: String
    ) : Parcelable