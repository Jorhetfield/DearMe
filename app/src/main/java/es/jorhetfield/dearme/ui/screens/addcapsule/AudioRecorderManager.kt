package es.jorhetfield.dearme.ui.screens.addcapsule

import android.content.Context
import android.media.MediaRecorder
import java.io.File

class AudioRecorderManager(private val context: Context) {
    private var recorder: MediaRecorder? = null
    private var visualizer: AudioVisualizerManager? = null

    fun startRecording(): Pair<File, AudioVisualizerManager> {
        val dir = File(context.cacheDir, "audio").apply { mkdirs() }
        val file = File(dir, "recording_${System.currentTimeMillis()}.m4a")
        recorder = MediaRecorder(context).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(96_000)   // 96 kbps
            setAudioSamplingRate(44_100)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }
        visualizer = AudioVisualizerManager(recorder)
        return file to visualizer!!
    }

    fun captureAmplitude() {
        visualizer?.captureAmplitude()
    }

    fun stopRecording(): List<Float> {
        recorder?.apply { stop(); release() }
        recorder = null
        val amplitudes = visualizer?.getAmplitudes() ?: emptyList()
        visualizer?.reset()
        return amplitudes
    }

    fun release() {
        recorder?.release()
        recorder = null
        visualizer?.reset()
    }
}
