package com.example.notepad

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes_db"
        private const val DATABASE_VERSION = 2   // Increased version

        private const val TABLE_NAME = "notes"

        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = """
            CREATE TABLE $TABLE_NAME(
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_DATE TEXT
            )
        """.trimIndent()

        db?.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert Note
    fun insertNote(note: Note): Long {

        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_DESCRIPTION, note.description)
            put(COLUMN_DATE, note.date)
        }

        val id = db.insert(TABLE_NAME, null, values)

        db.close()

        return id
    }

    // Get All Notes
    fun getAllNotes(): ArrayList<Note> {

        val notesList = ArrayList<Note>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                val note = Note(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                )

                notesList.add(note)

            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()

        return notesList
    }

    // Update Note
    fun updateNote(note: Note): Int {

        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_DESCRIPTION, note.description)
            put(COLUMN_DATE, note.date)
        }

        val result = db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID=?",
            arrayOf(note.id.toString())
        )

        db.close()

        return result
    }

    // Delete Note
    fun deleteNote(id: Int): Int {

        val db = writableDatabase

        val result = db.delete(
            TABLE_NAME,
            "$COLUMN_ID=?",
            arrayOf(id.toString())
        )

        db.close()

        return result
    }
}