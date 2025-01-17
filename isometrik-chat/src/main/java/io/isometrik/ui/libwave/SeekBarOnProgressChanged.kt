package io.isometrik.ui.libwave

interface SeekBarOnProgressChanged {
    fun onProgressChanged(waveformSeekBar: WaveformSeekBar, progress: Float, fromUser: Boolean)
}