open class BankAccount(
    var accountNumber: String,
    val name: String,
    val pin: Int,
    open var balance: Double
) {
    val accountName: String = "$name#$accountNumber"
}

fun main() {

    val accounts = mutableListOf(
        BankAccount("1001", "Ali", 1234, 50000.0),
        BankAccount("1002", "Sara", 2345, 30000.0),
        BankAccount("1003", "Ahmed", 3456, 40000.0)
    )

    println("=============")
    println("Mezan Bank ATM")
    println("================")

    print("Enter Account Number: ")
    val account = readln()

    print("Enter PIN: ")
    val pin = readln().toInt()

    var found = false
    val loggedinUser: BankAccount?=null

    for (user in accounts) {

        if (user.accountNumber == account && user.pin == pin) {

            println("Welcome ${user.name}")
            found = true
            var loggedInUser = user
            break
        }
    }

    if (!found) {

        println("Account not found.")
        println("Creating New Account...")

        print("Enter Name: ")
        val name = readln()

        print("Enter Initial Balance: ")
        val balance = readln().toDouble()

        print("Create PIN: ")
        val newPin = readln().toInt()

        print("Confirm PIN: ")
        val confirmPin = readln().toInt()

        if (newPin == confirmPin) {

            val accountNumber = (1000 + accounts.size + 1).toString()

            accounts.add(
                BankAccount(accountNumber, name, newPin, balance)
            )

            println("Account Created Successfully!")
            println("Your Account Number is $accountNumber")

        } else {

            println("PINs do not match.")
        }
    }
}