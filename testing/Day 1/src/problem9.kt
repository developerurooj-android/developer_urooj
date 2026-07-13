fun main()
{
    println("Enter your electricity units")
    val units=readln().toInt()
if (units==100){
 val bill= 100*0.50
println("your electricity bill is $bill")
}
if (units==200)
{
  val bill=200*0.75
println("your bill is $bill")
}
    if (units>=200)
    {
        val bill=units*1.20
        println("your bill is $bill")
    }
    }