//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

   do{

       println("======Basic Calculator======")
       println("1. Add two numbers")
       println("2. Subtract two numbers")
       println("3. Multiply two numbers")
       println("4. Divide Two numbers")
       println("5. Exit")

       println("select your choice")
       var choice: Int
       choice = readln().toInt()


       // choose from menu
       when(choice) {

       1 ->{
           //Input two numbers
           println("Enter 1st number")
           val num1 = readln().toInt()
           println("Enter 2nd number")
           val num2 = readln().toInt()
           val result = num1 + num2
           println("The sum of two numbers is $result")
       }

           2 -> {
               //Input two numbers
               println("Enter 1st number")
               val num1 = readln().toInt()
               println("Enter 2nd number")
               val num2 = readln().toInt()
           val result = num1 - num2

           println("The difference of two numbers is $result")
       }
           3->{
               //Input two numbers
               println("Enter 1st number")
               val num1 = readln().toInt()
               println("Enter 2nd number")
               val num2 = readln().toInt()
           val result = num1 * num2
           println("The product of two numbers is $result")
       }
           4->{
               //Input two numbers
               println("Enter 1st number")
               val num1 = readln().toInt()
               println("Enter 2nd number")
               val num2 = readln().toInt()
           if(num2>0)
           {
               val result = num1 / num2
               println("The division of two numbers is $result")
           }
           else
           {
               println("inavlid try again")
           }
           }
       }
   }
       while(choice!=4)
}