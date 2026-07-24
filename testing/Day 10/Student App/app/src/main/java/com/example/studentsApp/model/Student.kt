package com.example.studentsApp.model

import java.io.Serializable

data class Student(
    var id: Int,
    var name: String,
    var rollNo: String,
    var department: String,
    var semester: String,
    var email: String,
    var phone: String
) : Serializable
