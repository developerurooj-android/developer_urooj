package com.example.doctorappointmentapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.adapter.ChatAdapter
import com.example.doctorappointmentapp.databinding.ActivityChatBinding
import com.example.doctorappointmentapp.model.ChatMessage
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private val auth by lazy { FirebaseAuth.getInstance() }

    private lateinit var adapter: ChatAdapter
    private var receiverId    = ""
    private var receiverName  = ""
    private var receiverImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiverId    = intent.getStringExtra(Constants.EXTRA_RECEIVER_ID)    ?: ""
        receiverName  = intent.getStringExtra(Constants.EXTRA_RECEIVER_NAME)  ?: "Doctor"
        receiverImage = intent.getStringExtra(Constants.EXTRA_RECEIVER_IMAGE) ?: ""

        binding.tvDoctorName.text = receiverName
        Glide.with(this).load(receiverImage).placeholder(R.drawable.ic_doctor)
            .circleCrop().into(binding.ivDoctorAvatar)

        val currentUid = auth.currentUser?.uid ?: ""
        adapter = ChatAdapter(currentUid)
        binding.rvMessages.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        binding.rvMessages.adapter = adapter

        viewModel.startListening(currentUid, receiverId)
        viewModel.messages.observe(this) { list ->
            adapter.submitList(list) {
                if (list.isNotEmpty()) binding.rvMessages.scrollToPosition(list.size - 1)
            }
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text?.toString()?.trim() ?: ""
            if (text.isEmpty()) return@setOnClickListener
            val msg = ChatMessage(
                senderId   = currentUid,
                receiverId = receiverId,
                message    = text,
                fileType   = "text"
            )
            viewModel.sendMessage(currentUid, receiverId, msg)
            binding.etMessage.setText("")
        }

        binding.btnAudioCall.setOnClickListener {
            startActivity(Intent(this, AudioCallActivity::class.java)
                .putExtra(Constants.EXTRA_RECEIVER_NAME,  receiverName)
                .putExtra(Constants.EXTRA_RECEIVER_IMAGE, receiverImage))
        }

        binding.btnVideoCall.setOnClickListener {
            startActivity(Intent(this, VideoCallActivity::class.java)
                .putExtra(Constants.EXTRA_RECEIVER_NAME,  receiverName)
                .putExtra(Constants.EXTRA_RECEIVER_IMAGE, receiverImage))
        }
    }
}
