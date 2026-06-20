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

    class DropDTuning : GuitarTune(
        "Drop D Guitar Tuning (D#)",
        listOf(
            UiNote(
                note = Note.D,
                frequency = 73.42f,
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

    class DropCTuning : GuitarTune(
        "Drop C Guitar Tuning (C#)",
        listOf(
            UiNote(
                note = Note.C,
                frequency = 65.41f,
                octave = 2,
            ),
            UiNote(
                note = Note.G,
                frequency = 98f,
                octave = 2,
            ),
            UiNote(
                note = Note.C,
                frequency = 130.81f,
                octave = 3,
            ),
            UiNote(
                note = Note.F,
                frequency = 174.61f,
                octave = 3,
            ),
            UiNote(
                note = Note.A,
                frequency = 220f,
                octave = 3,
            ),
            UiNote(
                note = Note.D,
                frequency = 293.66f,
                octave = 4,
            )
        )
    )
}