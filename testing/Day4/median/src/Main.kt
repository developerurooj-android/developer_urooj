
fun main(){
    val nums1 = intArrayOf(1,2,3,4,)
    val nums2 = intArrayOf(7,8,9)
    val merge= (nums1 + nums2).sorted()
    println(merge)
    var n = merge.size
    if(n%2==1){
        n =n/2

        println("median is ${merge[n]}")
    }

}