package com.example.studentsApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentsApp.databinding.FragmentStudentListBinding
import com.example.studentsApp.model.Student

class StudentListFragment : Fragment() {

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStudentListBinding.inflate(inflater, container, false)

        val studentList = arrayListOf(
            Student("Ali", "101", "BSCS"),
            Student("Ahmed", "102", "BSIT"),
            Student("Sara", "103", "BSE"),
            Student("Ayesha", "104", "BBA"),
            Student("Usman", "105", "BSSE")
        )

        val adapter = StudentAdapter(studentList)

        binding.recyclerViewStudents.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerViewStudents.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}