class BankAccount(
    var accountNumber: String,
    var accountName: String,
    var pin: String,
    var balance: Double,
    var transactions: MutableList<Transaction> = mutableListOf()
)