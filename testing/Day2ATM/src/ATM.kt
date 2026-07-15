class ATM {
    fun showMenu(
        user: BankAccount,
        checkBalance: Nothing?.() -> Unit,
        deposit: Nothing?.(Int) -> Unit,
        withdraw: Nothing?.(Int) -> Unit
    ) {

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

            val account = null
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

