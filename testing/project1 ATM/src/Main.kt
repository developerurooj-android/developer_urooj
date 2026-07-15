// inhertinace Parent class
open class bankAccount(
    var accountnumber: String,
    var username: String,
    protected var balance: Double
)
{
    private var attempts=3
    var correctpin=12345
    fun verifyPin()
    {
        println("Enter your pin")
        val pin=readln().toInt()
        if(pin==correctpin)
        {
            println("Welcome")
            return
        }
        else
        {
            attempts --
            if(attempts>0)
            {
                println("Incorrect pin. You have $attempts times left try again")
            }
        }


    }
}