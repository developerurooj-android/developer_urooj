open class BankAccount(
    var accountNumber: String,
    var username: String,
    protected var balance: Int,
    d: Double
) {

    var attempts = 3
    private val correctPin = "12345"

    fun verifyPin(pin: String): Boolean {
        if (pin == correctPin) {
            return true
        } else {
            attempts--
            println("Incorrect PIN. Attempts left: $attempts")
            return false
        }
    }

    open fun checkBalance() {
        println("Account Holder: $username")
        println("Balance: $balance")
    }

    open fun deposit(amount: Int) {
        balance += amount
        println("Deposited: $amount")
        println("Current Balance: $balance")
    }

    open fun withdraw(amount: Int) {

        if (amount <= balance) {
            balance -= amount
            println("Withdrawn: $amount")
            println("Current Balance: $balance")
        } else {
            println("Insufficient Balance")
        }
    }
}

class SavingsAccount(
    accountNumber: String,
    username: String,
    balance: Int
) : BankAccount(accountNumber, username, balance, 50000.0) {

    fun addInterest() {
        val interest = (balance * 0.05).toInt()
        balance += interest
        println("Interest Added: $interest")
        println("New Balance: $balance")
    }
}

class ATM {

    fun start(account: BankAccount) {

        println("===== ATM =====")

        while (account.attempts > 0) {

            print("Enter PIN: ")
            val pin = readln()

            if (account.verifyPin(pin)) {

                println("Login Successful")
                showMenu(account)
                return
            }
        }

        println("Account Blocked")
    }

    fun showMenu(account: BankAccount) {

        var choice: String

        do {

            println()
            println("===== ATM MENU =====")
            println("1. Check Balance")
            println("2. Deposit")
            println("3. Withdraw")
            println("4. Exit")

            print("Enter choice: ")
            choice = readln()

            when (choice) {

                "1" -> account.checkBalance()

                "2" -> {
                    print("Enter amount: ")
                    val amount = readln().toInt()
                    account.deposit(amount)
                }

                "3" -> {
                    print("Enter amount: ")
                    val amount = readln().toInt()
                    account.withdraw(amount)
                }

                "4" -> println("Thank you for using ATM")

                else -> println("Invalid Choice")
            }

        } while (choice != "4")
    }
}

fun main() {

    val myAccount = SavingsAccount(
        "123",
        "Urooj",
        123456
    )

    val atm = ATM()

    atm.start(myAccount)
}