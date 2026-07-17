package com.example.testfieldapp.detector

import kotlin.math.abs

object PitchDetector {
    private const val MIN_FREQUENCY = 60.0
    private const val MAX_FREQUENCY = 1000.0

    private const val YIN_THRESHOLD = 0.2

    //Возвращаем частоту, либо null если не смогли её определить
    fun detectPitch(audioData: ShortArray, sampleRate: Int): Float? {
        val bufferSize = audioData.size
        val halfBufferSize = bufferSize / 2

        //Магия
        val yinBuffer = DoubleArray(halfBufferSize)
        for (tau in 1 until halfBufferSize) {
            var sum = 0.0
            for (j in 0 until halfBufferSize) {
                val delta = audioData[j] - audioData[j + tau]
                sum += (delta * delta).toDouble()
            }
            yinBuffer[tau] = sum
        }

        //Ещё магия
        yinBuffer[0] = 1.0
        var runningSum = 0.0
        for (tau in 1 until halfBufferSize) {
            runningSum += yinBuffer[tau]
            yinBuffer[tau] = if (runningSum == 0.0) {
                1.0
            } else {
                yinBuffer[tau] * tau / runningSum
            }
        }

        //Ещё чуть чуть магии
        var tauEstimate = -1
        val minTau = (sampleRate / MAX_FREQUENCY).toInt().coerceAtLeast(1)
        val maxTau = (sampleRate / MIN_FREQUENCY).toInt().coerceAtMost(halfBufferSize - 1)

        var tau = minTau
        while (tau <= maxTau) {
            if (yinBuffer[tau] < YIN_THRESHOLD) {
                while (tau + 1 <= maxTau && yinBuffer[tau + 1] < yinBuffer[tau]) {
                    tau++
                }
                tauEstimate = tau
                break
            }
            tau++
        }

        //Не нашли частоту
        if (tauEstimate == -1) return null

        //Паробалическая интерполяция (Господи помилуй)
        val betterTau = parabolicInterpolation(yinBuffer, tauEstimate, halfBufferSize)

        if (betterTau <= 0.0) return null

        return (sampleRate / betterTau).toFloat()
    }

    private fun parabolicInterpolation(
        yinBuffer: DoubleArray,
        tauEstimate: Int,
        halfBufferSize: Int
    ): Double {
        val x0 = if (tauEstimate < 1) tauEstimate else tauEstimate - 1
        val x2 = if (tauEstimate + 1 < halfBufferSize) tauEstimate + 1 else tauEstimate

        if (x0 == tauEstimate) {
            return if (yinBuffer[tauEstimate] <= yinBuffer[x2]) {
                tauEstimate.toDouble()
            } else {
                x2.toDouble()
            }
        }
        if (x2 == tauEstimate) {
            return if (yinBuffer[tauEstimate] <= yinBuffer[x0]) {
                tauEstimate.toDouble()
            } else {
                x0.toDouble()
            }
        }

        val s0 = yinBuffer[x0]
        val s1 = yinBuffer[tauEstimate]
        val s2 = yinBuffer[x2]
        val adjustment = (s2 - s0) / (2.0 * (2.0 * s1 - s2 - s0))

        return if (abs(adjustment) < 1) {
            tauEstimate + adjustment
        } else {
            tauEstimate.toDouble()
        }
    }
}