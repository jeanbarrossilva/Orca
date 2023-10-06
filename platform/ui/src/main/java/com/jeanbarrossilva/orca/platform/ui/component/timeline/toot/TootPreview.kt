package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.SmallTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.test.Loading
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.samples
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.theme.extensions.EmptyMutableInteractionSource
import com.jeanbarrossilva.orca.platform.theme.extensions.IgnoringMutableInteractionSource
import com.jeanbarrossilva.orca.platform.ui.AccountFormatter
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.SmallAvatar
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline.HeadlineCard
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat.FavoriteStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat.ReblogStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.RelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.rememberRelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.core.style.toAnnotatedString
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.compose.rememberImageLoader
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime

/** Tag that identifies a [TootPreview]'s name for testing purposes. **/
internal const val TOOT_PREVIEW_NAME_TAG = "toot-preview-name"

/** Tag that identifies [TootPreview]'s metadata for testing purposes. **/
internal const val TOOT_PREVIEW_METADATA_TAG = "toot-preview-metadata"

/** Tag that identifies a [TootPreview]'s body for testing purposes. **/
internal const val TOOT_PREVIEW_BODY_TAG = "toot-preview-body"

/** Tag that identifies a [TootPreview]'s comment count stat for testing purposes. **/
internal const val TOOT_PREVIEW_COMMENT_COUNT_STAT_TAG = "toot-preview-comments-stat"

/** Tag that identifies a [TootPreview]'s reblog count stat for testing purposes. **/
internal const val TOOT_PREVIEW_REBLOG_COUNT_STAT_TAG = "toot-preview-reblogs-stat"

/** Tag that identifies a [TootPreview]'s share action for testing purposes. **/
internal const val TOOT_PREVIEW_SHARE_ACTION_TAG = "toot-preview-share-action"

/** Tag that identifies a [TootPreview] for testing purposes. **/
const val TOOT_PREVIEW_TAG = "toot-preview"

/** [Modifier] to be applied to a [TootPreview]'s name. **/
private val nameModifier = Modifier.testTag(TOOT_PREVIEW_NAME_TAG)

/** [Modifier] to be applied to [TootPreview]'s metadata. **/
private val metadataModifier = Modifier.testTag(TOOT_PREVIEW_METADATA_TAG)

/** [Modifier] to be applied to a [TootPreview]'s body. **/
private val bodyModifier = Modifier.testTag(TOOT_PREVIEW_BODY_TAG)

/**
 * Information to be displayed on a [Toot]'s preview.
 *
 * @param id Unique identifier.
 * @param avatarURL [URL] that leads to the author's avatar.
 * @param name Name of the author.
 * @param account [Account] of the author.
 * @param text Content written by the author.
 * @param highlight [Highlight] from the [text].
 * @param publicationDateTime Zoned moment in time in which it was published.
 * @param commentCount Amount of comments.
 * @param isFavorite Whether it's marked as favorite.
 * @param favoriteCount Amount of times it's been marked as favorite.
 * @param isReblogged Whether it's reblogged.
 * @param reblogCount Amount of times it's been reblogged.
 * @param url [URL] that leads to the [Toot].
 **/
@Immutable
data class TootPreview(
    val id: String,
    val avatarURL: URL,
    val name: String,
    private val account: Account,
    val text: AnnotatedString,
    val highlight: Highlight?,
    private val publicationDateTime: ZonedDateTime,
    private val commentCount: Int,
    val isFavorite: Boolean,
    private val favoriteCount: Int,
    val isReblogged: Boolean,
    private val reblogCount: Int,
    internal val url: URL
) : Serializable {
    /** Formatted, displayable version of [commentCount]. **/
    val formattedCommentCount = commentCount.formatted

    /** Formatted, displayable version of [favoriteCount]. **/
    val formattedFavoriteCount = favoriteCount.formatted

    /** Formatted, displayable version of [reblogCount]. **/
    val formattedReblogCount = reblogCount.formatted

    /**
     * Gets information about the author and how much time it's been since it was published.
     *
     * @param relativeTimeProvider [RelativeTimeProvider] for providing relative time of
     * publication.
     **/
    fun getMetadata(relativeTimeProvider: RelativeTimeProvider): String {
        val username = AccountFormatter.username(account)
        val timeSincePublication = relativeTimeProvider.provide(publicationDateTime)
        return "$username • $timeSincePublication"
    }

    companion object {
        /** [TootPreview] sample. **/
        val sample
            @Composable get() = getSample(OrcaTheme.colors)

        /** [TootPreview] samples. **/
        val samples
            @Composable get() = Toot.samples.map { it.toTootPreview() }

        /** Gets a sample [TootPreview]. **/
        fun getSample(colors: Colors): TootPreview {
            return TootPreview(
                Toot.sample.id,
                Toot.sample.author.avatarURL,
                Toot.sample.author.name,
                Toot.sample.author.account,
                Toot.sample.content.text.toAnnotatedString(colors),
                Toot.sample.content.highlight,
                Toot.sample.publicationDateTime,
                Toot.sample.comment.count,
                Toot.sample.favorite.isEnabled,
                Toot.sample.favorite.count,
                Toot.sample.reblog.isEnabled,
                Toot.sample.reblog.count,
                Toot.sample.url
            )
        }
    }
}

@Composable
fun TootPreview(modifier: Modifier = Modifier) {
    TootPreview(
        avatar = { SmallAvatar() },
        name = { SmallTextualPlaceholder(nameModifier) },
        metadata = { MediumTextualPlaceholder(metadataModifier) },
        content = {
            Column(
                bodyModifier.semantics { set(SemanticsProperties.Loading, true) },
                Arrangement.spacedBy(OrcaTheme.spacings.extraSmall)
            ) {
                repeat(3) { LargeTextualPlaceholder() }
                MediumTextualPlaceholder()
            }
        },
        stats = { },
        onClick = null,
        modifier
    )
}

@Composable
fun TootPreview(
    preview: TootPreview,
    onHighlightClick: () -> Unit,
    onFavorite: () -> Unit,
    onReblog: () -> Unit,
    onShare: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = rememberImageLoader(),
    relativeTimeProvider: RelativeTimeProvider = rememberRelativeTimeProvider()
) {
    val metadata = remember(preview, relativeTimeProvider) {
        preview.getMetadata(relativeTimeProvider)
    }

    TootPreview(
        avatar = { SmallAvatar(preview.name, preview.avatarURL, imageLoader = imageLoader) },
        name = { Text(preview.name, nameModifier) },
        metadata = { Text(metadata, metadataModifier) },
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.medium)) {
                Text(preview.text, bodyModifier)

                preview.highlight?.headline?.let {
                    HeadlineCard(it, onHighlightClick)
                }
            }
        },
        stats = {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Stat(
                    StatPosition.LEADING,
                    OrcaTheme.iconography.comment.outlined,
                    contentDescription = stringResource(R.string.platform_ui_toot_preview_comments),
                    onClick = { },
                    Modifier.testTag(TOOT_PREVIEW_COMMENT_COUNT_STAT_TAG)
                ) {
                    Text(preview.formattedCommentCount)
                }

                FavoriteStat(StatPosition.SUBSEQUENT, preview, onClick = onFavorite)
                ReblogStat(StatPosition.SUBSEQUENT, preview, onClick = onReblog)

                Stat(
                    StatPosition.TRAILING,
                    OrcaTheme.iconography.share.outlined,
                    contentDescription = stringResource(R.string.platform_ui_toot_preview_share),
                    onClick = onShare,
                    Modifier.testTag(TOOT_PREVIEW_SHARE_ACTION_TAG)
                )
            }
        },
        onClick,
        modifier
    )
}

@Composable
private fun TootPreview(
    avatar: @Composable () -> Unit,
    name: @Composable () -> Unit,
    metadata: @Composable () -> Unit,
    content: @Composable () -> Unit,
    stats: @Composable () -> Unit,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember(onClick) {
        onClick
            ?.let { IgnoringMutableInteractionSource(HoverInteraction::class) }
            ?: EmptyMutableInteractionSource()
    }
    val spacing = OrcaTheme.spacings.medium

    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        onClick ?: { },
        modifier.testTag(TOOT_PREVIEW_TAG),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        interactionSource = interactionSource
    ) {
        Column(
            Modifier.padding(spacing),
            Arrangement.spacedBy(spacing)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                avatar()

                Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                    Column(
                        verticalArrangement = Arrangement
                            .spacedBy(OrcaTheme.spacings.extraSmall)
                    ) {
                        ProvideTextStyle(OrcaTheme.typography.bodyLarge, name)
                        ProvideTextStyle(OrcaTheme.typography.bodySmall, metadata)
                    }

                    content()
                    stats()
                }
            }
        }
    }
}

@Composable
@MultiThemePreview
private fun LoadingTootPreviewPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background.container) {
            TootPreview()
        }
    }
}

@Composable
@MultiThemePreview
private fun LoadedInactiveTootPreviewPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background.container) {
            TootPreview(TootPreview.sample.copy(isFavorite = false, isReblogged = false))
        }
    }
}

@Composable
@MultiThemePreview
private fun LoadedActiveTootPreviewPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colors.background.container) {
            TootPreview(TootPreview.sample.copy(isFavorite = true, isReblogged = true))
        }
    }
}

@Composable
private fun TootPreview(preview: TootPreview, modifier: Modifier = Modifier) {
    TootPreview(
        preview,
        onHighlightClick = { },
        onFavorite = { },
        onReblog = { },
        onShare = { },
        onClick = { },
        modifier
    )
}
