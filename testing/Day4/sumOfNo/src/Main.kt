//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    var sum: Int=0
  for (i in 1 until 100)
        if(i % 3==0 || i % 5==0)
        {
            println(i)
        sum+=i
    }
    println(sum)
}