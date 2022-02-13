package jp.numero.dagashiapp.data.response

import kotlinx.serialization.Serializable

@Serializable
data class MilestoneDetailResponse(
    val id: String,
    val number: Int,
    val description: String,
    val issues: IssuesResponse
)

@Serializable
data class IssuesResponse(
    val nodes: List<IssueResponse>,
)

@Serializable
data class IssueResponse(
    val url: String,
    val title: String,
    val body: String,
    val labels: LabelsResponse,
    val comments: CommentsResponse
)

@Serializable
data class LabelsResponse(
    val nodes: List<LabelResponse>
)

@Serializable
data class LabelResponse(
    val name: String,
    val description: String,
    val color: String
)

@Serializable
data class CommentsResponse(
    val nodes: List<CommentResponse>
)

@Serializable
data class CommentResponse(
    val body: String,
    val publishedAt: String,
    val author: AuthorResponse
)

@Serializable
data class AuthorResponse(
    val login: String,
    val url: String,
    val avatarUrl: String
)