fun tripletSum(arr: IntArray) {

    val n = arr.size
    var found = false
//nested for loop
    for (i in 0 until n - 2) {
        for (j in i + 1 until n - 1) {
            for (k in j + 1 until n) {

                if (arr[i] + arr[j] + arr[k] == 0) {
                    println("Triplet found: ${arr[i]}, ${arr[j]}, ${arr[k]}")
                    found = true
                }
            }
        }
    }
// if triplet not found
    if (!found) {
        println("No triplet found")
    }
}
// main function
fun main() {
    val nums = intArrayOf(-1, 0, 1, 2, -1, -4)

    println(nums.contentToString())
    tripletSum(nums)            //function call
}