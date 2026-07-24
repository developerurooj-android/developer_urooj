# Android Calculator Application Documentation

## Project Title

**Android Calculator App**

---

# 1. Project Overview

The Android Calculator App is a basic calculator developed using **Kotlin** and **XML** in Android Studio. The application performs basic arithmetic operations including addition, subtraction, multiplication, and division. It provides a simple and user-friendly interface similar to a mobile calculator.

---

# 2. Objective

The objective of this project is to understand Android application development concepts by implementing:

* XML Layout Design
* Event Handling
* Button Click Listeners
* Kotlin Programming
* User Input Handling
* Arithmetic Operations
* Android Activity Lifecycle

---

# 3. Technologies Used

| Technology        | Purpose                 |
| ----------------- | ----------------------- |
| Kotlin            | Application Logic       |
| XML               | User Interface Design   |
| Android Studio    | Development Environment |
| Android SDK       | Android APIs            |
| AppCompatActivity | Activity Support        |

---

# 4. Project Structure

```
Calculator
│
├── app
│   ├── manifests
│   │     └── AndroidManifest.xml
│   │
│   ├── java
│   │     └── com.example.calculator
│   │            └── MainActivity.kt
│   │
│   └── res
│         ├── layout
│         │      └── activity_main.xml
│         │
│         ├── values
│         │      ├── strings.xml
│         │      ├── colors.xml
│         │      └── themes.xml
│         │
│         └── drawable
│
└── Gradle Scripts
```

---

# 5. User Interface

The application contains:

### Display Section

* Expression Display
* Result Display

### Number Buttons

```
0
1
2
3
4
5
6
7
8
9
```

### Operator Buttons

```
+
-
×
÷
=
```

### Utility Buttons

```
AC
DEL
.
```

---

# 6. Working Flow

```
Application Starts
        │
        ▼
User Presses Numbers
        │
        ▼
Number Appears on Display
        │
        ▼
User Selects Operator
        │
        ▼
Operator Stored
        │
        ▼
User Enters Second Number
        │
        ▼
Expression Displayed
        │
        ▼
User Presses '='
        │
        ▼
Calculation Performed
        │
        ▼
Result Displayed
```

---

# 7. Functionalities

## Addition

Example:

```
10 + 20

Result = 30
```

---

## Subtraction

Example

```
50 - 15

Result = 35
```

---

## Multiplication

Example

```
20 × 30

Result = 600
```

---

## Division

Example

```
100 ÷ 4

Result = 25
```

---

## Decimal Calculation

Example

```
10.5 + 2.5

Result = 13
```

---

## Delete

Deletes the last entered digit or operator.

Example

```
1234

↓

123
```

---

## Clear

Removes the entire expression and resets the calculator.

---

# 8. Logic Used

The application stores:

```
Expression

Operator

Current Input
```

Example

```
Expression

100+20
```

When the equal button is pressed:

1. Expression is divided into two numbers.
2. Operator is identified.
3. Arithmetic operation is performed.
4. Result is displayed.

---

# 9. Main Variables

| Variable       | Description                                      |
| -------------- | ------------------------------------------------ |
| expression     | Stores complete mathematical expression          |
| operator       | Stores selected operator                         |
| isNewOperation | Indicates whether a new calculation should begin |

---

# 10. Main Functions

## appendNumber()

* Adds digits to the expression.
* Updates the display.

---

## appendOperator()

* Stores operator.
* Prevents multiple operators from being entered consecutively.

---

## calculate()

* Splits the expression.
* Converts numbers to Double.
* Performs the selected operation.
* Displays the result.

---

## clearCalculator()

* Clears the expression.
* Clears the operator.
* Resets the calculator.

---

## deleteLastCharacter()

* Removes the last entered character.
* Updates the display.

---

# 11. Validation

The application performs the following validations:

* Prevents entering two operators consecutively.
* Prevents division by zero.
* Prevents calculation without valid input.
* Supports multi-digit numbers.
* Supports decimal values.

---

# 12. Sample Output

### Addition

```
Expression

10+20=

Result

30
```

---

### Multiplication

```
Expression

100×300=

Result

30000
```

---

### Division

```
Expression

250÷5=

Result

50
```

---

# 13. Concepts Used

* Kotlin Programming
* XML Layout Design
* AppCompatActivity
* Event Handling
* Click Listeners
* Variables
* Functions
* Conditional Statements
* String Manipulation
* Type Conversion
* Exception Handling (Division by Zero)
* Android UI Components
* Android Activity Lifecycle

---



# 14. Conclusion

The Android Calculator App demonstrates the fundamentals of Android application development using Kotlin and XML. It provides a clean and intuitive interface for performing basic arithmetic operations while showcasing key Android concepts such as event handling, UI design, state management, and input validation. This project serves as a strong foundation for learning Android development and can be extended with advanced calculator features in the future.
