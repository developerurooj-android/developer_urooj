package com.example.studentsApp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsApp.databinding.ItemStudentBinding
import com.example.studentsApp.model.Student

class StudentAdapter(
    private var studentList: List<Student>,
    private val onItemClick: (Student) -> Unit,
    private val onEditClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
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

        holder.binding.root.setOnClickListener { onItemClick(student) }
        holder.binding.btnEdit.setOnClickListener { onEditClick(student) }
        holder.binding.btnDelete.setOnClickListener { onDeleteClick(student) }
    }

    override fun getItemCount(): Int = studentList.size

    fun updateList(newList: List<Student>) {
        studentList = newList
        notifyDataSetChanged()
    }
}
