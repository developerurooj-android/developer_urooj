package com.example.doctorappointmentapp.model

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val imageUrl: String = "",
    val fileUrl: String = "",
    val fileType: String = "",   // "image" | "pdf" | "text"
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
