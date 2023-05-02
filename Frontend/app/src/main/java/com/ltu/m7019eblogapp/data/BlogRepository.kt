package com.ltu.m7019eblogapp.data

import com.ltu.m7019eblogapp.data.service.BlogApiService
import com.ltu.m7019eblogapp.data.service.CategorySearchBody
import com.ltu.m7019eblogapp.data.service.PostSearchBody
import com.ltu.m7019eblogapp.data.service.TagSearchBody
import com.ltu.m7019eblogapp.model.Category
import com.ltu.m7019eblogapp.model.Post
import com.ltu.m7019eblogapp.model.Tag
import com.ltu.m7019eblogapp.model.User

interface BlogRepository {
    //-----------------------------Posts-------------------------

    suspend fun getPosts(set: Int = 1) : List<Post>
    suspend fun getPost(post_id: String) : Post

    //----------------------Users-------------------------------

    suspend fun getUsers(set: Int = 1) : List<User>
    suspend fun getUser(user_id: String) : User
    suspend fun getUserPosts(user_id: String, set: Int = 1) : List<Post>

    //--------------------Categories & Tags------------------------------

    suspend fun getCategories(set: Int = 1) : List<Category>
    suspend fun getCategory(category_id: String) : Category
    suspend fun getTags(set: Int = 1) : List<Tag>
    suspend fun getTag(tag_id: String) : Tag

    //------------------------Search--------------------------------------

    //TODO: Swap body objects for proper params?
    suspend fun searchPost(params: PostSearchBody, set: Int = 1) : List<Post>
    suspend fun searchCategory(params: CategorySearchBody, set: Int = 1) : List<Category>
    suspend fun searchTag(params: TagSearchBody, set: Int = 1) : List<Tag>

}

class NetworkBlogRepository(private val blogApiService: BlogApiService) : BlogRepository {

    //-----------------------------Posts-------------------------

    override suspend fun getPosts(set: Int): List<Post> {
        TODO("Not yet implemented")
    }

    override suspend fun getPost(post_id: String): Post {
        TODO("Not yet implemented")
    }

    //----------------------Users-------------------------------

    override suspend fun getUsers(set: Int): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(user_id: String): User {
        TODO("Not yet implemented")
    }

    override suspend fun getUserPosts(user_id: String, set: Int): List<Post> {
        TODO("Not yet implemented")
    }

    //--------------------Categories & Tags------------------------------

    override suspend fun getCategories(set: Int): List<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategory(category_id: String): Category {
        TODO("Not yet implemented")
    }

    override suspend fun getTags(set: Int): List<Tag> {
        TODO("Not yet implemented")
    }

    override suspend fun getTag(tag_id: String): Tag {
        TODO("Not yet implemented")
    }

    //------------------------Search--------------------------------------

    override suspend fun searchPost(params: PostSearchBody, set: Int): List<Post> {
        TODO("Not yet implemented")
    }

    override suspend fun searchCategory(params: CategorySearchBody, set: Int): List<Category> {
        TODO("Not yet implemented")
    }

    override suspend fun searchTag(params: TagSearchBody, set: Int): List<Tag> {
        TODO("Not yet implemented")
    }

}