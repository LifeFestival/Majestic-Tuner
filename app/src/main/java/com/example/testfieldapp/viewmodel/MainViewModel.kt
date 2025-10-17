package com.example.testfieldapp.viewmodel

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testfieldapp.model.GuitarTune
import com.example.testfieldapp.model.Note
import com.example.testfieldapp.model.UiNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jtransforms.fft.DoubleFFT_1D
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class MainViewModel : ViewModel() {
    companion object {
        private const val SAMPLING_RATE: Int = 44100
        private const val CHANNEL_CONFIG: Int = AudioFormat.CHANNEL_IN_MONO
        private const val AUDIO_FORMAT: Int = AudioFormat.ENCODING_PCM_16BIT
        private const val BUFFER_SIZE_FACTOR: Int = 4
        private const val OCTAVES_COUNT: Int = 12

        //Reference note in this case is A4
        private const val REF_NOTE_FREQUENCY = 440f
        private const val REF_NOTE_INDEX = 57
    }

    private val bufferSize = AudioRecord.getMinBufferSize(
        SAMPLING_RATE,
        CHANNEL_CONFIG,
        AUDIO_FORMAT
    ) * BUFFER_SIZE_FACTOR

    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    private val _currentNote = MutableStateFlow<UiNote?>(null)
    val currentNote = _currentNote.asStateFlow()

    private val _currentTune = MutableStateFlow<GuitarTune>(GuitarTune.StandardTuning())
    val currentTune = _currentTune.asStateFlow()

    private val _selectedNote = MutableStateFlow(_currentTune.value.notes.first())
    val selectedNote = _selectedNote.asStateFlow()

    private var recordingJob: Job? = null

    private var recorder: AudioRecord? = null

    @SuppressLint("MissingPermission")
    private fun createRecorder() {
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLING_RATE,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            bufferSize
        )
    }

    fun changeSelectedNote(note: UiNote) {
        _selectedNote.value = note
    }

    fun startRecording() {
        _isRecording.value = true

        if (recorder == null) createRecorder()

        recordingJob?.cancel()

        recordingJob = viewModelScope.launch(Dispatchers.IO) {
            recorder?.startRecording()

            val audioData = ShortArray(bufferSize)

            while (_isRecording.value) {
                recorder?.read(audioData, 0, bufferSize)

                if (audioData.isEmpty()) continue

                calculateFrequency(audioData)
            }

            _currentNote.value = null
        }
    }

    fun stopRecording() {
        _isRecording.value = false

        recorder?.stop()
        recorder?.release()

        recorder = null

        recordingJob?.cancel()
    }

    private fun calculateFrequency(audioData: ShortArray) {
        //Создаем срез аудио данных
        val actualAudioData: ShortArray = applyWindowing(audioData)

        //Находим рутовую ноту
        val frequency = findFundamentalFrequency(actualAudioData)

        //Создаем UI модель
        val note = frequencyToNote(frequency)

        _currentNote.value = note
    }

    private fun applyWindowing(audioData: ShortArray): ShortArray {
        return ShortArray(audioData.size) { i ->
            val window = 0.5 * (1 - cos(2 * Math.PI * i / (audioData.size - 1)))
            (audioData[i] * window).toInt().toShort()
        }
    }

    private fun findFundamentalFrequency(samples: ShortArray): Float {
        val autocorr = DoubleArray(samples.size / 2)

        for (lag in autocorr.indices) {
            autocorr[lag] = (0 until samples.size - lag).sumOf {
                samples[it].toDouble() * samples[it + lag]
            }
        }

        val minLag = SAMPLING_RATE / 500 // 500Hz upper limit
        val maxLag = SAMPLING_RATE / 50  // 50Hz lower limit

        var maxLagIndex = minLag
        for (i in minLag until maxLag) {
            if (autocorr[i] > autocorr[maxLagIndex]) {
                maxLagIndex = i
            }
        }

        return SAMPLING_RATE.toFloat() / maxLagIndex
    }

    private fun findPitchCepstrum(samples: ShortArray): Float {
        val fft = DoubleFFT_1D(samples.size.toLong())
        val fftInput = DoubleArray(samples.size * 2).apply {
            samples.forEachIndexed { i, sample ->
                this[2 * i] = sample.toDouble()
                this[2 * i + 1] = 0.0
            }
        }

        fft.complexForward(fftInput)

        val logSpectrum = DoubleArray(samples.size / 2) { i ->
            val re = fftInput[2 * i]
            val im = fftInput[2 * i + 1]
            ln(sqrt(re * re + im * im))
        }

        val cepstrum = DoubleArray(samples.size * 2).apply {
            System.arraycopy(logSpectrum, 0, this, 0, logSpectrum.size)
            // Mirror for real IFFT
            for (i in logSpectrum.size until samples.size) {
                this[2 * i] = logSpectrum[samples.size - i - 1]
                this[2 * i + 1] = 0.0
            }
        }
        fft.complexInverse(cepstrum, true)

        // 4. Find peak in guitar range (50-500Hz)
        val minLag = SAMPLING_RATE / 500
        val maxLag = SAMPLING_RATE / 50
        var peakLag = minLag
        for (i in minLag..maxLag) {
            if (cepstrum[2 * i] > cepstrum[2 * peakLag]) {
                peakLag = i
            }
        }

        return SAMPLING_RATE.toFloat() / peakLag
    }

    private fun frequencyToNote(frequency: Float): UiNote {
        Log.d("RAW INPUT", "Fequency - $frequency")

        if (frequency <= 0) {
            return UiNote(
                Note.None,
                -1f,
                -1,
            )
        }

        val semitones = (OCTAVES_COUNT * log2(frequency / REF_NOTE_FREQUENCY)).roundToInt()
        val octave = (semitones + REF_NOTE_INDEX) / OCTAVES_COUNT
        val noteIndex =
            ((semitones + 9) % OCTAVES_COUNT).let { if (it < 0) it + OCTAVES_COUNT else it }
        val exactFrequency = REF_NOTE_FREQUENCY * 2f.pow((semitones) / OCTAVES_COUNT)

        val note: Note = Note.entries.firstOrNull { it.noteIndex == noteIndex } ?: Note.None

        Log.d(
            "NOTE",
            "Semitones - $semitones, index - $noteIndex, freq - $exactFrequency, octave - $octave, note - ${note.symbol}"
        )

        return UiNote(
            note,
            exactFrequency,
            octave,
        )
    }

    private fun log2(x: Float) = (ln(x) / ln(2f))
}