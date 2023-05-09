package com.ltu.m7019eblogapp.data

import com.auth0.android.result.Credentials
import com.ltu.m7019eblogapp.data.service.BlogApiService
import com.ltu.m7019eblogapp.data.service.CategorySearchBody
import com.ltu.m7019eblogapp.data.service.PostSearchBody
import com.ltu.m7019eblogapp.data.service.TagSearchBody
import com.ltu.m7019eblogapp.model.*
import okhttp3.internal.wait
import retrofit2.HttpException

interface BlogRepository {
    //-----------------------------Posts-------------------------

    suspend fun getPosts(set: Int = 1) : List<Post>
    suspend fun getPost(post_id: String) : Post

    //----------------------Users-------------------------------

    suspend fun getUsers(set: Int = 1) : List<User>
    suspend fun getUser(user_id: String) : User
    suspend fun getUserPosts(user_id: String, set: Int = 1) : List<Post>
    suspend fun getCurrentUser(accessToken: String) : User
    suspend fun createUser(accessToken: String, name: String) : User

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

    //--------------------------AUTH0--------------------------------------
    suspend fun login(accessToken: String, name: String)

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

    override suspend fun getCurrentUser(accessToken: String): User {
        return api.getCurrentUser("Bearer $accessToken")
    }

    override suspend fun createUser(accessToken: String, name: String): User {
        return api.createUser("Bearer $accessToken", MiniUser(name))
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

    //-----------------------AUTH0-----------------------------------------

    override suspend fun login(accessToken: String, name: String) {
        try{
            getCurrentUser(accessToken)
        } catch(e : HttpException ){
            when(e.code()){
                404 -> {
                    // Valid token but user not in DB
                    createUser(accessToken, name)
                }
                401 -> {
                    TODO("Invalid token")
                }
            }
        }

    }
}
