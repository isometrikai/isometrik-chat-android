package io.isometrik.chat.utils

import java.io.Serializable

/**
 * Data class to hold link preview information
 */
data class LinkPreviewModel(
    val url: String,
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val siteName: String? = null
) : Serializable
