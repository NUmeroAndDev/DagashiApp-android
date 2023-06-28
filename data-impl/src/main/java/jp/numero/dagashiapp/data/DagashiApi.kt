package jp.numero.dagashiapp.data

import jp.numero.dagashiapp.data.response.MilestoneDetailResponse
import jp.numero.dagashiapp.data.response.MilestoneListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DagashiApi {

    @GET("api/index.json")
    suspend fun getMilestoneList(): MilestoneListResponse

    @GET("api/{cursor}.json")
    suspend fun getMilestoneList(@Path("cursor") cursor: String): MilestoneListResponse

    @GET("api/issue/{path}.json")
    suspend fun getMilestoneDetail(@Path("path") path: String): MilestoneDetailResponse
}