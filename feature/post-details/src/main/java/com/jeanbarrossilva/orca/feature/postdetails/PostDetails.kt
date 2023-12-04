/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.feature.postdetails

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.list.ListLoadable
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.feature.postdetails.ui.header.Header
import com.jeanbarrossilva.orca.feature.postdetails.ui.header.formatted
import com.jeanbarrossilva.orca.feature.postdetails.viewmodel.PostDetailsViewModel
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBarWithBackNavigation
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.autos.reactivity.BottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.autos.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.autos.reactivity.rememberBottomAreaAvailabilityNestedScrollConnection
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.AccountFormatter
import com.jeanbarrossilva.orca.platform.ui.component.avatar.createSample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.Timeline
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.Figure
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.formatted
import com.jeanbarrossilva.orca.platform.ui.component.timeline.refresh.Refresh
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import java.io.Serializable
import java.net.URL
import java.time.ZonedDateTime

@Immutable
internal data class PostDetails(
  val id: String,
  val avatarLoader: SomeImageLoader,
  val name: String,
  private val account: Account,
  val text: AnnotatedString,
  val figure: Figure?,
  private val publicationDateTime: ZonedDateTime,
  private val commentCount: Int,
  val isFavorite: Boolean,
  private val favoriteCount: Int,
  val isReblogged: Boolean,
  private val reblogCount: Int,
  val url: URL
) : Serializable {
  val formattedPublicationDateTime = publicationDateTime.formatted
  val formattedUsername = AccountFormatter.username(account)
  val formattedCommentCount = commentCount.formatted
  val formattedFavoriteCount = favoriteCount.formatted
  val formattedReblogCount = reblogCount.formatted

  companion object {
    val sample
      @Composable
      get() = Post.createSample(ImageLoader.Provider.createSample()).toPostDetails(onLinkClick = {})
  }
}

@Composable
internal fun PostDetails(
  viewModel: PostDetailsViewModel,
  boundary: PostDetailsBoundary,
  onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener,
  modifier: Modifier = Modifier
) {
  val postLoadable by viewModel.detailsLoadableFlow.collectAsState()
  val commentsLoadable by viewModel.commentsLoadableFlow.collectAsState()
  var isTimelineRefreshing by remember { mutableStateOf(false) }
  val bottomAreaAvailabilityNestedScrollConnection =
    rememberBottomAreaAvailabilityNestedScrollConnection(onBottomAreaAvailabilityChangeListener)

  PostDetails(
    postLoadable,
    commentsLoadable,
    isTimelineRefreshing,
    onTimelineRefresh = {
      isTimelineRefreshing = true
      viewModel.requestRefresh { isTimelineRefreshing = false }
    },
    onFavorite = viewModel::favorite,
    onRepost = viewModel::repost,
    onShare = viewModel::share,
    onNavigateToDetails = boundary::navigateToPostDetails,
    onNext = viewModel::loadCommentsAt,
    onBackwardsNavigation = boundary::pop,
    bottomAreaAvailabilityNestedScrollConnection,
    modifier
  )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PostDetails(
  postLoadable: Loadable<PostDetails>,
  commentsLoadable: ListLoadable<PostPreview>,
  isTimelineRefreshing: Boolean,
  onTimelineRefresh: () -> Unit,
  onFavorite: (postID: String) -> Unit,
  onRepost: (postID: String) -> Unit,
  onShare: (URL) -> Unit,
  onNavigateToDetails: (postID: String) -> Unit,
  onNext: (index: Int) -> Unit,
  onBackwardsNavigation: () -> Unit,
  bottomAreaAvailabilityNestedScrollConnection: BottomAreaAvailabilityNestedScrollConnection,
  modifier: Modifier = Modifier
) {
  val topAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior

  Scaffold(
    modifier,
    topBar = {
      @OptIn(ExperimentalMaterial3Api::class)
      TopAppBarWithBackNavigation(
        onNavigation = onBackwardsNavigation,
        title = { AutoSizeText(stringResource(R.string.feature_post_details)) },
        scrollBehavior = topAppBarScrollBehavior
      )
    }
  ) {
    Timeline(
      commentsLoadable,
      onFavorite,
      onRepost,
      onShare,
      onClick = onNavigateToDetails,
      onNext,
      Modifier.nestedScroll(bottomAreaAvailabilityNestedScrollConnection)
        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
      contentPadding = it,
      refresh =
        Refresh(isTimelineRefreshing, indicatorOffset = it.calculateTopPadding(), onTimelineRefresh)
    ) {
      when (postLoadable) {
        is Loadable.Loading -> Header()
        is Loadable.Loaded ->
          Header(
            postLoadable.content,
            onFavorite = { onFavorite(postLoadable.content.id) },
            onRepost = { onRepost(postLoadable.content.id) },
            onShare = { onShare(postLoadable.content.url) }
          )
        is Loadable.Failed -> Unit
      }
    }
  }
}

@Composable
@MultiThemePreview
private fun LoadingPostDetailsPreview() {
  AutosTheme { PostDetails(Loadable.Loading(), commentsLoadable = ListLoadable.Loading()) }
}

@Composable
@MultiThemePreview
private fun LoadedPostDetailsWithoutComments() {
  AutosTheme {
    PostDetails(Loadable.Loaded(PostDetails.sample), commentsLoadable = ListLoadable.Empty())
  }
}

@Composable
@MultiThemePreview
private fun LoadedPostDetailsPreview() {
  AutosTheme {
    PostDetails(Loadable.Loaded(PostDetails.sample), commentsLoadable = ListLoadable.Loading())
  }
}

@Composable
private fun PostDetails(
  postLoadable: Loadable<PostDetails>,
  commentsLoadable: ListLoadable<PostPreview>,
  modifier: Modifier = Modifier
) {
  PostDetails(
    postLoadable,
    commentsLoadable,
    isTimelineRefreshing = false,
    onTimelineRefresh = {},
    onFavorite = {},
    onRepost = {},
    onShare = {},
    onNavigateToDetails = {},
    onNext = {},
    onBackwardsNavigation = {},
    BottomAreaAvailabilityNestedScrollConnection.empty,
    modifier
  )
}
