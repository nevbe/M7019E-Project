package com.ltu.m7019eblogapp.data

import com.ltu.m7019eblogapp.data.service.BlogApiService

interface BlogRepository {
}

class NetworkBlogRepository(private val blogApiService: BlogApiService) : BlogRepository {

}