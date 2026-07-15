package data

import BankAccount

class Transfer(
    private val account:BankAccount,
    override var balance: Double
) : BankAccount(account.accountNumber, account.name, account.pin, account.balance) {
    fun main()
    {
        println("----- Transfer Money -----")
        print("Enter receiver account number: ")
        val ReceiverAccNo = readln().toInt()
        val accounts = null
        val reciever = accounts.find{
            val it = null
            accountNumber = ReceiverAccNo.toString()
        }
        if (reciever != null) {
            println("Enter amount to transfer:")
            val amount = readln()!!.toDouble()
            if (amount<=account.balance ) {
                account.balance -= amount
                reciever.balance += amount
                println("$amount transferred to ${reciever}")
                println("your new balance is ${account.balance}")
            }
            else
            {
                println("No transfer available")
            }
        }
        else
        {
            println("receiver not found")
        }
    }
}