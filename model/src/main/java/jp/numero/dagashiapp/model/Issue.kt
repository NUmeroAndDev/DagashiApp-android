package jp.numero.dagashiapp.model

import java.time.Instant

data class Issue(
    val url: String,
    val title: String,
    val body: String,
    val labels: List<Label>,
    val comments: List<Comment>
)

data class Label(
    val name: String,
    val description: String,
    val color: String
)

data class Comment(
    val body: String,
    val publishedAt: Instant,
    val author: Author
)

data class Author(
    val id: String,
    val url: String,
    val icon: String
)