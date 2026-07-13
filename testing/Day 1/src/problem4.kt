fun main() {

    for (i in 1..5) {

        println("Enter marks of subject $i:")
        val marks = readln().toInt()

        if (marks in 90..100) {
            println("Grade: A")
        }
        else if (marks in 80..89) {
            println("Grade: B")
        }
        else if (marks in 70..79) {
            println("Grade: C")
        }
        else if (marks in 60..69) {
            println("Grade: D")
        }
        else {
            println("Grade: F")
        }

        println()
    }
}