package jp.numero.dagashiapp.data

import java.time.Instant

data class MilestoneList(
    val value: List<Milestone>,
    val hasMore: Boolean = false,
    val nextCursor: String? = null
)

data class Milestone(
    val id: String,
    val number: Int,
    val description: String?,
    val path: String,
    val closedAd: Instant,
    val issues: List<Issue>
) {
    data class Issue(
        val title: String
    )
}

data class MilestoneDetail(
    val id: String,
    val number: Int,
    val url: String,
    val description: String?,
    val issues: List<Issue>
)