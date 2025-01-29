package io.isometrik.ui.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.arthenica.ffmpegkit.FFmpegKit
import io.isometrik.chat.R
import io.isometrik.ui.libwave.SeekBarOnProgressChanged
import io.isometrik.ui.libwave.Utils
import io.isometrik.ui.libwave.WaveGravity
import io.isometrik.ui.libwave.WaveformSeekBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

object AudioFIleUtil {

    suspend fun getOrDownloadAudioFile(context: Context, audioUrl: String): File {
        // Generate a unique file name based on the URL (you can use hashing for better results)
        val fileName = audioUrl.hashCode().toString()
        val cacheFile = File(context.cacheDir, fileName)

        // Check if the file already exists
        if (cacheFile.exists()) {
            return cacheFile
        }

        // File does not exist, download it
        withContext(Dispatchers.IO) {
            URL(audioUrl).openStream().use { input ->
                cacheFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        return cacheFile
    }

    fun extractAmplitudes(context: Context, audioFile: File): List<Int> {
        val amplitudes = mutableListOf<Int>()
        // Create output PCM file
        val pcmFile = File(context.cacheDir, "${audioFile.name}.pcm")
        var startReadingFile = false
        if (pcmFile.exists()) {
            startReadingFile = true
        }else{
            // Construct the FFmpeg command as a single string
            val command =
                "-i ${audioFile.absolutePath} -f s16le -ac 1 -ar 44100 ${pcmFile.absolutePath}"
            // Execute the command
            val result = FFmpegKit.execute(command)
            if (result.returnCode.isValueSuccess) {
                startReadingFile = true
            } else {
                println("FFmpeg Error: ${result.failStackTrace}")
            }
        }
        if(startReadingFile){
            pcmFile.inputStream().buffered().use { input ->
                while (true) {
                    // Read two bytes at a time for 16-bit PCM
                    val low = input.read()
                    val high = input.read()
                    if (low == -1 || high == -1) break

                    // Combine the bytes to form the amplitude value
                    val amplitude = (high shl 8) or (low and 0xFF)
                    amplitudes.add(amplitude)
                }
            }
        }
        return amplitudes
    }

    fun applyWaveSeekbarConfig(waveSeekBar: WaveformSeekBar, amplitudes: IntArray){
        waveSeekBar.apply {
            progress = 0F
            waveWidth = Utils.dp(context, 2)
            waveGap = Utils.dp(context, 1)
            waveMinHeight = Utils.dp(context, 5)
            waveCornerRadius = Utils.dp(context, 2)
            waveGravity = WaveGravity.CENTER
            waveBackgroundColor =
                ContextCompat.getColor(context, R.color.ism_identifier_text_grey)
            waveProgressColor =
                ContextCompat.getColor(context, R.color.ism_blue)
            sample = amplitudes
//                marker = getDummyMarkerSample(ismSentMessageAudioBinding)
            onProgressChanged = object : SeekBarOnProgressChanged {
                override fun onProgressChanged(
                    waveformSeekBar: WaveformSeekBar,
                    progress: Float,
                    fromUser: Boolean
                ) {
//                    if (!fromUser)
//                        waveSeekBar.progress = progress
                }
            }
        }
    }
}