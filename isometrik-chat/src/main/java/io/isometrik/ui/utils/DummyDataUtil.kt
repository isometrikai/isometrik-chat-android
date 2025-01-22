package io.isometrik.ui.utils

import io.isometrik.chat.databinding.IsmSentMessageAudioBinding
import java.util.HashMap
import java.util.Random

object DummyDataUtil {

    fun getDummyWaveSample(): IntArray {
        val data = IntArray(50)
        for (i in data.indices)
            data[i] = Random().nextInt(data.size)

        return data
    }

    private fun getDummyMarkerSample(binding: IsmSentMessageAudioBinding): HashMap<Float, String> {
        val map = hashMapOf<Float, String>()
        map[binding.waveSeekBar.maxProgress / 2] = "Middle"
        map[binding.waveSeekBar.maxProgress / 4] = "Quarter"
        return map
    }

}