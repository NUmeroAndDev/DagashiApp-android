package jp.numero.dagashiapp.data

interface DagashiRepository {
    suspend fun fetchMilestoneList(): MilestoneList

    suspend fun fetchMoreMilestoneList(nextCursor: String): MilestoneList

    suspend fun fetchMilestoneDetail(path: String): MilestoneDetail
}