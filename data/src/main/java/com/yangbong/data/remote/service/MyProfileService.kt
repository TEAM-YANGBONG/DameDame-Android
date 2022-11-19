package com.yangbong.data.remote.service

import com.yangbong.data.remote.call_adapter.NetworkState
import com.yangbong.data.remote.model.response.BaseResponse
import com.yangbong.data.remote.model.response.MyProfileResponse
import retrofit2.http.GET

interface MyProfileService {

    @GET("user/myProfile")
    suspend fun getMyProfile(): NetworkState<BaseResponse<MyProfileResponse>>
}