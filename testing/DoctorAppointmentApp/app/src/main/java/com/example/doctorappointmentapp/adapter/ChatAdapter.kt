package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.databinding.ItemMessageReceivedBinding
import com.example.doctorappointmentapp.databinding.ItemMessageSentBinding
import com.example.doctorappointmentapp.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(private val currentUserId: String) :
    ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DIFF) {

    companion object {
        private const val VIEW_SENT     = 1
        private const val VIEW_RECEIVED = 2
        val DIFF = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(a: ChatMessage, b: ChatMessage) = a.id == b.id
            override fun areContentsTheSame(a: ChatMessage, b: ChatMessage) = a == b
        }
    }

    override fun getItemViewType(position: Int) =
        if (getItem(position).senderId == currentUserId) VIEW_SENT else VIEW_RECEIVED

    inner class SentVH(private val b: ItemMessageSentBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: ChatMessage) {
            b.tvMessage.text = m.message
            b.tvTime.text    = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(m.timestamp))
            b.ivRead.setImageResource(
                if (m.isRead) com.example.doctorappointmentapp.R.drawable.ic_double_check
                else          com.example.doctorappointmentapp.R.drawable.ic_check
            )
        }
    }

    inner class ReceivedVH(private val b: ItemMessageReceivedBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(m: ChatMessage) {
            b.tvMessage.text = m.message
            b.tvTime.text    = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(m.timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_SENT)
            SentVH(ItemMessageSentBinding.inflate(inflater, parent, false))
        else
            ReceivedVH(ItemMessageReceivedBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = getItem(position)
        when (holder) {
            is SentVH     -> holder.bind(msg)
            is ReceivedVH -> holder.bind(msg)
        }
    }
}
