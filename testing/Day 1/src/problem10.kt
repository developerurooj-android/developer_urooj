fun main()
{
    println("Enter username")
    val username = readln().toString()
    println("Enter password")
    val password= readln().toInt()
    if (username =="Urooj" && password==1233){
        println("login successful")
    }
    else{
        println("login failed")
    }

}