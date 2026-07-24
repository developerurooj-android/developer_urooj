package com.example.studentsApp

import com.example.studentsApp.model.Student

object StudentRepository {
    private val students = mutableListOf<Student>()
    private var nextId = 1

    init {
        // Sample data
        addStudent("Ali", "101", "BSCS", "5th", "ali@example.com", "1234567890")
        addStudent("Ahmed", "102", "BSIT", "3rd", "ahmed@example.com", "0987654321")
        addStudent("Sara", "103", "BSE", "7th", "sara@example.com", "1122334455")
    }

    fun getStudents(): List<Student> = students

    fun addStudent(name: String, rollNo: String, department: String, semester: String, email: String, phone: String) {
        students.add(Student(nextId++, name, rollNo, department, semester, email, phone))
    }

    fun updateStudent(id: Int, name: String, rollNo: String, department: String, semester: String, email: String, phone: String) {
        val index = students.indexOfFirst { it.id == id }
        if (index != -1) {
            students[index] = Student(id, name, rollNo, department, semester, email, phone)
        }
    }

    fun deleteStudent(id: Int) {
        students.removeAll { it.id == id }
    }
}
