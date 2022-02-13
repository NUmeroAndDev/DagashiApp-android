package jp.numero.dagashiapp.data

import jp.numero.dagashiapp.data.response.MilestoneDetailResponse
import jp.numero.dagashiapp.data.response.MilestoneListResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DagashiRemoteDataSource @Inject constructor(
    private val dagashiApi: DagashiApi,
) {
    suspend fun fetchMilestoneList(
        nextCursor: String? = null
    ): MilestoneListResponse {
        return if (nextCursor != null) {
            dagashiApi.getMilestoneList(nextCursor)
        } else {
            dagashiApi.getMilestoneList()
        }
    }

    suspend fun fetchMilestoneDetail(
        path: String
    ): MilestoneDetailResponse {
        return dagashiApi.getMilestoneDetail(path)
    }
}