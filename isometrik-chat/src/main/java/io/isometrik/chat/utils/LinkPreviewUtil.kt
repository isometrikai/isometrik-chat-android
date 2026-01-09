package io.isometrik.chat.utils

import android.text.SpannableString
import android.text.util.Linkify
import android.util.Patterns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object LinkPreviewUtil {
    
    private val urlPattern: Pattern = Patterns.WEB_URL
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    /**
     * Extract the first URL from text message
     */
    fun extractFirstUrl(text: String?): String? {
        if (text.isNullOrEmpty()) return null
        
        val matcher = urlPattern.matcher(text)
        if (matcher.find()) {
            var url = matcher.group()
            // Ensure URL has protocol
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }
            return url
        }
        return null
    }

    /**
     * Make links clickable in SpannableString
     */
    fun makeLinksClickable(spannableString: SpannableString): SpannableString {
        Linkify.addLinks(spannableString, Linkify.WEB_URLS)
        return spannableString
    }

    /**
     * Fetch link preview metadata from URL
     * This should be called on a background thread
     */
    suspend fun fetchLinkPreview(url: String): LinkPreviewModel? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val html = response.body?.string() ?: return@withContext null
            val doc = Jsoup.parse(html)

            // Extract Open Graph tags first, fallback to standard meta tags
            val title = doc.select("meta[property=og:title]").attr("content")
                .takeIf { it.isNotEmpty() } 
                ?: doc.select("meta[name=twitter:title]").attr("content")
                .takeIf { it.isNotEmpty() }
                ?: doc.title()
                .takeIf { it.isNotEmpty() }

            val description = doc.select("meta[property=og:description]").attr("content")
                .takeIf { it.isNotEmpty() }
                ?: doc.select("meta[name=twitter:description]").attr("content")
                .takeIf { it.isNotEmpty() }
                ?: doc.select("meta[name=description]").attr("content")
                .takeIf { it.isNotEmpty() }

            val imageUrl = doc.select("meta[property=og:image]").attr("content")
                .takeIf { it.isNotEmpty() }
                ?: doc.select("meta[name=twitter:image]").attr("content")
                .takeIf { it.isNotEmpty() }
                ?: doc.select("meta[name=twitter:image:src]").attr("content")
                .takeIf { it.isNotEmpty() }

            val siteName = doc.select("meta[property=og:site_name]").attr("content")
                .takeIf { it.isNotEmpty() }

            // Convert relative image URLs to absolute
            val absoluteImageUrl = imageUrl?.let { imgUrl ->
                if (imgUrl.startsWith("http://") || imgUrl.startsWith("https://")) {
                    imgUrl
                } else {
                    try {
                        val baseUrl = java.net.URL(url)
                        "${baseUrl.protocol}://${baseUrl.host}${if (imgUrl.startsWith("/")) imgUrl else "/$imgUrl"}"
                    } catch (e: Exception) {
                        imgUrl
                    }
                }
            }

            LinkPreviewModel(
                url = url,
                title = title,
                description = description,
                imageUrl = absoluteImageUrl,
                siteName = siteName
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
