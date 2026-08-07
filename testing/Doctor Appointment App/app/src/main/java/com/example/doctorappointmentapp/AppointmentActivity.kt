package com.example.doctorappointmentapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.databinding.ActivityAppointmentBinding
import com.example.doctorappointmentapp.databinding.DialogAddEditAppointmentBinding

class AppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var adapter: AppointmentAdapter
    private val repository: AppointmentRepository = FirestoreAppointmentRepository()
    private var currentFilter = "All"
    private var allAppointments = listOf<Appointment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeAppointments()
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        adapter = AppointmentAdapter(
            appointmentList = emptyList(),
            onEdit = { appointment -> showAddEditDialog(appointment) },
            onDelete = { appointment -> showDeleteConfirmation(appointment) },
            onStatusChange = { appointment, newStatus ->
                val updated = appointment.copy(status = newStatus)
                repository.updateAppointment(updated) { success ->
                    if (success) {
                        Toast.makeText(this, "Status updated to $newStatus", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        binding.rvAppointments.layoutManager = LinearLayoutManager(this)
        binding.rvAppointments.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnSeedData.setOnClickListener {
            (repository as? FirestoreAppointmentRepository)?.seedMockData { success ->
                if (success) {
                    Toast.makeText(this, "Mock data seeded successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to seed mock data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        binding.fabAddAppointment.setOnClickListener {
            showAddEditDialog(null)
        }

        binding.tabUpcoming.setOnClickListener { filterAppointments("Upcoming") }
        binding.tabCompleted.setOnClickListener { filterAppointments("Completed") }
        binding.tabCancelled.setOnClickListener { filterAppointments("Cancelled") }
        binding.tabAll.setOnClickListener { filterAppointments("All") }
    }

    private fun filterAppointments(status: String) {
        currentFilter = status
        Toast.makeText(this, "Filtering by: $status", Toast.LENGTH_SHORT).show()
        applyFilter()
    }

    private fun observeAppointments() {
        repository.getAppointments(
            onResult = { list ->
                allAppointments = list
                applyFilter()
            },
            onError = { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun applyFilter() {
        val filteredList = if (currentFilter == "All") {
            allAppointments
        } else {
            allAppointments.filter { it.status == currentFilter }
        }
        adapter.updateList(filteredList)
    }

    private fun showAddEditDialog(appointment: Appointment?) {
        val dialogBinding = DialogAddEditAppointmentBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setTitle(if (appointment == null) "New Appointment" else "Edit Appointment")

        if (appointment != null) {
            dialogBinding.etDoctorName.setText(appointment.doctorName)
            dialogBinding.etSpecialization.setText(appointment.specialization)
            dialogBinding.etDate.setText(appointment.date)
            dialogBinding.etTime.setText(appointment.time)
            dialogBinding.etFee.setText(appointment.consultationFee.toString())
        }

        val dialog = builder.create()

        dialogBinding.btnSave.setOnClickListener {
            val name = dialogBinding.etDoctorName.text.toString()
            val spec = dialogBinding.etSpecialization.text.toString()
            val date = dialogBinding.etDate.text.toString()
            val time = dialogBinding.etTime.text.toString()
            val fee = dialogBinding.etFee.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isNotEmpty() && spec.isNotEmpty()) {
                if (appointment == null) {
                    val newAppointment = Appointment(
                        doctorName = name,
                        specialization = spec,
                        date = date,
                        time = time,
                        status = "Upcoming",
                        consultationFee = fee,
                        profileImage = "https://img.freepik.com/free-photo/doctor-offering-medical-tele-consultation_23-2149329007.jpg"
                    )
                    repository.addAppointment(newAppointment) { success ->
                        if (success) {
                            Toast.makeText(this, "Appointment Added", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "Failed to Add", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val updated = appointment.copy(
                        doctorName = name,
                        specialization = spec,
                        date = date,
                        time = time,
                        consultationFee = fee
                    )
                    repository.updateAppointment(updated) { success ->
                        if (success) {
                            Toast.makeText(this, "Appointment Updated", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun showDeleteConfirmation(appointment: Appointment) {
        AlertDialog.Builder(this)
            .setTitle("Delete Appointment")
            .setMessage("Are you sure you want to delete this appointment?")
            .setPositiveButton("Delete") { _, _ ->
                repository.deleteAppointment(appointment.appointmentId) { success ->
                    if (success) {
                        Toast.makeText(this, "Appointment deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_appointments
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, PatientHomeActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_appointments -> true
                R.id.nav_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
