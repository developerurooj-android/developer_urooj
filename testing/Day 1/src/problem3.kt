fun main()
{
    println("Enter number of working hours")
    val hours =readln().toInt()
    println("Enter hourly wage")
    val wage =readln().toDouble()
    val salary = hours*wage
    println("salaray is $salary")
    if(hours>40)
    {
       val Salary = hours*wage * 1.5
        println("salary for above 40 hours is $Salary")
    }
}