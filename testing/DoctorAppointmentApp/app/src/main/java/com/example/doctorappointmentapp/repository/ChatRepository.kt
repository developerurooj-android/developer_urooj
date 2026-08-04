package com.example.doctorappointmentapp.repository

import com.example.doctorappointmentapp.model.ChatMessage
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ChatRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /** Deterministic chat-room ID — same result regardless of who calls first. */
    fun chatRoomId(uid1: String, uid2: String): String =
        listOf(uid1, uid2).sorted().joinToString("_")

    // ── Messaging ─────────────────────────────────────────────────

    suspend fun sendMessage(senderId: String, receiverId: String, msg: ChatMessage): Resource<Unit> = try {
        val roomId = chatRoomId(senderId, receiverId)
        db.collection(Constants.CHATS_COLLECTION)
            .document(roomId)
            .collection(Constants.MESSAGES_SUBCOLLECTION)
            .add(msg).await()
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to send message")
    }

    /**
     * Real-time listener — fires every time a new message is added.
     * Returns [ListenerRegistration] that must be removed in onDestroy/onCleared.
     */
    fun listenForMessages(
        senderId: String,
        receiverId: String,
        onMessages: (List<ChatMessage>) -> Unit
    ): ListenerRegistration {
        val roomId = chatRoomId(senderId, receiverId)
        return db.collection(Constants.CHATS_COLLECTION)
            .document(roomId)
            .collection(Constants.MESSAGES_SUBCOLLECTION)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, error ->
                if (error != null) return@addSnapshotListener
                val list = snap?.documents?.mapNotNull {
                    it.toObject(ChatMessage::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onMessages(list)
            }
    }

    // ── Read receipts ─────────────────────────────────────────────

    suspend fun markMessagesRead(
        senderId: String,
        receiverId: String,
        currentUserId: String
    ): Resource<Unit> = try {
        val roomId = chatRoomId(senderId, receiverId)
        val snap = db.collection(Constants.CHATS_COLLECTION)
            .document(roomId)
            .collection(Constants.MESSAGES_SUBCOLLECTION)
            .whereEqualTo("receiverId", currentUserId)
            .whereEqualTo("isRead", false)
            .get().await()
        val batch = db.batch()
        snap.documents.forEach { batch.update(it.reference, "isRead", true) }
        batch.commit().await()
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Failed to mark read")
    }

    // ── Typing indicator ──────────────────────────────────────────

    /** Write/clear a typing flag to Firestore under the chat room document. */
    fun setTyping(senderId: String, receiverId: String, isTyping: Boolean) {
        val roomId = chatRoomId(senderId, receiverId)
        db.collection(Constants.CHATS_COLLECTION)
            .document(roomId)
            .update("typing_$senderId", isTyping)
            .addOnFailureListener {
                // Room may not exist yet — create it
                db.collection(Constants.CHATS_COLLECTION)
                    .document(roomId)
                    .set(mapOf("typing_$senderId" to isTyping))
            }
    }

    fun listenTyping(
        senderId: String,
        receiverId: String,
        watchUserId: String,
        onTyping: (Boolean) -> Unit
    ): ListenerRegistration {
        val roomId = chatRoomId(senderId, receiverId)
        return db.collection(Constants.CHATS_COLLECTION)
            .document(roomId)
            .addSnapshotListener { snap, _ ->
                val typing = snap?.getBoolean("typing_$watchUserId") ?: false
                onTyping(typing)
            }
    }
}
