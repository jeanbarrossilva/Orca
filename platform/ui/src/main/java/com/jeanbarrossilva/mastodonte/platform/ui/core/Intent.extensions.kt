package com.jeanbarrossilva.mastodonte.platform.ui.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.os.bundleOf
import java.net.URL

/**
 * [Intent] through which the [Activity] can be started.
 *
 * @param context [Context] to create the [Intent] with.
 * @param args Arguments to be passed to the [Intent]'s [extras][Intent.getExtras].
 * @see Context.startActivity
 **/
inline fun <reified T : Activity> Intent(
    context: Context,
    vararg args: Pair<String, Any?>
): Intent {
    val extras = bundleOf(*args)
    return Intent(context, T::class.java).apply { putExtras(extras) }
}

/**
 * [Intent] that allows the user to share the [text] externally.
 *
 * @param text Content to be shared.
 **/
fun Intent(text: String): Intent {
    return Intent(Intent.ACTION_SEND).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
}

/**
 * [Intent] for requesting or directly opening the [url] in a web browser.
 *
 * @param url [URL] to be accessed externally.
 **/
fun Intent(url: URL): Intent {
    val uri = Uri.parse("$url")
    return Intent(Intent.ACTION_VIEW, uri).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
}
