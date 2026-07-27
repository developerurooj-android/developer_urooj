package com.example.notepad

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var btnAddNote: FloatingActionButton
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var notes: ArrayList<Note>

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val title = data?.getStringExtra("title")
                val description = data?.getStringExtra("description")
                val date = data?.getStringExtra("date")
                val position = data?.getIntExtra("position", -1) ?: -1

                if (title != null && description != null && date != null) {
                    if (position != -1) {
                        // Editing existing note
                        val note = notes[position]
                        note.title = title
                        note.description = description
                        note.date = date
                        databaseHelper.updateNote(note)
                        adapter.notifyItemChanged(position)
                    } else {
                        // Adding new note
                        val newNote = Note(title = title, description = description, date = date)
                        val id = databaseHelper.insertNote(newNote)
                        newNote.id = id.toInt()
                        notes.add(newNote)
                        adapter.notifyItemInserted(notes.size - 1)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        notes = databaseHelper.getAllNotes()

        recyclerView = findViewById(R.id.rvNotes)
        btnAddNote = findViewById(R.id.btnAddNote)

        adapter = NoteAdapter(
            notes,
            onNoteClick = { note, position ->
                val itemView = recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                if (itemView != null) {
                    showPopupMenu(itemView, note, position)
                }
            },
            onNoteLongClick = { _, position ->
                showDeleteDialog(position)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            launcher.launch(intent)
        }
    }

    private fun showPopupMenu(view: View, note: Note, position: Int) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.note_options_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    val intent = Intent(this, AddNoteActivity::class.java)
                    intent.putExtra("isEdit", true)
                    intent.putExtra("title", note.title)
                    intent.putExtra("description", note.description)
                    intent.putExtra("date", note.date)
                    intent.putExtra("position", position)
                    launcher.launch(intent)
                    true
                }
                R.id.action_delete -> {
                    showDeleteDialog(position)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Note")
        builder.setMessage("Are you sure you want to delete this note?")
        builder.setPositiveButton("Delete") { _, _ ->
            val note = notes[position]
            databaseHelper.deleteNote(note.id)
            notes.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyItemRangeChanged(position, notes.size)
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
