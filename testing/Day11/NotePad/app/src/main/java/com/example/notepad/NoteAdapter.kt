package com.example.notepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val notes: ArrayList<Note>,
    private val onNoteClick: (Note, Int) -> Unit,
    private val onNoteLongClick: (Note, Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvNoteTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvNoteDescription)
        val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.tvTitle.text = note.title
        holder.tvDescription.text = note.description
        holder.tvDate.text = note.date

        holder.itemView.setOnClickListener {
            onNoteClick(note, position)
        }

        holder.itemView.setOnLongClickListener {
            onNoteLongClick(note, position)
            true
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}