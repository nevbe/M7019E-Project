package com.ltu.m7019eblogapp.ui.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ltu.m7019eblogapp.model.User

class ProfileViewModel(application: Application, user: User) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the profile fragment"
    }
    val text: LiveData<String> = _text

    init {
        _text.value = "Hello ${user.username}!"
    }
}