import jdk.internal.vm.Continuation.pin
import java.text.SimpleDateFormat
import java.util.Date

fun main() {

    val accountsMap = hashMapOf(
        1 to BankAccount("1234", "Ali", "1122", 50000.0),
        2 to BankAccount("2345", "Ahmer", "1223", 60000.0),
        3 to BankAccount("2346", "Urooj", "1234", 70000.0),
        4 to BankAccount("2347", "Fatima", "2345", 80000.0)
    )

//Welcome or login page
    println("==========")
    println("Meezan Bank")
    println("==========")

    print("Enter your account number: ")
    var accountNumber = readln()

    var found = false

    for ((key, user) in accountsMap){

        if (accountNumber == user.accountNumber) {

            found = true
            println("Account Found!")

            var attempts = 3

            while (attempts > 0) {

                print("Enter your PIN: ")
                val enteredPin = readln()

                if (enteredPin == user.pin) {

                    println("Welcome ${user.accountName}")
                    println("Login Successful")

                    var choice: Int

                    do {
                        println("\n========== Meezan Bank ATM ==========")
                        println("1. Balance Inquiry")
                        println("2. Cash Deposit")
                        println("3. Cash Withdrawal")
                        println("4. Funds Transfer")
                        println("5. Change PIN")
                        println("6. Mini Statement")
                        println("7. Logout")

                        print("Enter your choice: ")
                        choice = readln().toInt()

                        when (choice) {
                            // balance check
                            1 -> {
                                println("=====Balance Inquiry=====")
                                println("Your Balance is: Rs. ${user.balance}")
                            }
                                //cash deposit
                            2 -> {
                                println("=====Cash Deposit=====")

                                println("Your Current Balance: Rs. ${user.balance}")

                                print("Enter amount to deposit: ")
                                val amount = readln().toDouble()
                                user.balance += amount

                            //save transactions

                                user.transactions.add(
                                    Transaction(
                                        "Cash Deposit",
                                        amount,
                                        user.balance,
                                        System.currentTimeMillis()
                                    )
                                )
                                println("Rs. $amount deposited successfully.")
                                println("Your current balance is: Rs. ${user.balance}")
                            }
                            // cash withdraw

                            3 -> {
                                println("Your Current Balance: Rs. ${user.balance}")

                                print("Enter amount to withdraw: ")
                                val amount = readln().toDouble()
                                if (amount <= user.balance) {
                                    user.balance -= amount
                                    //save transactions
                                    user.transactions.add(
                                        Transaction(
                                            "Cash Deposit",
                                            amount,
                                            user.balance,
                                            System.currentTimeMillis()
                                        )
                                    )

                                    println("Rs. $amount withdraw successfully.")
                                    println("Your current balance is: Rs. ${user.balance}")
                                } else {
                                    println("Enter valid amount")
                                }
                                //Validation
                                if(amount<= 0) {
                                    println("Error:Amount must be greater than 0")
                                }

                            }
                            //Funds Transfer

                            4 -> {

                            println("\n========== FUND TRANSFER ==========")

                            // Sender Details
                            println("Sender Details")
                            println("Account Name   : ${user.accountName}")
                            println("Account Number : ${user.accountNumber}")
                            println("Balance        : Rs. ${user.balance}")

                            println()

                            // Receiver Account
                            print("Enter Receiver Account Number: ")
                            val receiverAccNo = readln()

                            var receiver: BankAccount? = null

                            for ((_, account) in accountsMap) {

                                if (account.accountNumber == receiverAccNo) {
                                    receiver = account
                                    break
                                }
                            }

                            if (receiver != null) {

                                println("\nReceiver Found")
                                println("Account Name   : ${receiver.accountName}")
                                println("Account Number : ${receiver.accountNumber}")

                                print("\nEnter Amount to Transfer: ")
                                val amount = readln().toDouble()
                                //validation
                                if (amount <= user.balance) {

                                    user.balance -= amount
                                    receiver.balance += amount

                                    // Sender History
                                    user.transactions.add(
                                        Transaction(
                                            "Sent to ${receiver.accountName}",
                                            amount,
                                            user.balance,
                                            System.currentTimeMillis()
                                        )
                                    )

                                    // Receiver History
                                    receiver.transactions.add(
                                        Transaction(
                                            "Received from ${user.accountName}",
                                            amount,
                                            receiver.balance,
                                            System.currentTimeMillis()
                                        )
                                    )

                                    println("\nTransfer Successful!")
                                    println("Rs. $amount transferred to ${receiver.accountName} by ${user.accountName}")


                                } else {

                                    println("Insufficient Balance.")
                                }

                            } else {

                                println("Receiver Account Not Found.")
                            }
                        }
                            //change pin
                            5 -> {

                                println("----- Change PIN -----")

                                print("Enter your old PIN: ")
                                val oldPIN = readln()

                                if (oldPIN == user.pin) {

                                    print("Enter your new PIN: ")
                                    val newPIN = readln()

                                    print("Confirm your new PIN: ")
                                    val confirmPIN = readln()

                                    if (newPIN == confirmPIN) {

                                        user.pin = newPIN

                                        println("PIN changed successfully!")

                                    } else {

                                        println("PIN confirmation does not match.")
                                    }

                                } else {

                                    println("Incorrect old PIN.")
                                }
                            }
                                //Mini statements
                            6 -> {

                                println("\n========== MINI STATEMENT ==========")
                                println("Account Name   : ${user.accountName}")
                                println("Account Number : ${user.accountNumber}")
                                println("Current Balance: Rs. ${user.balance}")

                                println("\n------ Transaction History ------")

                                val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

                                if (user.transactions.isEmpty()) {

                                    println("No Transactions Available")

                                } else {

                                    for (transaction in user.transactions) {

                                        val date = formatter.format(Date(transaction.time))

                                        println("-------------------------------------------")
                                        println("Time            : $date")
                                        println("Transaction     : ${transaction.type}")
                                        println("Amount          : Rs. ${transaction.amount}")
                                        println("Current Balance : Rs. ${transaction.balance}")
                                    }
                                }
                            }

                            7 -> {
                                println("Logged Out Successfully.")
                            }

                            else -> {
                                println("Invalid Choice")
                            }
                        }

                    } while (choice != 7)

                    return

                } else {

                    attempts--

                    if (attempts > 0) {
                        println("Wrong PIN. Attempts left: $attempts")
                    } else {
                        println("Login Failed!")
                    }
                }
            }
        }
    }
        //account not exist create new account
    if (!found) {

        println("Account does not exist.")
        println("Please create a new account.")

        val key = accountsMap.size + 1   // Auto-generate key

        println("Enter Account Name:")
        val accountName = readln()

        println("Enter Account Number:")
        val accountNumber = readln()

        println("Enter PIN:")
        val pin = readln()

        println("Enter Balance:")
        val balance = readln().toDouble()

        accountsMap[key] = BankAccount(
            accountNumber,
            accountName,
            pin,
            balance
        )

        println("\nAccount Created Successfully!")
        println("Returning to Main Menu...\n")

        return  // Goes back to the start of the do-while loop
    }
    }
