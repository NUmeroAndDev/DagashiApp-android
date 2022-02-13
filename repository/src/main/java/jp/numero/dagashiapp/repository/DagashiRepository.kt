package jp.numero.dagashiapp.repository

import jp.numero.dagashiapp.model.MilestoneDetail
import jp.numero.dagashiapp.model.MilestoneList

interface DagashiRepository {
    suspend fun fetchMilestoneList(): MilestoneList

    suspend fun fetchMoreMilestoneList(nextCursor: String): MilestoneList

    suspend fun fetchMilestoneDetail(path: String): MilestoneDetail
}