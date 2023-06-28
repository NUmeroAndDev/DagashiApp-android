package jp.numero.dagashiapp.data

import jp.numero.dagashiapp.model.MilestoneList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DagashiLocalDataSource @Inject constructor() {

    var localMilestoneListCache: MilestoneList? = null
        private set

    fun updateLocalMilestoneListCache(milestoneList: MilestoneList) {
        localMilestoneListCache = milestoneList
    }
}