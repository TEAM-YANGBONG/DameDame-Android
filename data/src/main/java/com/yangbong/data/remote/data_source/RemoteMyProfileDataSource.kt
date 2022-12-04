package com.yangbong.data.remote.data_source

import com.yangbong.data.remote.call_adapter.NetworkState
import com.yangbong.data.remote.model.response.BaseResponse
import com.yangbong.data.remote.model.response.MyProfileResponse

interface RemoteMyProfileDataSource {
    suspend fun getMyProfile(userId:Int): NetworkState<BaseResponse<MyProfileResponse>>
}