fun main() {

    println("Enter number of working hours:")
    val hours = readln().toInt()

    if (hours <= 2) {
        println("Parking is free")
    } else {
        var bill = hours * 2

        // Maximum bill is 20
        if (bill > 20) {
            bill = 20
        }

        println("Bill is $$bill")
    }
}