package com.example.notepad

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDate: EditText
    private lateinit var btnSave: MaterialButton

    private var selectedDate: String = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarAddNote)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        etDate = findViewById(R.id.etDate)
        btnSave = findViewById(R.id.btnSave)

        // Set default date
        etDate.setText(selectedDate)

        etDate.setOnClickListener {
            showDatePicker()
        }

        // Check if editing an existing note
        val isEdit = intent.getBooleanExtra("isEdit", false)

        if (isEdit) {
            supportActionBar?.title = getString(R.string.edit_note)
            btnSave.text = getString(R.string.update_note)

            etTitle.setText(intent.getStringExtra("title"))
            etDescription.setText(intent.getStringExtra("description"))
            val noteDate = intent.getStringExtra("date")
            if (noteDate != null) {
                selectedDate = noteDate
                etDate.setText(selectedDate)
            }
        }

        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (title.isEmpty()) {
                etTitle.error = getString(R.string.title_required)
                return@setOnClickListener
            }

            val resultIntent = Intent()
            resultIntent.putExtra("title", title)
            resultIntent.putExtra("description", description)
            resultIntent.putExtra("date", selectedDate)

            // Return the position when editing
            if (isEdit) {
                resultIntent.putExtra("position", intent.getIntExtra("position", -1))
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(selectedCalendar.time)
                etDate.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}