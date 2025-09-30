package com.fsck.k9.ui.messagelist.item

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.k9mail.core.ui.compose.designsystem.atom.CircularProgressIndicator
import coil3.ImageLoader
import com.fsck.k9.contacts.ContactImageModel
import com.fsck.k9.mail.Address
import com.fsck.k9.ui.messagelist.MessageListAppearance
import com.fsck.k9.ui.messagelist.MessageListItem
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.thunderbird.core.ui.compose.designsystem.organism.message.ActiveMessageItem
import net.thunderbird.core.ui.compose.designsystem.organism.message.ReadMessageItem
import net.thunderbird.core.ui.compose.designsystem.organism.message.UnreadMessageItem

@Suppress("LongParameterList", "LongMethod")
@OptIn(ExperimentalTime::class)
@Composable
internal fun MessageItemContent(
    item: MessageListItem,
    isActive: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onFavouriteClick: (Boolean) -> Unit,
    appearance: MessageListAppearance,
    imageLoader: ImageLoader,
) {
    val receivedAt = remember(item.messageDate) {
        Instant.fromEpochMilliseconds(item.messageDate)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    when {
        isActive -> ActiveMessageItem(
            sender = "${item.displayName}",
            subject = item.subject ?: "n/a",
            preview = item.previewText,
            receivedAt = receivedAt,
            avatar = {
                if (appearance.showContactPicture) {
                    ContactImageAvatar(
                        address = item.displayAddress ?: Address(""),
                        imageLoader = imageLoader,
                    )
                }
            },
            onClick = onClick,
            onLongClick = onLongClick,
            onLeadingClick = onAvatarClick,
            onFavouriteChange = onFavouriteClick,
            favourite = item.isStarred,
            selected = isSelected,
            maxPreviewLines = appearance.previewLines,
            threadCount = item.threadCount,
            hasAttachments = item.hasAttachments,
            swapSenderWithSubject = !appearance.senderAboveSubject,
        )

        item.isRead -> ReadMessageItem(
            sender = "${item.displayName}",
            subject = item.subject ?: "n/a",
            preview = item.previewText,
            receivedAt = receivedAt,
            avatar = {
                if (appearance.showContactPicture) {
                    ContactImageAvatar(
                        address = item.displayAddress ?: Address(""),
                        imageLoader = imageLoader,
                    )
                }
            },
            onClick = onClick,
            onLongClick = onLongClick,
            onLeadingClick = onAvatarClick,
            onFavouriteChange = onFavouriteClick,
            favourite = item.isStarred,
            selected = isSelected,
            maxPreviewLines = appearance.previewLines,
            threadCount = item.threadCount,
            hasAttachments = item.hasAttachments,
            swapSenderWithSubject = !appearance.senderAboveSubject,
        )

        else -> UnreadMessageItem(
            sender = "${item.displayName}",
            subject = item.subject ?: "n/a",
            preview = item.previewText,
            receivedAt = receivedAt,
            avatar = {
                if (appearance.showContactPicture) {
                    ContactImageAvatar(
                        address = item.displayAddress ?: Address(""),
                        imageLoader = imageLoader,
                    )
                }
            },
            onClick = onClick,
            onLongClick = onLongClick,
            onLeadingClick = onAvatarClick,
            onFavouriteChange = onFavouriteClick,
            favourite = item.isStarred,
            selected = isSelected,
            maxPreviewLines = appearance.previewLines,
            threadCount = item.threadCount,
            hasAttachments = item.hasAttachments,
            swapSenderWithSubject = !appearance.senderAboveSubject,
        )
    }
}

@Composable
fun ContactImageAvatar(address: Address, imageLoader: ImageLoader, modifier: Modifier = Modifier) {
    val model = remember(address) {
        ContactImageModel(address = address, contactLetterOnly = false)
    }
    CoilImage(
        imageModel = { model },
        imageLoader = { imageLoader },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
        ),
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape),
        loading = {
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        },
    )
}
