package com.example.studentsApp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val navController = findNavController(R.id.nav_host_fragment)

        when (item.itemId) {

            R.id.menu_add -> {
                navController.navigate(R.id.addStudentFragment)
                return true
            }

            R.id.menu_edit -> {
                navController.navigate(R.id.studentDetailsFragment)
                return true
            }

            R.id.menu_about -> {
                navController.navigate(R.id.aboutFragment)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}