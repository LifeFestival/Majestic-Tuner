package com.example.testfieldapp.model

sealed class GuitarTune(
    val name: String,
    val notes: List<UiNote>
) {
    class StandardTuning : GuitarTune(
        "Standard Guitar Tuning (Mi)",
        listOf(
            UiNote(
                note = Note.E,
                frequency = 82.41f,
                octave = 2,
            ),
            UiNote(
                note = Note.A,
                frequency = 110f,
                octave = 2,
            ),
            UiNote(
                note = Note.D,
                frequency = 146.83f,
                octave = 3,
            ),
            UiNote(
                note = Note.G,
                frequency = 196f,
                octave = 3,
            ),
            UiNote(
                note = Note.B,
                frequency = 246.94f,
                octave = 3,
            ),
            UiNote(
                note = Note.E,
                frequency = 329.63f,
                octave = 4,
            )
        )
    )
}