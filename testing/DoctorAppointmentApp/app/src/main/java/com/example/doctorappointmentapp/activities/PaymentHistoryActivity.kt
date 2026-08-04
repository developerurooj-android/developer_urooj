package com.example.doctorappointmentapp.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorappointmentapp.adapter.PaymentAdapter
import com.example.doctorappointmentapp.databinding.ActivityPaymentHistoryBinding
import com.example.doctorappointmentapp.model.PaymentModel
import com.example.doctorappointmentapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PaymentHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentHistoryBinding
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var adapter: PaymentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        loadPaymentHistory()
    }

    private fun setupRecyclerView() {
        adapter = PaymentAdapter(emptyList())
        binding.rvPaymentHistory.apply {
            layoutManager = LinearLayoutManager(this@PaymentHistoryActivity)
            adapter = this@PaymentHistoryActivity.adapter
        }
    }

    private fun loadPaymentHistory() {
        val uid = auth.currentUser?.uid ?: return
        
        binding.progressBar.visibility = View.VISIBLE
        
        db.collection(Constants.PAYMENTS_COLLECTION)
            .whereEqualTo("patientId", uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshots ->
                binding.progressBar.visibility = View.GONE
                val payments = snapshots.toObjects(PaymentModel::class.java)
                
                if (payments.isEmpty()) {
                    binding.layoutEmpty.visibility = View.VISIBLE
                    binding.rvPaymentHistory.visibility = View.GONE
                } else {
                    binding.layoutEmpty.visibility = View.GONE
                    binding.rvPaymentHistory.visibility = View.VISIBLE
                    adapter.updateList(payments)
                }
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                com.google.android.material.snackbar.Snackbar.make(
                    binding.root, "Error loading history: ${it.message}", 
                    com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                ).show()
            }
    }
}
