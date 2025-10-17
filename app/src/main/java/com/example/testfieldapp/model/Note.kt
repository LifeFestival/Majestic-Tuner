package com.example.testfieldapp.model

enum class Note(
    val symbol: String,
    val noteIndex: Int,
    val musicName: String
) {
    C("C", 0, "Do"),
    CSharp("C#", 1, "Do#"),
    D("D", 2, "Re"),
    DSharp("D#", 3, "Re#"),
    E("E", 4, "Mi"),
    F("F", 5, "Fa"),
    FSharp("F#", 6, "Fa#"),
    G("G", 7, "Sol"),
    GSharp("G#", 8, "Sol#"),
    A("A", 9, "La"),
    ASharp("A#", 10, "La#"),
    B("B", 11, "Si"),

    None("-", -1, "-")
}