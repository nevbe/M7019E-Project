package com.ltu.m7019eblogapp.ui.login

import androidx.lifecycle.ViewModel
import com.ltu.m7019eblogapp.model.User

class UserSessionViewModel : ViewModel() {
    var user : User? = null
    var accessToken : String? = null
}