package com.example.doctorappointmentapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ai.Chat
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var db:FirebaseFirestore

        lateinit var adapter:ChatAdapter

        val chatList=ArrayList<Chat>()

        db= FirebaseFirestore.getInstance()

        db.collection("Chats")
            .get()
        }
    }
