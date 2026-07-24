package com.example.studentsApp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studentsApp.databinding.FragmentStudentDetailsBinding
import com.example.studentsApp.model.Student

class StudentDetailsFragment : Fragment() {

    private var _binding: FragmentStudentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDetailsBinding.inflate(inflater, container, false)

        val student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("student", Student::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable("student") as? Student
        }

        student?.let {
            binding.tvName.text = it.name
            binding.tvRollNo.text = it.rollNo
            binding.tvDepartmentDetail.text = it.department
            binding.tvSemesterDetail.text = it.semester
            binding.tvEmailDetail.text = it.email
            binding.tvPhoneDetail.text = it.phone
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
