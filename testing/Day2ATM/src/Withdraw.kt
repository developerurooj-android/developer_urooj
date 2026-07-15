class Withdraw(private val account: BankAccount): BankAccount(account.accountNumber, account.name, account.pin, account.balance) {
    fun main(){
   println("---Withdraw Money---")
   println("Enter amount to withdraw")
   val amount= readln()!!.toDouble()
if(amount <= balance){
    println("Transaction successful")
}
        else{
            println("invalid amount try again")
        }
        var currentAmount = balance-amount
        println("Current amount: $amount")
    }


}