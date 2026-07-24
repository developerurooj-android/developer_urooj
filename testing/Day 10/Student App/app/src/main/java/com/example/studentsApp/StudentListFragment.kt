package com.example.studentsApp

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentsApp.databinding.DialogStudentBinding
import com.example.studentsApp.databinding.FragmentStudentListBinding
import com.example.studentsApp.model.Student

class StudentListFragment : Fragment() {

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()
        setupMenu()
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter(
            studentList = StudentRepository.getStudents(),
            onItemClick = { student ->
                val bundle = Bundle().apply {
                    putSerializable("student", student)
                }
                findNavController().navigate(R.id.studentDetailsFragment, bundle)
            },
            onEditClick = { student ->
                showStudentDialog(student)
            },
            onDeleteClick = { student ->
                showDeleteConfirmation(student)
            }
        )

        binding.recyclerViewStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewStudents.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAddStudent.setOnClickListener {
            showStudentDialog()
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_add -> {
                        showStudentDialog()
                        true
                    }
                    R.id.menu_about -> {
                        findNavController().navigate(R.id.aboutFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showStudentDialog(student: Student? = null) {
        val dialogBinding = DialogStudentBinding.inflate(layoutInflater)
        val isEdit = student != null

        if (isEdit) {
            student?.let {
                dialogBinding.etName.setText(it.name)
                dialogBinding.etRollNo.setText(it.rollNo)
                dialogBinding.etDepartment.setText(it.department)
                dialogBinding.etSemester.setText(it.semester)
                dialogBinding.etEmail.setText(it.email)
                dialogBinding.etPhone.setText(it.phone)
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (isEdit) "Edit Student" else "Add Student")
            .setView(dialogBinding.root)
            .setPositiveButton(if (isEdit) "Update" else "Add", null)
            .setNegativeButton("Cancel", null)
            .create()
            .apply {
                setOnShowListener { dialog ->
                    val button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    button.setOnClickListener {
                        val name = dialogBinding.etName.text.toString()
                        val rollNo = dialogBinding.etRollNo.text.toString()
                        val dept = dialogBinding.etDepartment.text.toString()
                        val sem = dialogBinding.etSemester.text.toString()
                        val email = dialogBinding.etEmail.text.toString()
                        val phone = dialogBinding.etPhone.text.toString()

                        if (validateFields(dialogBinding)) {
                            val name = dialogBinding.etName.text.toString()
                            val rollNo = dialogBinding.etRollNo.text.toString()
                            val dept = dialogBinding.etDepartment.text.toString()
                            val sem = dialogBinding.etSemester.text.toString()
                            val email = dialogBinding.etEmail.text.toString()
                            val phone = dialogBinding.etPhone.text.toString()

                            if (isEdit) {
                                StudentRepository.updateStudent(student.id, name, rollNo, dept, sem, email, phone)
                            } else {
                                StudentRepository.addStudent(name, rollNo, dept, sem, email, phone)
                            }
                            adapter.updateList(StudentRepository.getStudents())
                            dialog.dismiss()
                        }
                    }
                }
            }
            .show()
    }

    private fun validateFields(binding: DialogStudentBinding): Boolean {
        var isValid = true
        if (binding.etName.text.toString().isBlank()) {
            binding.etName.error = "Name is required"
            isValid = false
        }
        if (binding.etRollNo.text.toString().isBlank()) {
            binding.etRollNo.error = "Roll No is required"
            isValid = false
        }
        if (binding.etDepartment.text.toString().isBlank()) {
            binding.etDepartment.error = "Department is required"
            isValid = false
        }
        if (binding.etSemester.text.toString().isBlank()) {
            binding.etSemester.error = "Semester is required"
            isValid = false
        }
        return isValid
    }

    private fun showDeleteConfirmation(student: Student) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.name}?")
            .setPositiveButton("Delete") { _, _ ->
                StudentRepository.deleteStudent(student.id)
                adapter.updateList(StudentRepository.getStudents())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
