fun main()
{
    println("Enter recharge")
    val recharge =readln().toInt()
    if (recharge>=50)
    {
        print("bonus is 5GB")
    }
    else if(recharge>=100)
    {
        println("bonus is 15GB")
    }
    else if( recharge>=200)
    {
        println("bonus is 40 40GB")
    }
    else
    {
        println("no bonus")
    }
}