fun main() {

    println("Enter your bill:")
    val bill = readln().toInt()

    if (bill >= 500) {
        println("discount in your bill is 20 %")
        val discount =bill - (bill * 10 / 100)
        println("Your bill is $discount")
    }
    else if (bill in 200..500) {
        println("Discount on your bill is 10%")
        val discount = bill - (bill * 10 / 100)
        println("Your bill is $discount")
    }
    else {
        println("No discount")
    }
}