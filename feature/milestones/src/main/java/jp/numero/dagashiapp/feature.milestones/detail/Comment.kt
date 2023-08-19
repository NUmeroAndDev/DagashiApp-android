package jp.numero.dagashiapp.feature.milestones.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import jp.numero.dagashiapp.model.Comment
import jp.numero.dagashiapp.ui.component.IssueDescriptionText

@Composable
fun Comments(
    commentList: List<Comment>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
    ) {
        commentList.forEachIndexed { index, comment ->
            CommentItem(comment = comment)
            if (index != commentList.lastIndex) {
                Divider(modifier = Modifier.padding(start = 32.dp + 24.dp))
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.semantics(mergeDescendants = true) {
                contentDescription = comment.author.id
            }
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = comment.author.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
            Text(
                text = comment.author.id,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        IssueDescriptionText(
            text = comment.body,
            modifier = Modifier.padding(start = 32.dp + 12.dp)
        )
    }
}