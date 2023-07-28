package com.jeanbarrossilva.mastodonte.core.mastodon.profile.edit

import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.mastodon.Mastodon
import com.jeanbarrossilva.mastodonte.core.profile.edit.Editor
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.parametersOf
import io.ktor.utils.io.streams.asInput
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.name

internal class MastodonEditor(private val authenticationLock: AuthenticationLock) : Editor {
    @Suppress("UNREACHABLE_CODE", "UNUSED_VARIABLE")
    override suspend fun setAvatarURL(avatarURL: URL) {
        val file: Path = TODO()
        val fileAsFile = file.toFile()
        val fileLength = fileAsFile.length()
        val inputProvider = InputProvider(fileLength) { fileAsFile.inputStream().asInput() }
        val contentDisposition = "form-data; name=\"avatar\" filename=\"${file.name}\""
        val headers = Headers.build { append(HttpHeaders.ContentDisposition, contentDisposition) }
        val formData = formData { append("avatar", inputProvider, headers) }
        Mastodon.httpClient.submitFormWithBinaryData(ROUTE, formData) {
            authenticationLock.unlock {
                bearerAuth(it.accessToken)
            }
        }
    }

    override suspend fun setName(name: String) {
        authenticationLock.unlock {
            Mastodon.httpClient.submitForm(ROUTE, parametersOf("display_name", name)) {
                bearerAuth(it.accessToken)
            }
        }
    }

    override suspend fun setBio(bio: String) {
        authenticationLock.unlock {
            Mastodon.httpClient.submitForm(ROUTE, parametersOf("note", bio)) {
                bearerAuth(it.accessToken)
            }
        }
    }

    companion object {
        private const val ROUTE = "api/v1/accounts/update_credentials"
    }
}
