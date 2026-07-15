class CheckBalance(private val account:BankAccount): BankAccount(
    account.accountNumber, account.name, account.pin, account.balance
){
    fun executer(){
        println("-----check balance------")
        println("Account: ${account.accountName}")
        println("Account ${account.accountNumber}")
        println("current balance: ${account.balance}")
        }

}