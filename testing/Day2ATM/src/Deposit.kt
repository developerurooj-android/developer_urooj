class Deposit(
    var balance: Int
) {

    // use for deposit
    fun deposit(amount: Int) {
        println("Enter amount: $amount")
        if(amount>0){
        balance += amount
        println("Deposited: $amount")
        println("Current Balance: $balance")
    }
        else
        {
            println("invalid amount try again")
        }
}
}