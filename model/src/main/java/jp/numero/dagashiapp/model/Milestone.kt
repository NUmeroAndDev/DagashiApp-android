package jp.numero.dagashiapp.model

import java.time.Instant

data class MilestoneList(
    val value: List<Milestone>,
    val hasMore: Boolean,
    val nextCursor: String? = null
)

data class Milestone(
    val id: String,
    val number: Int,
    val description: String,
    val path: String,
    val closedAd: Instant
)

data class MilestoneDetail(
    val id: String,
    val number: Int,
    val url: String,
    val description: String,
    val issues: List<Issue>
)