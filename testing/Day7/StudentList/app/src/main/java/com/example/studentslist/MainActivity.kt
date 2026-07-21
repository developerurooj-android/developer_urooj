package com.example.studentslist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentslist.R.id

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var studentAdapter: StudentAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(id.recyclerView)
        val students = Students.students
        studentAdapter = StudentAdapter(students)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = studentAdapter
    }
}