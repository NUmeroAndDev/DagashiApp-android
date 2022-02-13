package jp.numero.dagashiapp.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.numero.dagashiapp.data.DagashiRemoteDataSource
import jp.numero.dagashiapp.data.response.MilestoneDetailResponse
import jp.numero.dagashiapp.data.response.MilestoneListResponse
import jp.numero.dagashiapp.model.*
import javax.inject.Inject
import javax.inject.Singleton

internal class DagashiRepositoryImpl @Inject constructor(
    private val dagashiRemoteDataSource: DagashiRemoteDataSource
) : DagashiRepository {
    override suspend fun fetchMilestoneList(nextCursor: String?): MilestoneList {
        return dagashiRemoteDataSource.fetchMilestoneList(nextCursor).toModel()
    }

    override suspend fun fetchMilestoneDetail(path: String): MilestoneDetail {
        return dagashiRemoteDataSource.fetchMilestoneDetail(path).toModel()
    }

    private fun MilestoneListResponse.toModel(): MilestoneList {
        return MilestoneList(
            value = milestones.nodes.map { milestoneResponse ->
                Milestone(
                    id = milestoneResponse.id,
                    number = milestoneResponse.number,
                    description = milestoneResponse.description,
                    path = milestoneResponse.path,
                    closedAd = milestoneResponse.closedAt
                )
            },
            // TODO: pass page info
        )
    }

    private fun MilestoneDetailResponse.toModel(): MilestoneDetail {
        return MilestoneDetail(
            id = id,
            number = number,
            description = description,
            issues = issues.nodes.map { issueResponse ->
                Issue(
                    url = issueResponse.url,
                    title = issueResponse.title,
                    body = issueResponse.body,
                    labels = issueResponse.labels.nodes.map { labelResponse ->
                        Label(
                            name = labelResponse.name,
                            description = labelResponse.description,
                            color = labelResponse.color
                        )
                    },
                    comments = issueResponse.comments.nodes.map { commentResponse ->
                        val authorResponse = commentResponse.author
                        Comment(
                            body = commentResponse.body,
                            publishedAt = commentResponse.publishedAt,
                            author = Author(
                                id = authorResponse.login,
                                url = authorResponse.url,
                                icon = authorResponse.avatarUrl
                            )
                        )
                    }
                )
            }
        )
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class RepositoryModule {
        @Singleton
        @Binds
        abstract fun bindDagashiRepository(impl: DagashiRepositoryImpl): DagashiRepository
    }
}