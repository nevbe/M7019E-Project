package com.ltu.m7019eblogapp.data.service

import com.ltu.m7019eblogapp.model.Category
import com.ltu.m7019eblogapp.model.Post
import com.ltu.m7019eblogapp.model.User
import com.ltu.m7019eblogapp.model.Tag
import retrofit2.http.*

//TODO: Add @Headers({"Authorization", "Bearer "+ token}) for auth

interface BlogApiService {
    //-----------------------------Posts-------------------------
    @GET("posts")
    suspend fun getPosts(
        @Query("set")
        set: Int = 1
    ) : List<Post>
    @GET("posts/{post_id}")
    suspend fun getPost(
        @Path("post_id")
        post_id: String
    ) : Post
    /*
    TODO: Implement when login is done
    @PATCH("posts/{post_id}")
    suspend fun updatePost(
        @Path("post_id")
        post_id: String
    )
    @DELETE("posts/{post_id}")
    suspend fun deletePost(
        @Path("post_id")
        post_id: String
    )
     */

    //----------------------Users-------------------------------
    @GET("user")
    suspend fun getUsers(
        @Query("set")
        set: Int = 1
    ) : List<User>
    @GET("user/{user_id}")
    suspend fun getUser(
        @Path("user_id")
        user_id: String
    )
    @GET("user/{user_id}/posts")
    suspend fun getUserPosts(
        @Path("user_id")
        user_id: String,
        @Query("set")
        set: Int = 1
    )

    /*
    TODO: Auth
    @GET("user/me")
    suspend fun getCurrentUser()
    @PATCH("user/{user_id}")
    suspend fun patchUser()
    @DELETE("user/{user_id}")
    suspend fun deleteUser()
     */

    //--------------------Categories & Tags------------------------------
    @GET("categories")
    suspend fun getCategories(
        @Query("set")
        set: Int = 1
    ) : List<Category>
    @GET("categories/{category_id}")
    suspend fun getCategory(
        @Path("category_id")
        category_id: String
    ) : Category
    @GET("tags")
    suspend fun getTags(
        @Query("set")
        set: Int = 1
    ) : List<Tag>
    @GET("tags/{tag_id}")
    suspend fun getTag(
        @Path("tag_id")
        tag_id: String
    ) : Tag

    //------------------------Search--------------------------------------
    @POST("search/posts")
    suspend fun searchPost(
        @Body params: PostSearchBody,
        @Query("set")
        set: Int = 1
    )
    @POST("search/categories")
    suspend fun searchCategory(
        @Body params: CategorySearchBody,
        @Query("set")
        set: Int = 1
    )
    @POST("search/tags")
    suspend fun searchTag(
        @Body params: TagSearchBody,
        @Query("set")
        set: Int = 1
    )

}