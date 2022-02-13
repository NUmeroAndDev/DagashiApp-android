package jp.numero.dagashiapp.repository

import jp.numero.dagashiapp.model.MilestoneDetail
import jp.numero.dagashiapp.model.MilestoneList

interface DagashiRepository {
    suspend fun fetchMilestoneList(nextCursor: String? = null): MilestoneList

    suspend fun fetchMilestoneDetail(path: String): MilestoneDetail
}