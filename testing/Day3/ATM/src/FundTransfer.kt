class FundTransfer(
    var amount: Double,
    var balance: Double,
    var reciever:BankAccount,
    val sender:BankAccount
)