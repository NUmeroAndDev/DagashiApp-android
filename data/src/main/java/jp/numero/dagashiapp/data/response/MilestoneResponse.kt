package jp.numero.dagashiapp.data.response

import kotlinx.serialization.Serializable

@Serializable
data class MilestoneListResponse(
    val milestones: MilestonesResponse,
)

@Serializable
data class MilestonesResponse(
    val nodes: List<MilestoneResponse>,
    val pageInfo: PageInfoResponse
)

@Serializable
data class MilestoneResponse(
    val id: String,
    val number: Int,
    val description: String,
    val path: String,
    val closedAt: String
)

@Serializable
data class PageInfoResponse(
    val endCursor: String,
    val hasNextPage: Boolean
)