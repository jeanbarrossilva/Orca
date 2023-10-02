package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.HttpStyleEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.startingAt
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.std.styledstring.toStyledString
import java.net.URL
import java.time.ZonedDateTime

/**
 * Primitive information to be persisted about an [HttpToot].
 *
 * @param id Unique identifier.
 * @param authorID ID of the [Author] that has authored the [HttpToot].
 * @param headlineTitle Title of the [HttpToot]'s [content][HttpToot.content]'s
 * [highlight][Content.highlight] [headline][Highlight.headline].
 * @param headlineSubtitle Subtitle of the [HttpToot]'s [content][HttpToot.content]'s
 * [highlight][Content.highlight] [headline][Highlight.headline].
 * @param headlineCoverURL URL [String] that leads to the cover image of the [HttpToot]'s
 * [content][HttpToot.content]'s [highlight][Content.highlight] [headline][Highlight.headline].
 * @param publicationDateTime [String] representation of the moment in which the [HttpToot] was
 * published.
 * @param commentCount Amount of comments that the [HttpToot] has received.
 * @param isFavorite Whether the [HttpToot] has been favorited by the currently
 * [authenticated][Actor.Authenticated] [Actor].
 * @param isReblogged Whether the [HttpToot] is reblogged.
 * @param reblogCount Amount of times the [HttpToot] has been reblogged.
 * @param url URL [String] that leads to the [HttpToot].
 **/
@Entity(tableName = "toots")
internal data class HttpTootEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "author_id") val authorID: String,
    val text: String,
    @ColumnInfo(name = "headline_title") val headlineTitle: String?,
    @ColumnInfo(name = "headline_subtitle") val headlineSubtitle: String?,
    @ColumnInfo(name = "headline_cover_url") val headlineCoverURL: String?,
    @ColumnInfo(name = "publication_date_time") val publicationDateTime: String,
    @ColumnInfo(name = "comment_count") val commentCount: Int,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "favorite_count") val favoriteCount: Int,
    @ColumnInfo(name = "is_reblogged") val isReblogged: Boolean,
    @ColumnInfo(name = "reblog_count") val reblogCount: Int,
    @ColumnInfo(name = "url") val url: String
) {
    /**
     * Converts this [HttpTootEntity] into an [HttpToot].
     *
     * @param profileCache [Cache] from which the [Author]'s [Profile] will be retrieved.
     * @param dao [HttpTootEntityDao] that will select the persisted
     * [HTTP style entities][HttpStyleEntity].
     **/
    suspend fun toHttpToot(profileCache: Cache<Profile>, dao: HttpTootEntityDao): HttpToot {
        val author = profileCache.get(authorID).toAuthor()
        val styles = dao.selectWithStylesByID(id).styles
        val text = text.toStyledString { URL(styles.startingAt(it).url) }
        val content = Content.from(text) {
            if (headlineTitle != null && headlineCoverURL != null) {
                Headline(headlineTitle, headlineSubtitle, URL(headlineCoverURL))
            } else {
                null
            }
        }
        val publicationDateTime = ZonedDateTime.parse(publicationDateTime)
        val url = URL(url)
        return HttpToot(
            id,
            author,
            content,
            publicationDateTime,
            commentCount,
            favoriteCount,
            reblogCount,
            url
        )
    }

    companion object {
        /**
         * Creates an [HttpTootEntity] from the given [toot].
         *
         * @param toot [HttpToot] from which the [HttpTootEntity] will be created.
         **/
        suspend fun from(toot: HttpToot): HttpTootEntity {
            return HttpTootEntity(
                toot.id,
                toot.author.id,
                "${toot.content.text}",
                toot.content.highlight?.headline?.title,
                toot.content.highlight?.headline?.subtitle,
                "${toot.content.highlight?.headline?.coverURL}",
                "${toot.publicationDateTime}",
                toot.comment.countFlow.value,
                toot.favorite.isEnabledFlow.value,
                toot.favorite.countFlow.value,
                toot.reblog.isEnabledFlow.value,
                toot.reblog.countFlow.value,
                "${toot.url}"
            )
        }
    }
}
