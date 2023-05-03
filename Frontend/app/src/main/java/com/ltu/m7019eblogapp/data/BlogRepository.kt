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

class NetworkBlogRepository(private val api: BlogApiService) : BlogRepository {

    //-----------------------------Posts-------------------------

    override suspend fun getPosts(set: Int): List<Post> {
        return api.getPosts(set)
    }

    override suspend fun getPost(post_id: String): Post {
        return api.getPost(post_id)
    }

    //----------------------Users-------------------------------

    override suspend fun getUsers(set: Int): List<User> {
        return api.getUsers(set)
    }

    override suspend fun getUser(user_id: String): User {
        return api.getUser(user_id)
    }

    override suspend fun getUserPosts(user_id: String, set: Int): List<Post> {
        return api.getUserPosts(user_id, set)
    }

    //--------------------Categories & Tags------------------------------

    override suspend fun getCategories(set: Int): List<Category> {
        return api.getCategories(set)
    }

    override suspend fun getCategory(category_id: String): Category {
        return api.getCategory(category_id)
    }

    override suspend fun getTags(set: Int): List<Tag> {
        return api.getTags(set)
    }

    override suspend fun getTag(tag_id: String): Tag {
        return api.getTag(tag_id)
    }

    //------------------------Search--------------------------------------

    override suspend fun searchPost(params: PostSearchBody, set: Int): List<Post> {
        return api.searchPost(params, set)
    }

    override suspend fun searchCategory(params: CategorySearchBody, set: Int): List<Category> {
        return api.searchCategory(params, set)
    }

    override suspend fun searchTag(params: TagSearchBody, set: Int): List<Tag> {
        return api.searchTag(params, set)
    }

}