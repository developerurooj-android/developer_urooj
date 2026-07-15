class Bankacount(
    accountNumber: String,
    username: String,
    balance: Int,
    pin: String,
)
{
    fun main()
    {
        val accounts = mutableListOf(
            BankAccount("1001", "Ali", 1234, 50000.0),
            BankAccount("1002", "Sara", 2345, 30000.0),
            BankAccount("1003", "Ahmed", 3456, 45000.0),
            BankAccount("1004", "Fatima", 4567, 25000.0),
            BankAccount("1005", "Urooj", 5678, 60000.0)
        )
        var choice: Int
                do{
                    println("=======================")
                    println("Welcome to Mezan Bankacount")
                    println("========================")
                    println("Enter account number")
                    val accountNumber = readln().toInt()
                    println("Enter pin")

                    }
    }

    }
}