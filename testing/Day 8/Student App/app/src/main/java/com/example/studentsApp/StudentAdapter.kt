package com.example.studentsApp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsApp.databinding.ItemStudentBinding
import com.example.studentsApp.model.Student

class StudentAdapter(
    private val studentList: List<Student>
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentViewHolder {

        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int
    ) {

        val student = studentList[position]

        holder.binding.tvName.text = student.name
        holder.binding.tvRollNo.text = student.rollNo
        holder.binding.tvDepartment.text = student.department
    }

    override fun getItemCount(): Int {

        return studentList.size
    }
}