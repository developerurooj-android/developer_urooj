fun main()
{
    val currentbalance = 500000
    println("Enter your bank balance")
    val balance = readln().toInt()
    if (balance == currentbalance)
    {
        println("Deduct amount")
    }
    else
    {
        println("insuficent balance")
    }
}