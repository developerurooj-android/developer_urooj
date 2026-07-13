fun main(){
    val Pin = 1234
    var attempts= 3
        println("Enter your pin")
    val pin =readln ().toInt()
    if (pin==Pin)
    {
        println("Welcome")
    }
    else
        {
        attempts --
    if(attempts>0)
    {
        println("your pin iss incorrect. Try again you have $attempts left")
        for(i in 1..attempts)
        {
            println("Enter your pin")
            val pin =readln ().toInt()
        }
    }
        }
    }
