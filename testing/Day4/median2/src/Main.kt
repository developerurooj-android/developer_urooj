// merge two arrays
fun mergeArray(
    nums1: IntArray,
    nums2: IntArray
): IntArray {
    for (i in 0..nums1.lastIndex) {
        for (j in 0..nums2.lastIndex) {
            nums1[i] + nums2[j]
        }
    }
    return nums1 + nums2
}

// sort merge array
fun sort(merge: IntArray): IntArray {

    val n = merge.size

    for (i in 0 until n) {
        for (j in 0 until n - 1 - i) {
            if (merge[j] > merge[j + 1]) {
                val temp = merge[j]
                merge[j] = merge[j + 1]
                merge[j + 1] = temp
            }
        }
    }
    return merge
}

// median of merged array
fun median(sort: IntArray): Double {
    val n = sort.size
    return if (n % 2 == 0) {                        // if even number
        val n1 = (n / 2) - 1                        // index1
        val n2 = n / 2                              // index2
        (sort[n1] + sort[n2]) / 2.0
    } else {                                        // if odd number
        sort[n / 2].toDouble()
    }
}

// main function
fun main() {
    val nums1 = intArrayOf(1, 2, 3, 5, 6)
    val nums2 = intArrayOf(7, 8, 9, 0)

    val merged = mergeArray(nums1, nums2)
    val sortedArray = sort(merged)

    println(sortedArray.contentToString())

    val result = median(sortedArray)
    println("Median = $result")
}