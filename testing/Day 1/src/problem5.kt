fun main() {

    println("Enter your food bill:")
    val bill = readln().toDouble()

    val tax = bill * 0.05

    var totalBill = bill + tax

    if (bill > 100) {
        val serviceCharge = bill * 0.10
        totalBill += serviceCharge
    }

    println("Total Bill = $$totalBill")
}