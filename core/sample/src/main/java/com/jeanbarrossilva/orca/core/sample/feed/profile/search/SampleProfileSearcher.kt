package com.jeanbarrossilva.orca.core.sample.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.core.feed.profile.search.toProfileSearchResult
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * [ProfileSearcher] that searches through the sample [Profile]s.
 *
 * @param provider [SampleProfileProvider] by which [Profile]s will be provided.
 */
internal class SampleProfileSearcher(private val provider: SampleProfileProvider) :
  ProfileSearcher() {
  override suspend fun onSearch(query: String): Flow<List<ProfileSearchResult>> {
    return provider.profilesFlow.map { profiles ->
      profiles.map(Profile::toProfileSearchResult).filter { profile ->
        profile.account.toString().contains(query, ignoreCase = true) ||
          profile.name.contains(query, ignoreCase = true)
      }
    }
  }
}
