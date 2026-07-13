fun main() {

    val correctPin = 1234
    var attempts = 3

    do {
        println("Enter your PIN:")
        val pin = readln().toInt()

        if (pin == correctPin) {
            println("Welcome")
            return
        }

        attempts--

        if (attempts > 0) {
            println("Incorrect PIN. You have $attempts attempt(s) left.")
        }

    } while (attempts > 0)

    println("Your account is blocked.")
}