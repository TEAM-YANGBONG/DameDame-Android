package com.yangbong.data.remote.data_source

import com.yangbong.data.remote.call_adapter.NetworkState
import com.yangbong.data.remote.model.response.BaseResponse
import com.yangbong.data.remote.model.response.FriendListResponse

interface RemoteFriendsDataSource {

    suspend fun getFriendList(
        userId: Int,
        page: Int,
        size: Int
    ): NetworkState<BaseResponse<FriendListResponse>>
}