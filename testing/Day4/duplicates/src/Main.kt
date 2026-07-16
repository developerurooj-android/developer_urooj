//Remove Duplicate function
fun removeDuplicate(array: IntArray): Int {
//case 1 array is empty
    if (array.isEmpty())
    {
        println("Array is empty")
        return 0
    }

// if array is not empty
    var j = 0           //position of unique elements

    for (i in 1 until array.size) {
        if (array[i] != array[j]) {
            j++
            array[j] = array[i]


        }
    }

    return j+1
}
// main function

fun main() {
    val array = intArrayOf(1, 2, 2, 3, 3, 6, 6, 8, 10, 10)

    val length = removeDuplicate(array)             // function call

    for (i in 0 until length) {
        print("${array[i]} ")
    }
}