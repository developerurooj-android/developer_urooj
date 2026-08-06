

#https://developer.android.com/guide/fragments

#https://source.android.com/docs/core/audio/terminology
# Daily Learning Report
**Week 1**
**Date:** July 13, 2026
Day 1
## Objective

Learn the fundamentals of Kotlin programming language and strengthen problem-solving skills through coding practice.

## Tasks Completed

* Studied Kotlin basic syntax and core concepts.
* Learned about:

  * Variables (`val` and `var`)
  * Data types
  * Functions
  * Input and output (`print`, `println`, `readln`)
  * Conditional statements (`if`, `when`)
  * Loops (`for`, `while`)
  * Ranges and collections
  * Null safety basics
  * String templates
* Explored Kotlin examples and practiced writing simple programs.
* Solved **10 Kotlin programming problems** to reinforce the concepts learned.
1. ATM PIN Verification
Problem:
 An ATM allows only 3 attempts to enter the correct PIN. If the PIN is correct, display "Welcome". Otherwise, block the account after 3 failed attempts.
Concepts: if, loops, variables

2. Online Shopping Discount
Problem:
 A shopping website gives:

• 20% discount if the bill is above $500..
• 10% discount if the bill is between $200 and $500..
• No discount otherwise..
Calculate the final amount.
Concepts: if-else, arithmetic operators

3. Employee Salary Calculator
Problem:
 An employee works 8 hours per day.

• Input hourly wage and hours worked..
• Calculate total salary..
• If overtime exceeds 40 hours per week, pay 1.5× the hourly rate for extra hours..
Concepts: Variables, conditions

4. Student Grade System
Problem:
 Input marks for 5 subjects.

• Calculate total and percentage..
• Display grades:.
◦ A: 90–100.
◦ B: 80–89.
◦ C: 70–79.
◦ D: 60–69.
◦ F: Below 60.
Concepts: Loops, arrays, conditions

5. Restaurant Bill Generator
Problem:
 A customer orders food items.

• Calculate the total bill..
• Add 5% tax..
• Add a 10% service charge if the bill exceeds $100..
Concepts: Arithmetic, conditions

6. Mobile Recharge Offer
Problem:
 A telecom company offers:

• Recharge ≥ $50 → 5 GB bonus..
• Recharge ≥ $100 → 15 GB bonus..
• Recharge ≥ $200 → 40 GB bonus..
Display the bonus received.
Concepts: when or if-else

7. Parking Fee Calculator
Problem:
 Parking charges:

• First 2 hours: Free.
• Next hours: $2/hour.
• Maximum charge: $20.
Input parking hours and calculate the fee.
Concepts: Conditions, arithmetic

8. Bank Balance Checker
Problem:
 A customer wants to withdraw money.

• If the balance is sufficient, deduct the amount..
• Otherwise, display "Insufficient Balance.".
Concepts: Variables, conditions

9. Electricity Bill Calculator
Problem:
 Electricity rates:

• First 100 units → $0.50/unit.
• Next 100 units → $0.75/unit.
• Above 200 units → $1.20/unit.
Calculate the total bill.
Concepts: Conditional logic

10. Login System
Problem:
 A user enters a username and password.

• If both match stored values, display "Login Successful.".
• Otherwise, display "Invalid Username or Password.".
Concepts: Strings, conditions
## Skills Gained

* Understanding of Kotlin syntax and program structure.
* Ability to declare variables and write functions.
* Improved knowledge of control flow using conditions and loops.
* Basic understanding of collections and null safety.
* Enhanced logical thinking and problem-solving through hands-on coding practice.

## Challenges Faced

* Understanding Kotlin-specific syntax compared to Java.
* Learning the difference between immutable (`val`) and mutable (`var`) variables.

## Outcome

Successfully completed Kotlin basics and solved 10 practice problems. Gained confidence in writing simple Kotlin programs and built a strong foundation for Android development.


## Day 2 – Object-Oriented Programming (OOP) Concepts

### Topics Covered

On Day 2, I learned the core concepts of **Object-Oriented Programming (OOP)** in Kotlin. These concepts help in writing clean, reusable, and maintainable code.

### Concepts Learned

#### 1. Inheritance

* Learned how a child class inherits properties and functions from a parent class.
* Understood the use of the `open` keyword for creating inheritable classes and methods.
* Practiced extending parent classes to avoid code duplication.

#### 2. Encapsulation

* Learned how to protect data by keeping variables private and controlling access through methods.
* Understood the importance of data hiding and secure access to class members.
* Implemented encapsulation using access modifiers such as `private`, `public`, and `protected`.

#### 3. Polymorphism

* Learned how the same function can perform different tasks depending on the object.
* Studied **method overriding** and **runtime polymorphism**.
* Understood how parent class references can point to child class objects.

#### 4. Abstraction

* Learned how to hide implementation details while exposing only essential functionality.
* Studied the use of **abstract classes** and **abstract methods**.
* Understood how abstraction simplifies complex systems by focusing on required features.

### Practical Learning

* Created classes and objects in Kotlin.
* Implemented inheritance between parent and child classes.
* Applied encapsulation using access modifiers.
* Used method overriding to demonstrate polymorphism.
* Created abstract classes and implemented them in child classes.

### Learning Outcome

By the end of Day 2, I developed a strong understanding of the four pillars of Object-Oriented Programming and learned how they improve code reusability, flexibility, security, and maintainability.

---

# Day 3 – Collections in Kotlin

### Topics Covered

On Day 3, I learned different collection types available in Kotlin and understood when to use each data structure based on the application requirements.

### Concepts Learned

#### 1. Array

* Learned how to create and initialize arrays.
* Accessed and updated array elements using indexes.
* Understood that arrays have a fixed size once created.

#### 2. ArrayList

* Learned how to create dynamic lists using `ArrayList`.
* Performed operations such as adding, updating, removing, and searching elements.
* Understood that `ArrayList` can grow and shrink dynamically.

#### 3. Empty List

* Learned how to create an empty list using `emptyList()`.
* Understood that elements cannot be added after creation because it is immutable.

#### 4. Mutable List

* Learned how to create mutable lists using `mutableListOf()`.
* Performed operations such as:

  * Adding elements
  * Removing elements
  * Updating values
  * Iterating through the list
* Understood that mutable lists allow modifications after creation.

#### 5. Immutable List

* Learned how to create immutable lists using `listOf()`.
* Understood that once created, the list cannot be modified.
* Learned when immutable collections are useful for maintaining data integrity.

#### 6. HashMap

* Learned how to store data in **key-value pairs**.
* Performed operations including:

  * Adding entries
  * Updating values
  * Removing entries
  * Accessing values using keys
  * Iterating through key-value pairs
* Understood that `HashMap` provides fast data retrieval using unique keys.

### Practical Learning

* Implemented programs using arrays and `ArrayList`.
* Compared fixed-size arrays with dynamic `ArrayList`.
* Worked with mutable and immutable collections.
* Practiced creating and manipulating `HashMap` objects for efficient data storage.

### Learning Outcome

By the end of Day 3, I gained a clear understanding of Kotlin collection types, including Arrays, ArrayLists, Lists, and HashMaps. I learned the differences between mutable and immutable collections and understood how to choose the appropriate data structure for different programming scenarios.
# Day 4 Learning Report

## Objective

The objective of Day 4 was to improve problem-solving skills in Kotlin by practicing array-based coding problems, understanding nested loops, and implementing common algorithms.

## Topics Covered

### 1. Nested Loops

* Learned how nested loops work.
* Understood their importance in solving array problems where multiple elements need to be compared.
* Used nested loops to generate different combinations of array elements.

### 2. Triplet Sum Problem

* Implemented the Triplet Sum algorithm using three nested loops.
* Learned how to check whether the sum of three elements is equal to zero.
* Fixed common programming mistakes such as:

  * Incorrect loop boundaries.
  * Array index out-of-bounds errors.
  * Returning from the function too early.
* Modified the program to display all valid triplets instead of stopping after finding the first one.

### 3. Remove Duplicates from a Sorted Array

* Solved the Remove Duplicates from a Sorted Array problem using the two-pointer technique.
* Learned how to overwrite duplicate values while keeping only unique elements.
* Understood why this approach has **O(n)** time complexity and **O(1)** extra space complexity.

### 4. Debugging and Code Improvement

* Learned how to debug Kotlin programs.
* Corrected array printing using `contentToString()`.
* Improved string interpolation using `${}`.
* Identified logical errors and optimized code for better readability and correctness.

## Key Concepts Learned

* Arrays in Kotlin
* Nested loops
* Triplet Sum algorithm
* Two-pointer technique
* Removing duplicates from a sorted array
* Time Complexity: **O(n)** and **O(n³)**
* Debugging and code optimization

## Outcome

By the end of Day 4, I gained a better understanding of array manipulation, nested loop logic, debugging techniques, and solving coding interview-style problems in Kotlin. I also learned how to optimize code and avoid common programming mistakes while working with arrays.
# Day 5 Learning Report

## Topics Covered

On Day 5, I began learning Android development by understanding the Android project structure and the basics of XML. These concepts are essential for building Android applications and designing user interfaces.

### Android Project Structure

During today's session, I learned about the main components of an Android project, including:

* **AndroidManifest.xml** – Defines the application's configuration, activities, and permissions.
* **MainActivity.kt** – The main Kotlin file where the application logic is written.
* **res Folder** – Contains all application resources, including:

  * **layout** – Stores XML layout files.
  * **drawable** – Stores images and drawable resources.
  * **mipmap** – Stores launcher icons.
  * **values** – Contains strings, colors, themes, and other resource values.

### Basics of XML

I also learned the fundamentals of XML and its role in Android development.

* What XML (Extensible Markup Language) is.
* Why XML is used to design Android user interfaces.
* The structure of an XML file.
* Opening and closing tags.
* Attributes used within XML elements.
* The concept of a root layout.
* The purpose of XML namespaces such as `xmlns:android` and `xmlns:app`.

### Practical Work

* Explored the Android Studio project structure.
* Opened and examined the default XML layout file.
* Learned how XML files are connected with Kotlin using `setContentView()`.
* Modified simple XML elements to understand how UI changes appear in the application.

## Learning Outcome

By the end of Day 5, I understood the structure of an Android project and the basics of XML. This knowledge has provided a strong foundation for designing Android user interfaces and preparing for more advanced UI components in future sessions.

Sure! Here's the corrected **Day 6 Report** reflecting that you only **studied the calculator concept** and did not build it.

---
**Week 2**
# ** Day 1**

**Date:** July 20, 2026
**Technology:** Android Development (Kotlin + XML)

## **Tasks Performed**

* Created a **Login Screen** using XML.
* Implemented login functionality using a **fixed email and password**.
* Learned how to navigate between activities using **Intent**.
* Created a **Home Screen** that opens after a successful login.
* Implemented **Logout** functionality to return from the Home screen to the Login screen.
* Learned the purpose and structure of **AndroidManifest.xml** and how to register activities.
* Added a **Toolbar** to the Home screen and explored different ways to place a Logout button.
* Took an **overview of a Basic Calculator** in Android, including its UI components and logic for arithmetic operations (addition, subtraction, multiplication, and division).
* Studied the concept of **RecyclerView** and its importance in displaying lists efficiently.
* Learned the architecture of RecyclerView, including:

  * Model Class
  * Adapter
  * ViewHolder
  * Item Layout
* Understood the working of a **RecyclerView Adapter**, including:

  * `onCreateViewHolder()`
  * `onBindViewHolder()`
  * `getItemCount()`

## **Concepts Learned**

* Activity Navigation using `Intent`
* Android Manifest configuration
* Toolbar and basic UI design
* Login and Logout implementation
* Basic Calculator overview
* RecyclerView fundamentals
* Adapter and ViewHolder concepts
* Data flow from Model → Adapter → RecyclerView

## **Challenges Faced**

* Resolved manifest configuration issues.
* Fixed activity navigation errors.
* Corrected XML attribute errors (`textSize`, `textStyle`).
* Understood the relationship between RecyclerView, Adapter, ViewHolder, and Item Layout.
* Fixed layout issues related to the Home screen and Logout button.

## **Outcome**

By the end of today's session, I successfully developed a multi-screen Android application with Login and Home screens, implemented navigation between activities, and gained a conceptual understanding of the Basic Calculator and RecyclerView. I also learned how an Adapter connects data with a RecyclerView to display dynamic lists efficiently.

## **Next Learning Goals**

* Build a **Basic Calculator** in Android.
* Create a **RecyclerView Student List** application.
  
* Design list items using **CardView**.
* Handle item click events and pass data between activities using `Intent`.
# **Day 2 **

**Topic:** Android Navigation & Audio System Basics

## Objective

To understand Android Navigation concepts, Jetpack Compose navigation, and the fundamentals of the Android Audio System.

## Topics Covered

### 1. Jetpack Compose Navigation

* Learned the basics of **Jetpack Compose**.
* Understood the difference between **XML-based UI** and **Compose UI**.
* Learned how to create navigation using **NavController** and **NavHost**.
* Studied **composable destinations** and **dialog destinations**.

### 2. Navigation Concepts

* Learned about:

  * Fragment Destination
  * Activity Destination
  * Nested Navigation Graphs
  * Deep Links
  * Type-Safe Navigation using `@Serializable`
* Understood how navigation arguments are passed safely using Kotlin data classes instead of route strings.

### 3. Android Components

* Studied **Services** and their role in performing background tasks.
* Learned about **Broadcasts** and **Broadcast Receivers** for handling system events.

### 4. Android Audio Basics

* Gained an introduction to Android's audio architecture.
* Learned the purpose of:

  * AudioTrack
  * AudioRecord
  * MediaPlayer
  * MediaRecorder
  * Audio Focus
  * Audio Codecs (AC-3, AAC, MP3)
* Understood the concept of **Latency** and why low latency is important for audio and gaming applications.

### 5. Audio Hardware Concepts

* Learned basic concepts related to Android audio hardware, including:

  * Audio HAL (Hardware Abstraction Layer)
  * AIDL Audio HAL
  * Bluetooth Audio
  * USB Audio Interface
  * PCM Audio
  * Audio Interfaces
* Understood that these are mostly handled by the Android framework and hardware, while application developers typically use high-level APIs.

## Learning Outcome

By the end of Day 2 of 2nd week, I understood how Android manages navigation using the Navigation Component and Jetpack Compose, including different destination types, nested graphs, deep links, and type-safe navigation. I also gained a foundational understanding of the Android audio framework, common audio APIs, codecs, latency, and the role of the Audio HAL in connecting applications with audio hardware.

## Conclusion

Day 7 strengthened my understanding of Android Navigation and introduced me to the Android audio architecture. I learned when to use different navigation techniques and became familiar with the key concepts involved in audio playback, recording, and hardware interaction in Android.
# **Daily 3**

**Date:** 22 July 2026

## **Tasks Performed**

* Continued development of the **Student Management App** using **Kotlin**, **XML**, and **Android Studio**.
* Created and configured multiple fragments:

  * Student List Fragment
  * Add Student Fragment
  * Student Details Fragment
  * About Fragment
* Implemented **Navigation Component** by creating and configuring the `nav_graph.xml`.
* Fixed issues related to:

  * Incorrect fragment class names.
  * ViewBinding usage in fragments.
  * Navigation destination configuration.
  * RecyclerView binding and layout IDs.
* Designed the Student List screen using **RecyclerView** and displayed sample student data.
* Added a **Toolbar** in the MainActivity layout.
* Created an **Options Menu** containing:

  * Add Student
  * Edit Student
  * About
* Connected menu items with the Navigation Component using `NavController`.
* Performed debugging using **Logcat** to identify runtime exceptions such as:

  * `Fragment InstantiationException`
  * `ClassNotFoundException`
  * Incorrect fragment references in the Navigation Graph.
* Investigated why the Options Menu was not appearing and verified:

  * Toolbar setup
  * Theme configuration
  * Menu inflation
  * Navigation setup

## **Concepts Learned**

* Fragment lifecycle and ViewBinding implementation.
* Navigation Component configuration.
* Creating and managing multiple fragments.
* RecyclerView integration with fragments.
* Toolbar and Options Menu implementation.
* Using NavController for fragment navigation.
* Reading and analyzing Logcat for runtime errors.
* Importance of matching fragment class names with Navigation Graph entries.
* Debugging Android runtime crashes and UI issues.

## **Challenges Faced**

* Application crashed due to incorrect fragment references.
* Navigation Graph contained invalid fragment class names.
* Menu options were not appearing correctly in the Toolbar.
* Navigation between fragments was not functioning as expected.
* Resolved several configuration and runtime issues through debugging.


**Day:** 5
**Date:** 24 July 2026

## **Objectives**

The objective of today's session was to enhance the functionality and user experience of an Android Calculator application by implementing calculator logic, improving the display behavior, and understanding how expressions and results are managed in Kotlin.

## **Topics Covered**

* Developed a basic Android Calculator using **Kotlin** and **XML**.
* Implemented button click listeners for numeric and operator buttons.
* Learned how to store and update mathematical expressions using **String** variables.
* Implemented calculator operations:

  * Addition (+)
  * Subtraction (-)
  * Multiplication (×)
  * Division (÷)
* Implemented **AC (Clear)** functionality to reset the calculator.
* Implemented **DEL (Delete)** functionality to remove the last entered character.
* Added support for **decimal numbers**.
* Implemented validation to prevent entering multiple operators consecutively.
* Handled **division by zero** using exception handling.
* Learned how to format results to remove unnecessary decimal places (e.g., displaying `30` instead of `30.0`).
* Improved the calculator display by separating the **expression** and **result** using different `TextView`s.
* Explored how expression evaluation works through custom Kotlin functions.
* Studied the role of helper functions such as:

  * `appendNumber()`
  * `appendOperator()`
  * `calculate()`
  * `clearAll()`
  * `deleteLast()`
  * `updateDisplay()`
  * `formatResult()`
  * `evaluate()`

## **Challenges Faced**

* Managing expression updates while users entered numbers and operators.
* Handling unresolved reference errors caused by missing functions.
* Designing calculator logic similar to a mobile calculator.
* Displaying expressions and results correctly after pressing the equals button.
* Preventing invalid inputs and runtime errors during calculations.

## **Outcome**

By the end of the session, I successfully developed a functional Android Calculator capable of performing basic arithmetic operations. I also gained a better understanding of Kotlin event handling, expression management, UI updates, and implementing calculator logic using Android components.

## **Learning Outcome**

Today's work strengthened my understanding of:

* Android UI development using XML
* Kotlin programming fundamentals
* Event handling with Button Click Listeners
* String manipulation
* Arithmetic operations
* Exception handling
* Function design and code organization
* Calculator logic implementation
* Android application debugging



By the end of the day, the Student Management App structure was improved significantly. The RecyclerView-based Student List was functioning correctly, multiple fragments were integrated into the project, and considerable progress was made in configuring navigation and troubleshooting Toolbar menu issues. Additionally, practical experience was gained in using Logcat to identify and resolve Android runtime errors.

## **Daily Progress Report Week 3 Day 1**

### **Today's Scrum**

Today, I learned about **Android App Optimization** and the techniques used to improve an application's overall quality and performance. I studied how to optimize an Android app by reducing the app size, improving responsiveness and performance, enhancing security, minimizing unnecessary lines of code, and understanding the importance of **Dependency Injection** for writing modular, maintainable, and testable code.

---

### **Project:** Notepad App (Kotlin + XML + SQLite)

### **Objectives**

* Enhance the basic Notepad application.
* Implement local data storage using SQLite.
* Explore additional features such as editing, deleting, and adding dates to notes.

### **Work Completed**

#### **1. SQLite Database Integration**

* Learned the basics of SQLite in Android.
* Created a `DatabaseHelper` class by extending `SQLiteOpenHelper`.
* Created a `notes` table with the following columns:

  * `id`
  * `title`
  * `description`
* Implemented CRUD (Create, Read, Update, Delete) operations:

  * Insert Note
  * Retrieve All Notes
  * Update Note
  * Delete Note

#### **2. Data Persistence**

* Replaced temporary `ArrayList` storage with SQLite database storage.
* Learned how local databases preserve notes even after closing the application.

#### **3. Calendar Feature Planning**

* Explored adding a **DatePickerDialog** to allow users to select a date while creating or editing a note.
* Updated the database design to include a **date** column for storing the selected date.

#### **4. Edit and Delete Functionality**

* Planned the implementation of:

  * Edit existing notes.
  * Delete notes with a confirmation dialog.
* Understood the flow of passing data between activities for updating notes.

#### **5. Android Concepts Learned**

* SQLite Database
* SQLiteOpenHelper
* Database Tables
* Primary Key (`AUTOINCREMENT`)
* CRUD Operations
* ContentValues
* Cursor
* Local Data Persistence
* DatePickerDialog (Introduction)
* App Constants
* Android App Optimization
* Code Shrinking (`isMinifyEnabled`)
* Dependency Injection (Introduction)
* Android Adapter and ViewHolder concepts

### **Challenges Faced**

* Resolved issues related to updating the SQLite table structure.
* Learned that changing the database schema requires increasing the database version or reinstalling the application.
* Investigated application crashes and understood the importance of using Logcat for debugging.

### **Outcome**

Successfully upgraded the Notepad application from using an in-memory `ArrayList` to a persistent SQLite database and planned additional features such as note editing, deletion, and calendar integration. Additionally, gained an understanding of Android app optimization techniques, dependency injection, and strategies for improving application performance, security, maintainability, and code quality.

## **Daily Progress Report – Day 2**

### **Today's Scrum**

Today, I revised all the fundamental Android development concepts that I learned over the past **two weeks**. I reviewed Android project structure, XML layouts, Kotlin fundamentals, Activities, Intents, RecyclerView, Adapter, ViewHolder, SQLite, CRUD operations, Material Design components, Android app optimization, dependency injection, function overriding, and debugging techniques to strengthen my understanding before moving on to more advanced Android concepts.

---

## **Objectives**

* Revise all Android development concepts learned during the first two weeks.
* Strengthen understanding of Android application architecture and development workflow.
* Review the implementation of the Notepad application.

---

## **Topics Revised**

### **1. Android Fundamentals**

* Android Project Structure
* AndroidManifest.xml
* Gradle Build Files
* Resource Management (`layout`, `drawable`, `mipmap`, `values`)

### **2. Kotlin Basics**

* Variables (`val` & `var`)
* Data Types
* Functions
* Classes and Objects
* Constructors
* Constructor Overloading
* Inheritance
* Function Overriding
* Collections (`ArrayList`)
* Data Classes

### **3. XML Layouts**

* ConstraintLayout
* LinearLayout
* ScrollView
* NestedScrollView
* CoordinatorLayout
* AppBarLayout

### **4. UI Components**

* TextView
* EditText
* TextInputLayout
* TextInputEditText
* Button
* MaterialButton
* ImageView
* RecyclerView
* CardView
* MaterialToolbar

### **5. Android Components**

* Activities
* Activity Lifecycle (`onCreate`)
* Intents
* Passing Data Between Activities
* Toolbar Navigation

### **6. RecyclerView**

* RecyclerView
* Adapter
* ViewHolder
* Item Layout
* Data Binding
* LinearLayoutManager

### **7. SQLite Database**

* SQLiteOpenHelper
* Database Creation
* Tables and Columns
* Primary Key (`AUTOINCREMENT`)
* CRUD Operations
* ContentValues
* Cursor
* Local Data Persistence

### **8. Material Design**

* Material Components
* TextInputLayout
* Material Toolbar
* Material Buttons

### **9. Android App Optimization**

* Improving application performance
* Reducing APK size
* Code optimization
* `isMinifyEnabled`
* Code shrinking and optimization
* Security improvements
* Dependency Injection (Introduction)

### **10. Debugging**

* Understanding compiler errors
* Reading Logcat
* Resolving runtime crashes
* Fixing unresolved references

---

## **Project Revision**

### **Notepad Application**

Reviewed the complete application workflow:

* Add Notes
* Display Notes using RecyclerView
* Edit Notes
* Delete Notes
* Store Notes in SQLite
* Calendar (DatePicker) Integration

---

## **Concepts Reinforced**

* Object-Oriented Programming (OOP)
* Inheritance
* Function Overriding
* Constructor Overloading
* RecyclerView Architecture
* Adapter & ViewHolder
* SQLite Database Management
* CRUD Operations
* Android UI Design
* Material Design Components
* Android App Optimization
* Dependency Injection (Basic Understanding)

---

## **Outcome**

Today's revision helped reinforce the Android development fundamentals learned over the past two weeks. Revisiting these concepts improved my understanding of Android application structure, UI development, Kotlin programming, local database management, RecyclerView architecture, and app optimization techniques. This revision has strengthened my foundation and prepared me to move forward with more advanced Android topics and projects.

## **Week 3 - Day 3 Report**

**Date:** Week 3 - Day 3

### **Tasks Completed**

Today, I worked on the documentation of the **Doctor Appointment Android Application**. I prepared detailed project documentation covering the application's purpose, features, functional requirements, non-functional requirements, technology stack, project architecture, modules, and development plan. The documentation was created to provide a clear roadmap for the implementation of the project.

### **Concepts Learned**

* Software project documentation
* Functional and Non-Functional Requirements
* Android application architecture planning
* Feature and module planning
* Documentation best practices

### **Outcome**

Completed a comprehensive documentation file for the Doctor Appointment application, which will serve as the foundation for the development phase.

---

## **Week 3 - Day 4 Report**

**Date:** Week 3 - Day 4

### **Tasks Completed**

Today, I started the UI implementation of the **Doctor Appointment Android Application**. I designed and developed the following authentication screens using **XML** and **Kotlin**:

* Splash Screen
* Onboarding Screens (3 Pages)
* Login Screen
* Sign Up Screen
* Forgot Password Screen

In addition, I configured **Firebase** for the project by connecting the Android application with Firebase and preparing it for Authentication and other Firebase services.

### **Concepts Learned**

* Android Authentication UI Design
* Multi-page Onboarding using ViewPager2
* Material Design Components
* Firebase Project Setup
* Firebase Authentication Integration
* Google Services Configuration
* Android Navigation between Authentication Screens

### **Technologies Used**

* Kotlin
* XML
* Android Studio
* Firebase Authentication
* Firebase Console

### **Outcome**

Successfully completed the complete authentication UI (Onboarding, Login, Sign Up, Forgot Password) and configured Firebase for the Doctor Appointment application, making the project ready for authentication implementation.
## **Week 3 - Day 5 Report**

**Date:** Week 3 - Day 5

### **Tasks Completed**

Today, I continued developing the **Doctor Appointment Android Application** by designing and implementing the main user interface screens. I created the following screens using **XML** and **Kotlin**:

* Home Screen
* Appointments Screen
* Chat Screen
* Find Doctor Screen
* Rate Doctor Screen
* Settings Screen
* Profile Screen

Each screen was designed with a clean, modern, and user-friendly interface while maintaining a consistent theme throughout the application. I also implemented navigation between these screens to improve the overall user experience.

### **Concepts Learned**

* Multi-screen Android application development
* Material Design UI principles
* Android Navigation Component
* RecyclerView for dynamic lists
* CardView and ConstraintLayout
* Bottom Navigation implementation
* User profile and settings UI design
* Consistent UI/UX design practices

### **Technologies Used**

* Kotlin
* XML
* Android Studio
* Material Design Components
* Navigation Component

### **Outcome**

Successfully developed the core user interface of the Doctor Appointment application, including the Home, Appointments, Chat, Find Doctor, Rate Doctor, Settings, and Profile screens. The application now has a complete primary navigation flow and is ready for implementing backend functionality and real-time features.
Sure! Here's your **Week 3 Day 5 / Yesterday's Report** based only on what you learned yesterday (not today's onboarding work).

---

# **Android Development Internship – Daily Report**

## **Week 4 – Day 1**

### **Today's Scrum**

Today, I learned about several important Android application optimization and security concepts that are essential for developing production-ready applications. I explored **Edge-to-Edge UI**, **ProGuard**, **R8**, **API Security**, and **API Gateway**, understanding how these technologies improve application performance, security, and user experience.

### **Topics Learned**

#### **1. Edge-to-Edge UI**

* Learned how to create a modern full-screen Android interface where the app content extends behind the system status bar and navigation bar.
* Understood how Edge-to-Edge design provides a cleaner, immersive, and more professional user experience while properly handling system insets.

#### **2. ProGuard**

* Studied the purpose of ProGuard in Android applications.
* Learned that ProGuard helps secure applications by:

  * Obfuscating source code.
  * Removing unused classes and methods.
  * Shrinking the APK size.
  * Making reverse engineering more difficult.

#### **3. R8**

* Learned that **R8** is Android's modern code shrinker and optimizer.
* Understood that it performs:

  * Code shrinking
  * Code optimization
  * Resource optimization
  * Obfuscation
* Learned that R8 improves application performance while reducing APK size.

#### **4. API Security**

* Learned the importance of securing APIs in Android applications.
* Understood security practices such as:

  * Authentication and authorization.
  * Secure communication using HTTPS.
  * Protecting sensitive user information.
  * Preventing unauthorized access to backend services.

#### **5. API Gateway**

* Learned the role of an API Gateway in application architecture.
* Understood that an API Gateway:

  * Acts as a single entry point for client requests.
  * Routes requests to the appropriate backend services.
  * Improves security by handling authentication and request validation.
  * Helps manage traffic and simplifies communication between the mobile app and backend services.

### **Practical Work**

* Improved and refined the UI of multiple screens in the Doctor Appointment application.
* Focused on making the interface cleaner, more organized, and visually consistent by adjusting layouts and screen design.

### **Outcome**

By the end of the day, I gained a better understanding of Android application optimization and security concepts, including Edge-to-Edge UI, ProGuard, R8, API Security, and API Gateway. I also enhanced the user interface of several screens in the Doctor Appointment application, making the overall design more polished and user-friendly.

## **Week 4 – Day 2**

Today, I focused on implementing the **Splash Screen** and **Onboarding Screen** for the Doctor Appointment Android application using **manual coding** in XML and Kotlin. Instead of using templates or third-party libraries, I built each screen from scratch to strengthen my understanding of Android UI development and navigation.


## **Tasks Completed**

### **1. Splash Screen Development**

* Created a custom Splash Screen using **ConstraintLayout**.
* Added the application logo and app name.
* Designed a clean and simple splash screen UI.
* Implemented automatic navigation from Splash Screen to Onboarding Screen using **Handler** and **Looper**.
* Learned the purpose of the `finish()` method to prevent users from returning to the Splash Screen.

---

### **2. Onboarding Screen Development**

* Designed an onboarding flow consisting of **three onboarding pages**.
* Created a reusable **item_onboarding.xml** layout for each onboarding page.
* Used **ViewPager2** to enable horizontal swipe navigation between pages.
* Added **TabLayout** as a page indicator to display the current onboarding page.
* Implemented **Skip**, **Next**, and **Get Started** buttons for navigation.
* Displayed different images, titles, and descriptions for each onboarding page using a reusable layout.

---

## **Concepts Learned**

During today's implementation, I learned and practiced the following Android concepts:

* Splash Screen implementation using XML and Kotlin.
* Activity navigation using **Intent**.
* Delayed screen transition using **Handler** and **Looper**.
* Difference between `startActivity()` and `finish()`.
* ViewPager2 implementation for swipeable screens.
* RecyclerView Adapter and ViewHolder basics.
* Creating reusable layouts using `item_onboarding.xml`.
* Creating a Kotlin **data class** to store onboarding data.
* Working with **ImageView**, **TextView**, and **Button** widgets.
* Understanding **View Binding** and its advantages over `findViewById()`.
* Using **ConstraintLayout** and **LinearLayout** together appropriately.
* Difference between **wrap_content** and **match_parent**.
* Difference between **dp** and **sp** units.
* Purpose of **gravity**, **layout_gravity**, and **scaleType**.
* Understanding Android resource naming conventions.
* Managing drawable resources for onboarding images.

---

## **Challenges Faced**

During development, I encountered several issues and learned how to resolve them:

* Faced **Missing Constraints** errors while designing layouts in ConstraintLayout and learned how to apply proper horizontal and vertical constraints.
* Encountered **Cannot resolve symbol '@drawable/logo'** due to incorrect drawable resource naming and placement.
* Faced **View Binding** errors (`ItemOnboardingBinding` not found) and learned how to enable View Binding in the Gradle configuration.
* Experienced adapter implementation errors while creating the ViewPager2 adapter and understood the correct structure of **Adapter** and **ViewHolder**.
* Resolved XML ID mismatches between layout files and Kotlin code.
* Learned how Android automatically generates Binding classes based on XML file names.
* Understood the importance of proper project structure and correct resource naming conventions.

---

## **Outcome**

By the end of the day, I successfully implemented a functional **Splash Screen** and a **three-page Onboarding Screen** using XML and Kotlin. I gained practical experience with Android navigation, ViewPager2, adapters, View Binding, reusable layouts, and ConstraintLayout while also improving my debugging and problem-solving skills through resolving multiple UI and configuration issues.

# **Daily Internship Report**

**Project:** Doctor Appointment App
**Day 3:** Week 4
### **Tasks Performed**

Today I worked on the **authentication module** of the Doctor Appointment App. I focused on completing the user authentication flow by creating and improving the Login, Forgot Password, and Sign Up screens. During development, I also fixed multiple XML layout, ConstraintLayout, and Activity navigation issues.

### **Work Completed**

* Improved the **Login Screen UI** by adding the application logo and designing the email and password input fields using **Material TextInputLayout**.
* Added a **password visibility toggle** to the password field.
* Implemented **login validation** to check whether the email and password fields are empty before allowing login.
* Added navigation from the **Login Screen** to the **Forgot Password** and **Sign Up** screens.
* Created the **Forgot Password Screen** with:

  * App logo
  * Email input field
  * "Send Reset Link" button
* Created the **Sign Up Screen** containing:

  * Full Name
  * Email
  * Password
  * Confirm Password
  * Role Selection (Doctor / Patient)
  * Sign Up button
  * Login navigation link
* Implemented **Confirm Password validation** to ensure both password fields match.
* Added a **Role Selection** feature using a dropdown menu where users can choose either **Doctor** or **Patient**.
* Fixed XML errors related to:

  * ConstraintLayout constraints
  * Missing IDs
  * Resource linking failures
  * Activity navigation
  * View Binding references
* Configured navigation between all authentication screens:

  * Login → Forgot Password
  * Login → Sign Up
  * Sign Up → Login
* Updated the **AndroidManifest.xml** so the application launches from the **Splash Screen**.

### **Firebase Learning**

Today I also learned how Firebase Authentication will be integrated into the project.

Topics studied:

* Creating a Firebase project
* Connecting an Android application with Firebase
* Registering the Android app using the correct package name
* Downloading and adding the **google-services.json** file
* Enabling **Email/Password Authentication**
* Adding Firebase Authentication dependencies
* Initializing **FirebaseAuth**
* Understanding how to implement:

  * User Registration
  * User Login
  * Password Reset using email
* Learned the complete authentication workflow before integrating it into the application.

### **Challenges Faced**

* Resource linking errors caused by incorrect view IDs.
* ConstraintLayout positioning issues.
* Activity navigation problems between Login, Sign Up, and Forgot Password screens.
* Spinner and role selection implementation issues.
* View Binding reference errors.
* Layout alignment and UI positioning adjustments.

### **Outcome**

By the end of the day, the complete authentication UI flow was successfully prepared. The Login, Forgot Password, and Sign Up screens were connected through navigation, input validations were implemented, and the project was prepared for Firebase Authentication integration.

## Daily Report

**Date:** 06 August 2026
**Project:** Doctor Appointment Application
**Day:** Week 3 – Day 6

 **Firebase integration and Firestore database management**. I explored how Firebase services can be used in Android applications to store and manage user data securely. I also learned the basic workflow of connecting an Android project with Firebase and creating cloud-based databases.

---

## Tasks Completed Today

### 1. Firebase Setup in Android Project

Today, I completed the Firebase setup for the Doctor Appointment application.

Implemented:

* Connected Android project with Firebase.
* Added Firebase configuration file (`google-services.json`).
* Configured Firebase dependencies in Gradle.
* Verified Firebase connection with the application.

Firebase will be used for:

* User authentication.
* Storing user profiles.
* Managing appointments.
* Storing doctor and patient information.

---

### 2. Created Firestore Database

I created and configured a **Cloud Firestore Database** for the application.

Learned:

* Firestore is a NoSQL cloud database.
* Data is stored in the form of:

  * Collections
  * Documents
  * Fields

Planned Firestore structure:

```
Firestore Database

users
 └── userId
      ├── name
      ├── email
      ├── phone
      └── role

doctors
 └── doctorId
      ├── name
      ├── specialization
      ├── availability
      └── rating

appointments
 └── appointmentId
      ├── patientId
      ├── doctorId
      ├── date
      ├── time
      └── status
```

---

## 3. Created Home Screen UI

Today, I completed the design of the **Home Screen** for the Doctor Appointment app.

Home Screen includes:

* User greeting section.
* Profile icon.
* Notification icon.
* Search doctor field.
* Doctor categories.
* Popular doctors section.
* Nearby doctors section.
* Upcoming appointment card.
* Bottom navigation bar.

The screen follows a modern healthcare design:

* Blue and white color theme.
* Rounded cards.
* Clean spacing.
* User-friendly layout.

---

## 4. Created Appointment Details Screen

I also created the **Appointment Details Screen**.

Implemented UI components:

* Doctor information section.
* Doctor profile image.
* Doctor specialization.
* Appointment date and time.
* Patient information.
* Appointment status.
* Booking confirmation details.
* Payment information section.

This screen will later be connected with Firestore to display real appointment data.

---

## Concepts Learned Today

* Firebase project integration.
* Firestore database structure.
* Collections and documents.
* Cloud database management.
* Firebase data storage workflow.
* Designing scalable database structure.
* Connecting UI screens with backend services.

---

## Challenges Faced

* Understanding Firestore database structure and organizing collections properly.
* Managing Firebase configuration and dependencies.
* Planning database fields according to application requirements.

---

## Overall Progress

Today, I successfully completed the **Firebase backend setup**, created the **Firestore database structure**, and developed the **Home Screen and Appointment Details Screen UI** for the Doctor Appointment application. This creates a foundation for connecting frontend screens with real-time backend data.
