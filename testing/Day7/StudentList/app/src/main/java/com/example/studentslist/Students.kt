package com.example.studentslist

object Students {

    data class Student(
        val rollNo: Int,
        val name: String
    )

    val students = arrayListOf(
        Student(1, "Ali"),
        Student(2, "Ahmed"),
        Student(3, "Fatima"),
        Student(4, "Ayesha"),
        Student(5, "Hassan"),
        Student(6, "Zain"),
        Student(7, "Usman"),
        Student(8, "Sara"),
        Student(9, "Maryam"),
        Student(10, "Bilal")
    )
}