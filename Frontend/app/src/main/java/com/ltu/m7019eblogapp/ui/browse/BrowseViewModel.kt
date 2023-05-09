package com.ltu.m7019eblogapp.ui.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ltu.m7019eblogapp.data.DefaultAppContainer
import com.ltu.m7019eblogapp.model.Post
import kotlinx.coroutines.launch

class BrowseViewModel : ViewModel() {

    private val _container : DefaultAppContainer = DefaultAppContainer()

    private val _postList = MutableLiveData<List<Post>>()
    val postList : LiveData<List<Post>>
        get() {
            return _postList
        }

    private var currentSet: Int = 1

    init {
        getPosts()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is the browse Fragment"
    }
    val text: LiveData<String> = _text

    fun getPosts() {
        viewModelScope.launch {
            try {
                _postList.value = _container.blogRepository.getPosts(currentSet)
                _text.value = "Success! Loaded ${postList.value?.size} results"
            } catch (e: Exception) {
                _postList.value = listOf()
                _text.value = "Error: ${e.message}"
            }
        }

    }
}