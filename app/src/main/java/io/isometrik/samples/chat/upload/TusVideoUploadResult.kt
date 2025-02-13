package io.isometrik.samples.chat.upload

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import io.isometrik.samples.chat.MyApplication
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

data class UploadResult(
    val videoUrl: String?,
    val thumbnailUrl: String?
)

suspend fun startUploadingVideoAndThumbnail(
    path: String,
    mediaType: String,
    uploadManager: UploadManager,
    logEnabled: Boolean = true
): UploadResult {
    return withContext(Dispatchers.IO) {
        // Start uploading the main media file
        val jobMedia = async {
            if (logEnabled) println("TUS Media started")
            uploadFileWithResult(uploadManager, path, mediaType, true)
        }

        var mediaUrl: String? = null
        var thumbUrl: String? = null

        if (mediaType != "video") {
            mediaUrl = jobMedia.await()
            thumbUrl = mediaUrl
        } else {
            // Start uploading the thumbnail if it's a video
            if (logEnabled) println("TUS Thumbnail started")
            val jobThumbnail = async {
                val thumbnailPath = MyApplication.getInstance().getExternalFilesDir(null)
                    .toString() + "/" + MyApplication.APP_NAME + "/thumbnail.jpg"
                val success = createThumbnail(path, thumbnailPath)
                if (success) {
                    if (logEnabled) println("Thumbnail created successfully at $thumbnailPath")
                    uploadFileWithResult(uploadManager, thumbnailPath, "image",true)
                } else {
                    if (logEnabled) println("Failed to create thumbnail.")
                    ""
                }
            }
            mediaUrl = jobMedia.await()
            thumbUrl = jobThumbnail.await()
        }

        if (logEnabled) println("TUS final mediaUrl $mediaUrl thumbUrl $thumbUrl")
        UploadResult(mediaUrl, thumbUrl)
    }
}

// Java-friendly method to call the suspend function
fun startUploadingVideoAndThumbnailJava(
    path: String,
    mediaType: String,
    uploadManager: UploadManager,
    callback: (UploadResult) -> Unit
) {
    // Use a coroutine scope to launch the suspend function from Java
    CoroutineScope(Dispatchers.Main).launch {
        val result = startUploadingVideoAndThumbnail(path, mediaType, uploadManager)
        callback(result)
    }
}

    suspend fun uploadFileWithResult(
        uploadManager: UploadManager,
        path: String,
        mediaType: String,
        LOG_ENABLE: Boolean
    ): String = withContext(
        Dispatchers.IO
    ) {
        // Create a CompletableDeferred to be completed in the callback
        val deferredResult = CompletableDeferred<String>()

        // Call the upload method
        uploadManager.uploadFile(path, mediaType, object : UploadManager.UploadCallback {
            override fun onProgress(progress: UploadManager.UploadResult.Progress) {
                val percentage = (progress.uploadedBytes.toFloat() / progress.totalBytes) * 100
                if (LOG_ENABLE)
                    println("TUS M-Progress: $percentage%")
            }

            override fun onSuccess(success: UploadManager.UploadResult.Success) {
                if (LOG_ENABLE)
                    println("TUS M-Upload Success: " + success.finalURL)

                val optimisedUrl = success.finalURL
                if (LOG_ENABLE)
                    println("TUS M-Upload url : $optimisedUrl")

                // Complete the deferred with the URL
                deferredResult.complete(optimisedUrl)
            }

            override fun onError(error: UploadManager.UploadResult.Error) {
                error.exception.printStackTrace()
                if (LOG_ENABLE)
                    println("TUS M-Upload onError: $error")

                // Complete the deferred with an error message
                deferredResult.completeExceptionally(error.exception)
            }
        })

        // Await the result from the deferred
        return@withContext deferredResult.await()
    }

    suspend fun saveBitmapToFile(bitmap: Bitmap, filePath: String): Boolean {
        return try {
            val file = File(filePath)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun getVideoThumbnail(videoPath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(videoPath)
            // Retrieve a frame at 1 second (or any other time point you want)
            return retriever.getFrameAtTime(1000000) // Time in microseconds
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            retriever.release()
        }
    }

    suspend fun createThumbnail(videoPath: String, thumbnailPath: String): Boolean {
        val bitmap = getVideoThumbnail(videoPath)
        return bitmap?.let { saveBitmapToFile(it, thumbnailPath) } ?: false
    }

