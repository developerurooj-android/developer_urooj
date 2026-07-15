//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    //Display, Update, Search
    val studentsArray = arrayOf(
        Student("Ali", "Khan", 20, "Lahore"),
        Student("Ahmed", "Raza", 21, "Karachi"),
        Student("Fatima", "Noor", 19, "Islamabad"),
        Student("Urooj", "Khadim", 22, "Rawalpindi"),
        Student("Sara", "Iqbal", 20, "Faisalabad")
    )
/// Display add remove update search
    val studentsList = arrayListOf(
        Student("Ali", "Khan", 20, "Lahore"),
        Student("Ahmed", "Raza", 21, "Karachi"),
        Student("Fatima", "Khadim", 19, "Islamabad"),
        Student("Urooj", "Khadim", 22, "Rawalpindi"),
        Student("Sara", "Iqbal", 20, "Faisalabad")
    )
    //Display add remove update search use key value
    val studentsMap = hashMapOf(
        1 to Student("Ali", "Khan", 20, "Lahore"),
        2 to Student("Ahmed", "Raza", 21, "Karachi"),
        3 to Student("Fatima", "Noor", 19, "Islamabad"),
        4 to Student("Urooj", "Khadim", 22, "Rawalpindi"),
        5 to Student("Sara", "Iqbal", 20, "Faisalabad")
    )
    var choice: Int

    do {

        println("\n========== Select Data Structure ==========")
        println("1. Array")
        println("2. ArrayList")
        println("3. HashMap")
        println("4. Exit")
        print("Enter your choice: ")

        choice = readln().toInt()

        when (choice) {

            // ================= ARRAY(only display update and search using index) =================

            1 -> {

                var choice: Int

                do {

                    println("\n===== ARRAY MENU =====")
                    println("1. Display")
                    println("2. Update")
                    println("3. Search")
                    println("4. Back")

                    print("Enter your choice: ")
                    choice = readln().toInt()

                    when (choice) {

                        1 -> {
                            for (student in studentsArray) {
                                println("${student.firstname} ${student.lastname} ${student.age} ${student.address}")
                            }
                        }

                        2 -> {
                            println("Enter Index (0-4):")
                            val index = readln().toInt()

                            println("Enter First Name:")
                            studentsArray[index].firstname = readln()

                            println("Enter Last Name:")
                            studentsArray[index].lastname = readln()

                            println("Enter Age:")
                            studentsArray[index].age = readln().toInt()

                            println("Enter Address:")
                            studentsArray[index].address = readln()

                            println("Student Updated Successfully")
                        }

                        4 -> println("Returning...")

                        else -> println("Invalid Choice")

                    }


                } while (choice != 4)
            }

            // ================= ARRAYLIST (add remove update and search using index) =================

            2 -> {

                var choice: Int

                do {

                    println("\n===== ARRAYLIST MENU =====")
                    println("1. Display")
                    println("2. Add")
                    println("3. Remove")
                    println("4. Update")
                    println("5. Search")
                    println("6. Back")

                    print("Enter your choice: ")
                    choice = readln().toInt()

                    when (choice) {

                        1 -> {

                            for (student in studentsList) {

                                println("${student.firstname} ${student.lastname} ${student.age} ${student.address}")
                            }
                        }

                        2 -> {

                            println("Enter First Name:")
                            val first = readln()

                            println("Enter Last Name:")
                            val last = readln()

                            println("Enter Age:")
                            val age = readln().toInt()

                            println("Enter Address:")
                            val address = readln()

                            studentsList.add(Student(first, last, age, address))

                            println("Student Added Successfully")
                        }

                        3 -> {

                            println("Enter Index:")
                            val index = readln().toInt()

                            studentsList.removeAt(index)

                            println("Student Removed Successfully")
                        }

                        4 -> {

                            println("Enter Index:")
                            val index = readln().toInt()

                            println("Enter First Name:")
                            studentsList[index].firstname = readln()

                            println("Enter Last Name:")
                            studentsList[index].lastname = readln()

                            println("Enter Age:")
                            studentsList[index].age = readln().toInt()

                            println("Enter Address:")
                            studentsList[index].address = readln()

                            println("Student Updated Successfully")
                        }

                        5 -> {

                            println("Enter First Name:")
                            val name = readln()

                            var found = false

                            for (student in studentsList) {

                                if (student.firstname.equals(name, true)) {

                                    println("Student Found")
                                    println(student.firstname)
                                    println(student.lastname)
                                    println(student.age)
                                    println(student.address)

                                    found = true
                                }
                            }

                            if (!found)
                                println("Student Not Found")
                        }

                        6 -> println("Returning...")

                        else -> println("Invalid Choice")
                    }

                } while (choice != 6)
            }

            // ================= HASHMAP(add remove update search using key) =================
            3 -> {

                var choice: Int

                do {

                    println("\n===== HASHMAP MENU =====")
                    println("1. Display")
                    println("2. Add")
                    println("3. Remove")
                    println("4. Update")
                    println("5. Search")
                    println("6. Back")

                    print("Enter your choice: ")
                    choice = readln().toInt()

                    when (choice) {

                        // Display
                        1 -> {

                            for ((key, student) in studentsMap) {

                                println("Key : $key")
                                println("First Name : ${student.firstname}")
                                println("Last Name  : ${student.lastname}")
                                println("Age        : ${student.age}")
                                println("Address    : ${student.address}")
                                println("----------------------------")
                            }
                        }

                        // Add
                        2 -> {

                            println("Enter Key:")
                            val key = readln().toInt()

                            println("Enter First Name:")
                            val first = readln()

                            println("Enter Last Name:")
                            val last = readln()

                            println("Enter Age:")
                            val age = readln().toInt()

                            println("Enter Address:")
                            val address = readln()

                            studentsMap[key] = Student(first, last, age, address)

                            println("Student Added Successfully!")
                        }

                        // Remove
                        3 -> {

                            println("Enter Key:")
                            val key = readln().toInt()

                            if (studentsMap.containsKey(key)) {
                                studentsMap.remove(key)
                                println("Student Removed Successfully!")
                            } else {
                                println("Key Not Found")
                            }
                        }

                        // Update
                        4 -> {

                            println("Enter Key:")
                            val key = readln().toInt()

                            if (studentsMap.containsKey(key)) {

                                println("Enter New First Name:")
                                studentsMap[key]!!.firstname = readln()

                                println("Enter New Last Name:")
                                studentsMap[key]!!.lastname = readln()

                                println("Enter New Age:")
                                studentsMap[key]!!.age = readln().toInt()

                                println("Enter New Address:")
                                studentsMap[key]!!.address = readln()

                                println("Student Updated Successfully!")

                            } else {

                                println("Key Not Found")
                            }
                        }

                        // Search
                        5 -> {

                            println("Enter Key:")
                            val key = readln().toInt()

                            if (studentsMap.containsKey(key)) {

                                val student = studentsMap[key]!!

                                println("Student Found")
                                println("First Name : ${student.firstname}")
                                println("Last Name  : ${student.lastname}")
                                println("Age        : ${student.age}")
                                println("Address    : ${student.address}")

                            } else {

                                println("Student Not Found")
                            }
                        }

                        6 -> println("Returning...")

                        else -> println("Invalid Choice")
                    }

                } while (choice != 6)
            }

            4 -> println("Thank You!")

            else -> println("Invalid Choice")
        }

    } while (choice != 4)
}