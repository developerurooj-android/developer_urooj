# 📝 Notepad App

A simple **Notepad Android Application** built using **Kotlin** and **XML**. The application allows users to create, view, edit, and delete notes with local data storage using **SQLite**. Users can also assign a date to each note using a calendar.

---

# 📌 Features

- ✅ Add New Notes
- ✅ View All Notes
- ✅ Edit Existing Notes
- ✅ Delete Notes
- ✅ Store Notes using SQLite Database
- ✅ Select Date using Calendar (DatePicker)
- ✅ RecyclerView for displaying notes
- ✅ Material Design UI
- ✅ Toolbar Navigation

---

# 📱 Screens

1. Home Screen
    - Displays all saved notes.
    - RecyclerView to list notes.
    - Add Note button.

2. Add Note Screen
    - Enter Note Title.
    - Enter Note Description.
    - Select Date.
    - Save Note.

3. Edit Note
    - Update existing note.
    - Save changes.

4. Delete Note
    - Delete selected note from the database.

---

# 📂 Project Structure

```
NotepadApp
│
├── java/com/example/notepad/
│   │
│   ├── MainActivity.kt
│   ├── AddNoteActivity.kt
│   ├── Note.kt
│   ├── NoteAdapter.kt
│   ├── DatabaseHelper.kt
│   └── Constants.kt (Optional)
│
├── res/
│   │
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── activity_add_note.xml
│   │   └── item_note.xml
│   │
│   ├── drawable/
│   ├── mipmap/
│   ├── values/
│   │   ├── strings.xml
│   │   ├── colors.xml
│   │   └── themes.xml
│   │
│   └── AndroidManifest.xml
│
└── build.gradle
```

---

# 🛠 Technologies Used

- Kotlin
- XML
- Android SDK
- RecyclerView
- SQLite Database
- Material Components
- CardView
- ConstraintLayout
- Intent
- DatePickerDialog

---

# 🗄 Database

### Database Name

```
notes_db
```

### Table Name

```
notes
```

### Table Structure

| Column | Type |
|---------|------|
| id | INTEGER PRIMARY KEY AUTOINCREMENT |
| title | TEXT |
| description | TEXT |
| date | TEXT |

---

# 📖 App Workflow

```
Application Starts
        │
        ▼
MainActivity
        │
        ▼
Load Notes from SQLite
        │
        ▼
RecyclerView Displays Notes
        │
        ├──────────────┐
        │              │
        ▼              ▼
Add Note          Select Existing Note
        │              │
        ▼              ▼
Enter Details      Edit/Delete Note
        │              │
        ▼              ▼
Save to SQLite     Update/Delete in SQLite
        │              │
        └───────┬──────┘
                ▼
      Refresh RecyclerView
```

---

# 📚 Concepts Used

### Android Fundamentals

- Activities
- Intents
- Android Manifest
- Project Structure
- XML Layouts
- Material Design Components

### UI Components

- RecyclerView
- Adapter
- ViewHolder
- CardView
- Material Toolbar
- TextInputLayout
- Material Button

### Kotlin Concepts

- Classes
- Data Class
- Functions
- ArrayList
- Object-Oriented Programming
- Click Listeners

### SQLite Concepts

- SQLiteOpenHelper
- SQLite Database
- Tables
- Columns
- Primary Key
- CRUD Operations
- ContentValues
- Cursor

### Android Components

- RecyclerView
- Adapter
- ViewHolder
- DatePickerDialog
- Toolbar Navigation

---

# CRUD Operations

## Create

Create and save a new note.

## Read

Display all saved notes.

## Update

Modify an existing note.

## Delete

Remove a note permanently.

---

# 📅 Calendar Feature

The application allows users to:

- Open a DatePicker dialog.
- Select a date.
- Store the selected date in SQLite.
- Display the date with each note.

---

# 🎯 Learning Outcomes

Through this project, the following Android concepts were practiced:

- Android Project Structure
- XML Layout Design
- RecyclerView
- Adapter & ViewHolder
- SQLite Database
- CRUD Operations
- Intents
- Material UI Components
- Local Data Storage
- DatePickerDialog
- Event Handling
- Android Debugging
- Database Management

---

# 🚀 Future Improvements

- Search Notes
- Note Categories
- Dark Mode
- Reminder Notifications
- Voice Notes
- Pin Important Notes
- Share Notes
- Export Notes as PDF
- Backup & Restore
- Room Database Integration

---

# ▶️ How to Run

1. Clone or download the project.
2. Open the project in Android Studio.
3. Sync Gradle files.
4. Build the project.
5. Run the application on an emulator or Android device.

---

# 👩‍💻 Developed By

**Urooj Developer**

Android Developer (Kotlin & XML)

---

# 📄 License

This project is developed for educational and learning purposes.