package com.example.sampletask2.data.remote

import com.example.sampletask2.data.remote.response.HomePostResponse
import com.example.sampletask2.data.remote.response.StoryResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET(Endpoints.HOME_POSTS_LIST)
    fun doHomePostListApiCall(): Single<HomePostResponse>

    @GET
    fun doStoriesApiCall(@Url url: String): Single<StoryResponse>
}