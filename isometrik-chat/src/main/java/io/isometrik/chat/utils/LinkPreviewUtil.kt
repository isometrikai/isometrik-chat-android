package io.isometrik.chat.utils

import android.text.SpannableString
import android.text.util.Linkify
import android.util.Log
import android.util.Patterns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.jsoup.Jsoup
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream

object LinkPreviewUtil {
    
    private val urlPattern: Pattern = Patterns.WEB_URL
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
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
     * Fetch Branch.io link metadata using their API
     * Note: Branch.io API typically requires authentication, but we try public endpoints
     */
    private suspend fun fetchBranchIoMetadata(branchUrl: String): LinkPreviewModel? {
        val tag = "LinkPreviewUtil"
        try {
            // Extract link identifier from Branch.io URL
            val linkIdMatch = Regex("app\\.link/([^/?]+)").find(branchUrl)
            val linkId = linkIdMatch?.groupValues?.get(1)
            
            // Try multiple Branch.io API endpoints
            val apiEndpoints = mutableListOf<Pair<String, Request.Builder.() -> Unit>>()
            
            // Endpoint 1: POST to /v1/url with URL
            apiEndpoints.add(Pair("https://api2.branch.io/v1/url") { 
                val requestBody = JSONObject().apply {
                    put("url", branchUrl)
                }.toString()
                this.post(requestBody.toRequestBody("application/json".toMediaType()))
                    .header("Content-Type", "application/json")
            })
            
            // Endpoint 2: GET with URL parameter
            if (linkId != null) {
                val encodedUrl = URLEncoder.encode(branchUrl, "UTF-8")
                apiEndpoints.add(Pair("https://api2.branch.io/v1/url?url=$encodedUrl") {
                    this.get()
                })
            }
            
            for ((apiUrl, requestBuilder) in apiEndpoints) {
                try {
                    val request = Request.Builder()
                        .url(apiUrl)
                        .header("Accept", "application/json")
                        .apply(requestBuilder)
                        .build()
                    
                    val response = httpClient.newCall(request).execute()
                    
                    if (response.isSuccessful) {
                        val jsonString = response.body?.string()
                        if (jsonString != null) {
                            val json = JSONObject(jsonString)
                            
                            // Extract metadata from Branch.io response
                            val ogData = json.optJSONObject("og_data")
                            val data = json.optJSONObject("data")
                            
                            // Branch.io uses keys like "$og_title" (with dollar sign) in their JSON
                            val title = ogData?.optString("\$og_title")
                                ?: ogData?.optString("og_title")
                                ?: ogData?.optString("title")
                                ?: data?.optString("\$og_title")
                                ?: data?.optString("og_title")
                                ?: data?.optString("title")
                                ?: json.optString("\$og_title")
                                ?: json.optString("og_title")
                                ?: json.optString("title")
                            
                            val description = ogData?.optString("\$og_description")
                                ?: ogData?.optString("og_description")
                                ?: ogData?.optString("description")
                                ?: data?.optString("\$og_description")
                                ?: data?.optString("og_description")
                                ?: data?.optString("description")
                                ?: json.optString("\$og_description")
                                ?: json.optString("og_description")
                                ?: json.optString("description")
                            
                            val imageUrl = ogData?.optString("\$og_image")
                                ?: ogData?.optString("og_image")
                                ?: ogData?.optString("og_image_url")
                                ?: ogData?.optString("image_url")
                                ?: ogData?.optString("image")
                                ?: data?.optString("\$og_image")
                                ?: data?.optString("og_image")
                                ?: data?.optString("og_image_url")
                                ?: data?.optString("image_url")
                                ?: json.optString("\$og_image")
                                ?: json.optString("og_image")
                                ?: json.optString("og_image_url")
                                ?: json.optString("image_url")
                            
                            if (title != null || description != null || imageUrl != null) {
                                return LinkPreviewModel(
                                    url = branchUrl,
                                    title = title,
                                    description = description,
                                    imageUrl = imageUrl,
                                    siteName = "Branch.io"
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Silently fail and try next endpoint
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error fetching Branch.io metadata: ${e.message}")
        }
        return null
    }
    
    /**
     * Fetch link preview metadata from URL
     * This should be called on a background thread
     */
    suspend fun fetchLinkPreview(url: String): LinkPreviewModel? = withContext(Dispatchers.IO) {
        // Try Branch.io API first if it's a Branch.io link
        if (url.contains("app.link") || url.contains("branch.io")) {
            val branchMetadata = fetchBranchIoMetadata(url)
            if (branchMetadata != null && 
                (!branchMetadata.title.isNullOrEmpty() || 
                 !branchMetadata.description.isNullOrEmpty() || 
                 !branchMetadata.imageUrl.isNullOrEmpty())) {
                return@withContext branchMetadata
            }
        }
        
        fetchLinkPreviewInternal(url, 0)
    }
    
    /**
     * Internal function to fetch link preview with depth tracking
     */
    private suspend fun fetchLinkPreviewInternal(url: String, depth: Int): LinkPreviewModel? {
        val tag = "LinkPreviewUtil"
        
        if (depth > 3) {
            return null // Max 3 redirects to prevent infinite loops
        }
        
        // Retry logic for network failures (max 2 retries)
        for (attempt in 0..2) {
            try {
                if (attempt > 0) {
                    // Wait a bit before retrying (exponential backoff)
                    kotlinx.coroutines.delay(1000L * attempt)
                }
                
                // Check if this is a Branch.io link - use desktop User-Agent to get web version with Open Graph tags
                // Other apps like Telegram/Teams use desktop User-Agent, which makes Branch.io serve web version
                val isBranchLink = url.contains("app.link") || url.contains("branch.io")
                val userAgent = if (isBranchLink) {
                    // Desktop/Web User-Agent - Branch.io will serve web version with Open Graph tags
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                } else {
                    // Mobile User-Agent for other links
                    "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36"
                }
                
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", userAgent)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    // Don't set Accept-Encoding explicitly - let OkHttp handle gzip automatically
                    .build()

                val response = httpClient.newCall(request).execute()
                
                // Handle redirects explicitly - get final URL after redirects
                val finalUrl = response.request.url.toString()
                
                // Check for Location header redirect (some services use this)
                val locationHeader = response.header("Location")
                if (locationHeader != null && locationHeader.isNotEmpty()) {
                    // Handle Android Intent URLs (intent://...)
                    if (locationHeader.startsWith("intent://")) {
                        // Extract browser_fallback_url from Intent URL
                        val fallbackUrlPatterns = listOf(
                            Regex("[SB]\\.browser_fallback_url=([^;#]+)", RegexOption.IGNORE_CASE),
                            Regex("browser_fallback_url=([^;#]+)", RegexOption.IGNORE_CASE)
                        )
                        
                        var fallbackUrl: String? = null
                        for (pattern in fallbackUrlPatterns) {
                            val fallbackMatch = pattern.find(locationHeader)
                            if (fallbackMatch != null) {
                                fallbackUrl = fallbackMatch.groupValues[1]
                                break
                            }
                        }
                        
                        if (fallbackUrl != null) {
                            try {
                                val decodedUrl = java.net.URLDecoder.decode(fallbackUrl, "UTF-8")
                                if (decodedUrl.startsWith("http")) {
                                    return fetchLinkPreviewInternal(decodedUrl, depth + 1)
                                }
                            } catch (e: Exception) {
                                Log.e(tag, "Error decoding fallback URL: ${e.message}")
                            }
                        }
                    } else if (locationHeader.startsWith("http")) {
                        // Only follow if it's not a Branch.io URL (to avoid loops)
                        if (!locationHeader.contains("app.link") && !locationHeader.contains("branch.io")) {
                            return fetchLinkPreviewInternal(locationHeader, depth + 1)
                        }
                    }
                }
                
                // Handle redirect responses (3xx)
                if (response.code in 300..399) {
                    val finalUrlAfterRedirect = response.request.url.toString()
                    if (finalUrlAfterRedirect == url && (locationHeader == null || !locationHeader.startsWith("http"))) {
                        return null
                    }
                }
                
                // Accept successful responses
                if (!response.isSuccessful && response.code !in 300..399) {
                    return null
                }

                // For redirects (3xx), response body might be empty
                if (response.code in 300..399) {
                    return null
                }
                
                val responseBody = response.body ?: return null
                
                // OkHttp should automatically decompress gzip
                val html = try {
                    responseBody.string()
                } catch (e: Exception) {
                    Log.e(tag, "Error reading response body: ${e.message}")
                    return null
                }
                
                // Check if this is a Branch.io link for special handling
                if (isBranchLink) {
                    // Try to extract metadata from Branch.io HTML page
                    val branchMetadata = extractBranchIoMetadataFromHtml(html, url)
                    if (branchMetadata != null && 
                        (!branchMetadata.title.isNullOrEmpty() || 
                         !branchMetadata.description.isNullOrEmpty() || 
                         !branchMetadata.imageUrl.isNullOrEmpty())) {
                        return branchMetadata
                    }
                }
                
                // Check if content is gzipped (gzip files start with 0x1f 0x8b)
                val bytes = html.toByteArray(Charsets.ISO_8859_1)
                val isGzipped = bytes.size >= 2 && bytes[0].toInt() == 0x1f && bytes[1].toInt() == 0x8b
                
                if (isGzipped) {
                    // Try manual gzip decompression
                    try {
                        val gzipStream = GZIPInputStream(ByteArrayInputStream(bytes))
                        val decompressed = gzipStream.bufferedReader().readText()
                        gzipStream.close()
                        
                        // Check for redirects in decompressed content
                        val redirectUrl = extractRedirectUrl(decompressed, url)
                        if (redirectUrl != null && redirectUrl != url) {
                            return fetchLinkPreviewInternal(redirectUrl, depth + 1)
                        }
                        
                        // Parse decompressed HTML
                        val doc = Jsoup.parse(decompressed, finalUrl)
                        return extractMetadataFromDocument(doc, finalUrl, tag)
                    } catch (e: Exception) {
                        Log.e(tag, "Failed to decompress gzip: ${e.message}")
                        // Continue with original HTML
                    }
                }
                
                // Check if we're on a Firebase App Distribution page - don't follow redirects from here
                val isFirebaseAppDistribution = finalUrl.contains("appdistribution.firebase") || 
                                               finalUrl.contains("firebase.google.com") ||
                                               html.contains("appdistribution") ||
                                               html.contains("Firebase App Distribution")
                
                if (!isFirebaseAppDistribution) {
                    // Check for redirects (including JavaScript redirects for Branch.io)
                    val redirectUrl = extractRedirectUrl(html, url)
                    if (redirectUrl != null && redirectUrl != url) {
                        // Recursively try the redirect URL (limit depth to prevent infinite loops)
                        return fetchLinkPreviewInternal(redirectUrl, depth + 1)
                    }
                }
                
                // Parse HTML and extract metadata
                val doc = Jsoup.parse(html, finalUrl)
                return extractMetadataFromDocument(doc, finalUrl, tag)
                
            } catch (e: Exception) {
                val isNetworkError = e is java.net.SocketTimeoutException || 
                                     e is java.net.UnknownHostException ||
                                     e is java.net.ConnectException ||
                                     e.message?.contains("failed to connect") == true ||
                                     e.message?.contains("timeout") == true
                
                if (isNetworkError && attempt < 2) {
                    continue // Retry
                } else {
                    Log.e(tag, "Error fetching link preview for $url: ${e.message}")
                    // If it's the last attempt or not a network error, return null
                    if (!isNetworkError || attempt == 2) {
                        return null
                    }
                }
            }
        }
        
        // If we get here, all retries failed
        Log.e(tag, "Failed to fetch link preview after retries: $url")
        return null
    }
    
    /**
     * Extract metadata from parsed HTML document
     */
    private fun extractMetadataFromDocument(doc: org.jsoup.nodes.Document, finalUrl: String, tag: String): LinkPreviewModel? {
        try {
            // Extract Open Graph tags first, fallback to standard meta tags
            val ogTitle = doc.select("meta[property=og:title]").attr("content")
            val twitterTitle = doc.select("meta[name=twitter:title]").attr("content")
            val metaTitle = doc.select("meta[name=title]").attr("content")
            val docTitle = doc.title()
            
            // Extract from HTML content if meta tags are not available
            val htmlTitle = if (ogTitle.isEmpty() && twitterTitle.isEmpty() && metaTitle.isEmpty() && docTitle.isEmpty()) {
                doc.select(".card-title").firstOrNull()?.text()
                    ?: doc.select("[class*='title']").firstOrNull()?.text()
                    ?: doc.select("h1").firstOrNull()?.text()
                    ?: doc.select("h2").firstOrNull()?.text()
                    ?: doc.select("h3").firstOrNull()?.text()
            } else null
            
            val title = ogTitle.takeIf { it.isNotEmpty() } 
                ?: twitterTitle.takeIf { it.isNotEmpty() }
                ?: metaTitle.takeIf { it.isNotEmpty() }
                ?: htmlTitle?.takeIf { it.isNotEmpty() }
                ?: docTitle.takeIf { it.isNotEmpty() }

            val ogDescription = doc.select("meta[property=og:description]").attr("content")
            val twitterDescription = doc.select("meta[name=twitter:description]").attr("content")
            val metaDescription = doc.select("meta[name=description]").attr("content")
            
            // Extract from HTML content if meta tags are not available
            val htmlDescription = if (ogDescription.isEmpty() && twitterDescription.isEmpty() && metaDescription.isEmpty()) {
                doc.select(".app-content").firstOrNull()?.text()
                    ?: doc.select(".card-description").firstOrNull()?.text()
                    ?: doc.select("[class*='description']").firstOrNull()?.text()
                    ?: doc.select("p").firstOrNull()?.text()
            } else null
            
            val description = ogDescription.takeIf { it.isNotEmpty() }
                ?: twitterDescription.takeIf { it.isNotEmpty() }
                ?: metaDescription.takeIf { it.isNotEmpty() }
                ?: htmlDescription?.takeIf { it.isNotEmpty() }

            val ogImage = doc.select("meta[property=og:image]").attr("content")
            val twitterImage = doc.select("meta[name=twitter:image]").attr("content")
            val twitterImageSrc = doc.select("meta[name=twitter:image:src]").attr("content")
            val metaImage = doc.select("meta[name=image]").attr("content")
            
            // Extract image from CSS background-image if meta tags are not available
            var htmlImage: String? = null
            if (ogImage.isEmpty() && twitterImage.isEmpty() && twitterImageSrc.isEmpty() && metaImage.isEmpty()) {
                val styleTags = doc.select("style")
                for (style in styleTags) {
                    val styleContent = style.html()
                    // Look for background-image: url(...) patterns
                    val bgImagePattern = Regex("background-image\\s*:\\s*url\\(['\"]?([^'\")]+)['\"]?\\)", RegexOption.IGNORE_CASE)
                    val matches = bgImagePattern.findAll(styleContent)
                    for (match in matches) {
                        val imageUrl = match.groupValues[1]
                        // Filter out data URIs and generic Branch.io assets, prefer actual product images
                        if (!imageUrl.startsWith("data:") && 
                            !imageUrl.contains("branch-assets") &&
                            !imageUrl.contains("og_image.png") &&
                            (imageUrl.contains("s3.") || imageUrl.contains("amazonaws") || imageUrl.contains("cloudfront") || imageUrl.contains("http"))) {
                            htmlImage = imageUrl
                            break
                        }
                    }
                    if (htmlImage != null) break
                }
            }
            
            // Extract from img tags, but filter out app store button images
            var firstImgSrc: String? = null
            if (htmlImage == null) {
                val imgTags = doc.select("img[src]")
                for (img in imgTags) {
                    val src = img.attr("abs:src")
                    // Filter out app store button images and generic Branch.io assets
                    if (!src.contains("branch-assets") && 
                        !src.contains("og_image.png") &&
                        !src.contains("app-store") &&
                        src.startsWith("http")) {
                        firstImgSrc = src
                        break
                    }
                }
            }
            
            val imageUrl = ogImage.takeIf { it.isNotEmpty() }
                ?: twitterImage.takeIf { it.isNotEmpty() }
                ?: twitterImageSrc.takeIf { it.isNotEmpty() }
                ?: metaImage.takeIf { it.isNotEmpty() }
                ?: htmlImage?.takeIf { it.isNotEmpty() }
                // Fallback to first large image in the document
                ?: firstImgSrc?.takeIf { it.isNotEmpty() }

            val siteName = doc.select("meta[property=og:site_name]").attr("content")
                .takeIf { it.isNotEmpty() }

            // Convert relative image URLs to absolute
            val absoluteImageUrl = imageUrl?.let { imgUrl ->
                if (imgUrl.startsWith("http://") || imgUrl.startsWith("https://")) {
                    imgUrl
                } else {
                    try {
                        val baseUrl = java.net.URL(finalUrl)
                        "${baseUrl.protocol}://${baseUrl.host}${if (imgUrl.startsWith("/")) imgUrl else "/$imgUrl"}"
                    } catch (e: Exception) {
                        Log.e("LinkPreviewUtil", "Error converting relative image URL: ${e.message}")
                        imgUrl
                    }
                }
            }

            val previewModel = LinkPreviewModel(
                url = finalUrl,
                title = title,
                description = description,
                imageUrl = absoluteImageUrl,
                siteName = siteName
            )
            
            return previewModel
        } catch (e: Exception) {
            Log.e(tag, "Error extracting metadata from document: ${e.message}")
            return null
        }
    }
    
    /**
     * Extract redirect URL from HTML meta tags or JavaScript
     */
    private fun extractRedirectUrl(html: String, baseUrl: String): String? {
        try {
            val doc = Jsoup.parse(html, baseUrl)
            
            // Check if this is a Branch.io link
            val isBranchLink = baseUrl.contains("app.link") || baseUrl.contains("branch.io")
            
            // Branch.io specific: Look for fallback URL in meta tags or data attributes
            if (isBranchLink) {
                // Check for Branch.io fallback URL in meta tags
                val branchFallback = doc.select("meta[property=branch:deeplink]").attr("content")
                    .takeIf { it.isNotEmpty() }
                    ?: doc.select("meta[name=branch:deeplink]").attr("content")
                    .takeIf { it.isNotEmpty() }
                    ?: doc.select("meta[property=branch:fallback_url]").attr("content")
                    .takeIf { it.isNotEmpty() }
                    ?: doc.select("meta[name=branch:fallback_url]").attr("content")
                    .takeIf { it.isNotEmpty() }
                    ?: doc.select("meta[property=og:url]").attr("content")
                    .takeIf { it.isNotEmpty() && !it.contains("app.link") && !it.contains("branch.io") }
                
                if (branchFallback != null && branchFallback.isNotEmpty() && branchFallback.startsWith("http")) {
                    return branchFallback
                }
                
                // Check for fallback URL in validate() function calls (Branch.io pattern)
                val validatePattern = Regex("validate\\(['\"](https?://[^'\"]+)['\"]\\)", RegexOption.IGNORE_CASE)
                val validateMatches = validatePattern.findAll(html)
                
                // Collect all URLs from validate() calls
                val validateUrls = mutableListOf<String>()
                for (match in validateMatches) {
                    var url = match.groupValues[1]
                    // Decode HTML entities
                    url = url.replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">")
                    // Extract base URL without query parameters
                    val baseUrlMatch = Regex("(https?://[^?&]+)").find(url)
                    val cleanUrl = baseUrlMatch?.groupValues?.get(1) ?: url
                    if (cleanUrl.startsWith("http")) {
                        validateUrls.add(cleanUrl)
                    }
                }
                
                // Find the fallback URL (usually the one that's not a deep link scheme)
                for (url in validateUrls) {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        if (url != baseUrl) {
                            try {
                                val originalPath = java.net.URL(baseUrl).path
                                val fallbackPath = java.net.URL(url).path
                                
                                // If paths are different, it's likely the product page
                                if (fallbackPath != originalPath) {
                                    return url
                                }
                            } catch (e: Exception) {
                                // If parsing fails but URL is clearly different, use it
                                if (url.length > baseUrl.length + 5 && !url.contains("/i7CA74GtI0b")) {
                                    return url
                                }
                            }
                        }
                    }
                }
                
                // Check for $fallback_url in script data (multiple patterns)
                val fallbackUrlPatterns = listOf(
                    Regex("['\"]\\\$fallback_url['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("['\"]fallback_url['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("fallback_url\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("\\\$fallback_url\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE)
                )
                
                for (pattern in fallbackUrlPatterns) {
                    val fallbackMatch = pattern.find(html)
                    if (fallbackMatch != null) {
                        val fallbackUrl = fallbackMatch.groupValues[1]
                        if (fallbackUrl.startsWith("http") && !fallbackUrl.contains("app.link") && !fallbackUrl.contains("branch.io")) {
                            return fallbackUrl
                        }
                    }
                }
                
                // Check for Branch.io data object in script tags
                val scripts = doc.select("script")
                for (script in scripts) {
                    val scriptContent = script.html()
                    // Look for Branch.io initialization with data
                    val branchDataPattern = Regex("branch\\.(init|setBranchViewData|link)\\([^)]*['\"](https?://[^'\"]+)['\"]", RegexOption.IGNORE_CASE)
                    val branchMatch = branchDataPattern.find(scriptContent)
                    if (branchMatch != null) {
                        val url = branchMatch.groupValues[1]
                        if (url.startsWith("http") && !url.contains("app.link") && !url.contains("branch.io")) {
                            return url
                        }
                    }
                }
            }
            
            // Check for meta refresh redirect
            val metaRefresh = doc.select("meta[http-equiv=refresh]").attr("content")
            if (metaRefresh.isNotEmpty()) {
                val urlMatch = Regex("url=(.+)", RegexOption.IGNORE_CASE).find(metaRefresh)
                if (urlMatch != null) {
                    var redirectUrl = urlMatch.groupValues[1]
                    if (!redirectUrl.startsWith("http")) {
                        val base = java.net.URL(baseUrl)
                        redirectUrl = "${base.protocol}://${base.host}${if (redirectUrl.startsWith("/")) redirectUrl else "/$redirectUrl"}"
                    }
                    return redirectUrl
                }
            }
            
            // Check for Open Graph URL (sometimes used for redirects)
            val ogUrl = doc.select("meta[property=og:url]").attr("content")
            if (ogUrl.isNotEmpty() && ogUrl != baseUrl) {
                return ogUrl
            }
            
            // Check for canonical URL
            val canonicalUrl = doc.select("link[rel=canonical]").attr("href")
            if (canonicalUrl.isNotEmpty() && canonicalUrl != baseUrl) {
                return canonicalUrl
            }
            
            // Check for JavaScript redirects (common in Branch.io links)
            if (isBranchLink) {
                // Look for Branch.io data object with fallback_url
                val branchDataPatterns = listOf(
                    Regex("['\"]fallback_url['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("['\"]\\\$fallback_url['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("fallback_url\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("\\\$fallback_url\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE)
                )
                
                for (pattern in branchDataPatterns) {
                    val match = pattern.find(html)
                    if (match != null) {
                        val fallbackUrl = match.groupValues[1]
                        if (fallbackUrl.startsWith("http")) {
                            return fallbackUrl
                        }
                    }
                }
            }
            
            // Look for window.location, window.location.href, or location.href patterns
            val jsRedirectPatterns = listOf(
                Regex("window\\.location\\.href\\s*=\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                Regex("window\\.location\\s*=\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                Regex("location\\.href\\s*=\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                Regex("location\\s*=\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                // Branch.io specific patterns
                Regex("branch\\.deepLink\\(['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                Regex("branch\\.link\\(['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE)
            )
            
            // Check script tags for JavaScript redirects
            val scripts = doc.select("script")
            for (script in scripts) {
                val scriptContent = script.html()
                
                for (pattern in jsRedirectPatterns) {
                    val match = pattern.find(scriptContent)
                    if (match != null) {
                        var redirectUrl = match.groupValues.lastOrNull() ?: match.groupValues[1]
                        // Filter out common non-redirect URLs
                        if (redirectUrl.isNotEmpty() && 
                            !redirectUrl.contains("branch.io") && 
                            !redirectUrl.contains("app.link") &&
                            redirectUrl.startsWith("http")) {
                            return redirectUrl
                        }
                    }
                }
            }
            
            // Also check the raw HTML for URL patterns (in case scripts are inline)
            // Look for any HTTP URLs that aren't Branch.io URLs
            // BUT: Don't extract infrastructure URLs (CSP, CDN, Firebase, Google services, etc.)
            val urlPattern = Regex("(https?://[^\\s\"'<>)]+)", RegexOption.IGNORE_CASE)
            val urlMatches = urlPattern.findAll(html)
            for (match in urlMatches) {
                var redirectUrl = match.groupValues[1]
                // Filter out infrastructure and non-product URLs
                val isInfrastructureUrl = redirectUrl.contains("csp.withgoogle.com") ||
                                         redirectUrl.contains("gstatic.com") ||
                                         redirectUrl.contains("googleapis.com") ||
                                         redirectUrl.contains("google.com") ||
                                         redirectUrl.contains("firebase") ||
                                         redirectUrl.contains("appdistribution") ||
                                         redirectUrl.contains("branch.io") ||
                                         redirectUrl.contains("app.link") ||
                                         redirectUrl.contains("cdn") ||
                                         redirectUrl.contains("assets") ||
                                         redirectUrl.contains("static") ||
                                         redirectUrl.contains("fonts.googleapis.com") ||
                                         redirectUrl.contains("analytics") ||
                                         redirectUrl.contains("tagmanager")
                
                if (redirectUrl.isNotEmpty() && 
                    !isInfrastructureUrl &&
                    redirectUrl.startsWith("http") &&
                    redirectUrl.length > 20) { // Filter out very short URLs
                    return redirectUrl
                }
            }
            
            return null
        } catch (e: Exception) {
            Log.e("LinkPreviewUtil", "Error extracting redirect URL: ${e.message}")
            return null
        }
    }
    
    /**
     * Extract Branch.io metadata from HTML page
     * Branch.io might embed metadata in meta tags or JavaScript variables
     */
    private fun extractBranchIoMetadataFromHtml(html: String, url: String): LinkPreviewModel? {
        val tag = "LinkPreviewUtil"
        try {
            val doc = Jsoup.parse(html, url)
            
            // Check for Open Graph tags first (Branch.io might add them)
            val ogTitle = doc.select("meta[property=og:title]").attr("content")
            val ogDescription = doc.select("meta[property=og:description]").attr("content")
            val ogImage = doc.select("meta[property=og:image]").attr("content")
            
            // Check for Branch.io specific meta tags
            val branchTitle = doc.select("meta[property=branch:deeplink_title]").attr("content")
                .takeIf { it.isNotEmpty() }
                ?: doc.select("meta[name=branch:deeplink_title]").attr("content")
                .takeIf { it.isNotEmpty() }
            
            val branchDescription = doc.select("meta[property=branch:deeplink_description]").attr("content")
                .takeIf { it.isNotEmpty() }
                ?: doc.select("meta[name=branch:deeplink_description]").attr("content")
                .takeIf { it.isNotEmpty() }
            
            val branchImage = doc.select("meta[property=branch:deeplink_image]").attr("content")
                .takeIf { it.isNotEmpty() }
                ?: doc.select("meta[name=branch:deeplink_image]").attr("content")
                .takeIf { it.isNotEmpty() }
            
            // Try to extract from JavaScript variables (Branch.io might store metadata in JS)
            var jsTitle: String? = null
            var jsDescription: String? = null
            var jsImage: String? = null
            
            val scripts = doc.select("script")
            for (script in scripts) {
                val scriptContent = script.html()
                
                // Look for Branch.io data object
                val titlePatterns = listOf(
                    Regex("['\"]title['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("['\"]\\\$og_title['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("og_title\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE)
                )
                
                for (pattern in titlePatterns) {
                    val match = pattern.find(scriptContent)
                    if (match != null && jsTitle == null) {
                        jsTitle = match.groupValues[1].replace("&amp;", "&")
                        break
                    }
                }
                
                val descPatterns = listOf(
                    Regex("['\"]description['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("['\"]\\\$og_description['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("og_description\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE)
                )
                
                for (pattern in descPatterns) {
                    val match = pattern.find(scriptContent)
                    if (match != null && jsDescription == null) {
                        jsDescription = match.groupValues[1].replace("&amp;", "&")
                        break
                    }
                }
                
                val imagePatterns = listOf(
                    Regex("['\"]image['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("['\"]image_url['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("['\"]\\\$og_image['\"]\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE),
                    Regex("og_image\\s*[:=]\\s*['\"]([^'\"]+)['\"]", RegexOption.IGNORE_CASE)
                )
                
                for (pattern in imagePatterns) {
                    val match = pattern.find(scriptContent)
                    if (match != null && jsImage == null) {
                        jsImage = match.groupValues[1].replace("&amp;", "&")
                        break
                    }
                }
            }
            
            // Extract from HTML content (Branch.io web version has data in HTML, not meta tags)
            val htmlTitle = doc.select(".card-title").firstOrNull()?.text()
                ?: doc.select("[class*='title']").firstOrNull()?.text()
                ?: doc.select("h1").firstOrNull()?.text()
                ?: doc.select("h2").firstOrNull()?.text()
            
            val htmlDescription = doc.select(".app-content").firstOrNull()?.text()
                ?: doc.select(".card-description").firstOrNull()?.text()
                ?: doc.select("[class*='description']").firstOrNull()?.text()
                ?: doc.select("p").firstOrNull()?.text()
            
            // Extract image from CSS background-image or img tags
            var htmlImage: String? = null
            
            // Try to extract from CSS background-image in style tag
            val styleTags = doc.select("style")
            for (style in styleTags) {
                val styleContent = style.html()
                // Look for background-image: url(...) patterns
                val bgImagePattern = Regex("background-image\\s*:\\s*url\\(['\"]?([^'\")]+)['\"]?\\)", RegexOption.IGNORE_CASE)
                val matches = bgImagePattern.findAll(styleContent)
                for (match in matches) {
                    val imageUrl = match.groupValues[1]
                    // Filter out data URIs and generic Branch.io assets, prefer actual product images
                    if (!imageUrl.startsWith("data:") && 
                        !imageUrl.contains("branch-assets") &&
                        !imageUrl.contains("og_image.png") &&
                        (imageUrl.contains("s3.") || imageUrl.contains("amazonaws") || imageUrl.contains("cloudfront") || imageUrl.contains("http"))) {
                        htmlImage = imageUrl
                        break
                    }
                }
                if (htmlImage != null) break
            }
            
            // Fallback to img tags (but filter out app store buttons)
            if (htmlImage == null) {
                val imgTags = doc.select("img[src]")
                for (img in imgTags) {
                    val src = img.attr("abs:src")
                    // Filter out app store button images
                    if (!src.contains("branch-assets") && 
                        !src.contains("og_image.png") &&
                        src.startsWith("http")) {
                        htmlImage = src
                        break
                    }
                }
            }
            
            val title = ogTitle.takeIf { it.isNotEmpty() }
                ?: branchTitle
                ?: htmlTitle?.takeIf { it.isNotEmpty() }
                ?: jsTitle
                ?: doc.title().takeIf { it.isNotEmpty() }
            
            val description = ogDescription.takeIf { it.isNotEmpty() }
                ?: branchDescription
                ?: htmlDescription?.takeIf { it.isNotEmpty() }
                ?: jsDescription
            
            val imageUrl = ogImage.takeIf { it.isNotEmpty() }
                ?: branchImage
                ?: htmlImage?.takeIf { it.isNotEmpty() }
                ?: jsImage
            
            if (title != null || description != null || imageUrl != null) {
                return LinkPreviewModel(
                    url = url,
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    siteName = "Branch.io"
                )
            }
            
            return null
        } catch (e: Exception) {
            Log.e(tag, "Error extracting Branch.io metadata from HTML: ${e.message}")
            return null
        }
    }
}
