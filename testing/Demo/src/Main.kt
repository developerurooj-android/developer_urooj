
// Package
package demo

// Function
fun sayHello(name: String) {
   println("Hello, $name")   // String Template
}

// Program Entry Point
fun main() {

   // Variables
   val language = "Kotlin"
   var age = 20

   // Print Output
   println(language)

   // Input
   print("Enter your name: ")
   val name = readln()

   // Function Call
   sayHello(name)

   // if-else
   if (age >= 18) {
      println("Adult")
   } else {
      println("Minor")
   }

   // when Expression
   when (age) {
      20 -> println("Age is 20")
      else -> println("Age is something else")
   }

   // for Loop
   for (i in 1..3) {
      println(i)
   }

   // while Loop
   var count = 1
   while (count <= 2) {
      println(count)
      count++
   }

   // Range
   println(1..5)

   // Collection
   val fruits = listOf("Apple", "Banana")
   println(fruits)

   // Nullable Value
   var city: String? = null

   // Null Check
   println(city?.length)

   // Type Check & Smart Cast
   val obj: Any = "Kotlin"
   if (obj is String) {
      println(obj.length)
   }
}