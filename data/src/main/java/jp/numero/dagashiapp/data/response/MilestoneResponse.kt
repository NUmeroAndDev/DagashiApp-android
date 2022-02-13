package jp.numero.dagashiapp.data.response

import jp.numero.dagashiapp.data.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

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
    @Serializable(with = InstantSerializer::class)
    val closedAt: Instant
)

@Serializable
data class PageInfoResponse(
    val endCursor: String,
    val hasNextPage: Boolean
)