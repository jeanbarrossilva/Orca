package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import android.graphics.Typeface
import android.os.Build
import android.text.Spanned
import android.text.style.StyleSpan
import com.jeanbarrossilva.orca.std.styledstring.Style
import com.jeanbarrossilva.orca.std.styledstring.type.Bold
import com.jeanbarrossilva.orca.std.styledstring.type.Italic

/**
 * Creates a [StyleSpan], defining its font weight adjustment if the version of Android currently
 * being used supports it.
 *
 * @param style One of the constants defined by [Typeface] that determines the style of the
 *   [StyleSpan].
 * @param fontWeightAdjustment Adjustment to be made to the supplied font weight.
 * @see StyleSpan.getFontWeightAdjustment
 */
internal fun StyleSpan(style: Int, fontWeightAdjustment: Int): StyleSpan {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    StyleSpan(style, fontWeightAdjustment)
  } else {
    StyleSpan(style)
  }
}

/**
 * Whether this [StyleSpan] is structurally equal to the [other].
 *
 * @param other [StyleSpan] to which this one will be structurally compared.
 */
internal fun StyleSpan.isStructurallyEqualTo(other: StyleSpan): Boolean {
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    style == other.style && fontWeightAdjustment == other.fontWeightAdjustment
  } else {
    style == other.style
  }
}

/**
 * Converts this [StyleSpan] into [Style]s.
 *
 * @param indices Indices of a [Spanned] to which this [StyleSpan] has been applied.
 */
internal fun StyleSpan.toStyles(indices: IntRange): List<Style> {
  return when (style) {
    Typeface.BOLD -> listOf(Bold(indices))
    Typeface.BOLD_ITALIC -> listOf(Bold(indices), Italic(indices))
    Typeface.ITALIC -> listOf(Italic(indices))
    else -> emptyList()
  }
}
