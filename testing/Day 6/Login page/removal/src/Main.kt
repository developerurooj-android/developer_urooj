fun removeElement(nums: IntArray, `val`: Int): Int {

    var j = 0

    for (i in nums.indices) {
        if (nums[i] != `val`) {
            nums[j] = nums[i]
            j++
        }
    }

    return j
}

fun main() {

    val nums = intArrayOf(3, 2, 2, 3)

    val k = removeElement(nums, 3)

    println("k = $k")

    println("First k elements:")
    for (i in 0 until k) {
        print("${nums[i]} ")
    }
}