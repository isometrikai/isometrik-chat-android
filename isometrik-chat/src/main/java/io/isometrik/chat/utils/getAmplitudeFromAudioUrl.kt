package io.isometrik.chat.utils

import android.media.*
import java.nio.ByteBuffer

fun getAmplitudeFromAudioUrl(audioUrl: String, callback: (List<Int>) -> Unit) {
    Thread {
        try {
            val extractor = MediaExtractor()
            extractor.setDataSource(audioUrl)

            // Select the first audio track
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME)
                if (mime?.startsWith("audio/") == true) {
                    extractor.selectTrack(i)
                    break
                }
            }

            val format = extractor.getTrackFormat(extractor.sampleTrackIndex)
            val mimeType = format.getString(MediaFormat.KEY_MIME)
            val codec = MediaCodec.createDecoderByType(mimeType!!)
            codec.configure(format, null, null, 0)
            codec.start()

            val bufferInfo = MediaCodec.BufferInfo()
            val amplitudeList = mutableListOf<Int>()

            while (true) {
                val inputBufferIndex = codec.dequeueInputBuffer(10000)
                if (inputBufferIndex >= 0) {
                    val inputBuffer = codec.getInputBuffer(inputBufferIndex)
                    val sampleSize = extractor.readSampleData(inputBuffer!!, 0)
                    if (sampleSize < 0) break // End of stream

                    codec.queueInputBuffer(inputBufferIndex, 0, sampleSize, extractor.sampleTime, 0)
                    extractor.advance()
                }

                val outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 10000)
                if (outputBufferIndex >= 0) {
                    val outputBuffer = codec.getOutputBuffer(outputBufferIndex)
                    val amplitude = calculateAmplitude(outputBuffer!!, bufferInfo.size)
                    amplitudeList.add(amplitude)
                    codec.releaseOutputBuffer(outputBufferIndex, false)
                }
            }

            codec.stop()
            codec.release()
            extractor.release()

            callback(amplitudeList) // Return the amplitude values

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}

// Function to calculate amplitude from PCM buffer and return as an Int
private fun calculateAmplitude(buffer: ByteBuffer, size: Int): Int {
    buffer.rewind()
    var maxAmplitude = 0
    for (i in 0 until size step 2) { // PCM 16-bit (2 bytes per sample)
        val sample = buffer.getShort(i).toInt()
        maxAmplitude = maxOf(maxAmplitude, Math.abs(sample))
    }
    return maxAmplitude
}
