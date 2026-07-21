package com.example.studentslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val studentList: List<Students.Student>
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName: TextView = itemView.findViewById(R.id.tvname)
        val tvRollNo: TextView = itemView.findViewById(R.id.tvRollno)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)

        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {

        val student = studentList[position]

        holder.tvName.text = student.name
        holder.tvRollNo.text = student.rollNo.toString()
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}