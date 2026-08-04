package com.example.doctorappointmentapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctorappointmentapp.model.ChatMessage
import com.example.doctorappointmentapp.repository.ChatRepository
import com.example.doctorappointmentapp.utils.Resource
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class ChatViewModel(private val repo: ChatRepository = ChatRepository()) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _sendState = MutableLiveData<Resource<Unit>>()
    val sendState: LiveData<Resource<Unit>> = _sendState

    private var listener: ListenerRegistration? = null

    fun startListening(senderId: String, receiverId: String) {
        listener?.remove()
        listener = repo.listenForMessages(senderId, receiverId) { list ->
            _messages.postValue(list)
        }
    }

    fun sendMessage(senderId: String, receiverId: String, msg: ChatMessage) {
        viewModelScope.launch {
            _sendState.value = repo.sendMessage(senderId, receiverId, msg)
        }
    }

    fun markRead(senderId: String, receiverId: String, currentUserId: String) {
        viewModelScope.launch { repo.markMessagesRead(senderId, receiverId, currentUserId) }
    }

    override fun onCleared() {
        listener?.remove()
        super.onCleared()
    }
}
