package com.ltu.m7019eblogapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ltu.m7019eblogapp.data.DefaultAppContainer
import com.ltu.m7019eblogapp.data.util.DataFetchStatus
import com.ltu.m7019eblogapp.model.Post
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _container : DefaultAppContainer = DefaultAppContainer()

    private val _postList = MutableLiveData<List<Post>>()
    val postList : LiveData<List<Post>>
        get() {
            return _postList
        }

    private val _dataFetchStatus = MutableLiveData<DataFetchStatus>()
    val dataFetchStatus : LiveData<DataFetchStatus>
        get(){
            return _dataFetchStatus
        }

    private var currentSet: Int = 1

    init {
        getPosts()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is the browse Fragment"
    }
    val text: LiveData<String> = _text

    private val _navigateToPost = MutableLiveData<Post?>()
    val navigateToPost: MutableLiveData<Post?>
        get() {
            return _navigateToPost
        }

    fun getPosts() {
        _dataFetchStatus.value = DataFetchStatus.LOADING
        viewModelScope.launch {
            try {
                _postList.value = _container.blogRepository.getPosts(currentSet)
                _text.value = "Success! Loaded ${postList.value?.size} results"
                _dataFetchStatus.value = DataFetchStatus.DONE
            } catch (e: Exception) {
                _postList.value = listOf()
                _text.value = "Error: ${e.message}"
                _dataFetchStatus.value = DataFetchStatus.ERROR
            }
        }

    }

    fun onPostListItemClicked(post: Post){
        println("navigating to ${post.title}")
        _navigateToPost.value = post
    }

    fun onPostNavigationComplete(){
        println("nav complete!")
        _navigateToPost.value = null
    }
}