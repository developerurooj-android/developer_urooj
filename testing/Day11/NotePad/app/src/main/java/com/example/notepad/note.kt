package com.example.notepad

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Note(
    var id: Int = 0,
    var title: String,
    var description: String,
    var date: String = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
): Serializable