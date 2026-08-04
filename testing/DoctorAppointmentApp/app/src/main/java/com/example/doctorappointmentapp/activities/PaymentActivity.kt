package com.example.doctorappointmentapp.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.doctorappointmentapp.databinding.ActivityPaymentBinding
import com.example.doctorappointmentapp.model.PaymentModel
import com.example.doctorappointmentapp.utils.Constants
import com.example.doctorappointmentapp.utils.Resource
import com.example.doctorappointmentapp.viewmodel.AppointmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private val auth    by lazy { FirebaseAuth.getInstance() }
    private val db      by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }

    private var appointmentId = ""
    private var doctorId      = ""
    private var amount        = 0.0
    private var selectedMethod = ""
    private var screenshotUri: Uri? = null

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            screenshotUri = it
            binding.ivScreenshot.setImageURI(it)
            binding.ivScreenshot.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appointmentId = intent.getStringExtra(Constants.EXTRA_APPOINTMENT_ID) ?: ""
        loadAppointmentDetails()

        setupPaymentMethods()
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnUploadScreenshot.setOnClickListener { imagePicker.launch("image/*") }
        binding.btnPay.setOnClickListener { submitPayment() }
    }

    private fun loadAppointmentDetails() {
        if (appointmentId.isEmpty()) return
        db.collection(Constants.APPOINTMENTS_COLLECTION).document(appointmentId).get()
            .addOnSuccessListener { doc ->
                doctorId = doc.getString("doctorId") ?: ""
                binding.tvDoctorName.text   = doc.getString("doctorName") ?: "Doctor"
                binding.tvSpecialty.text    = doc.getString("doctorSpecialty") ?: "Consultation"
                binding.tvDateTime.text     = "${doc.getString("date")} • ${doc.getString("time")}"
                amount = doc.getDouble("fee") ?: 0.0
                binding.tvFee.text = "Rs ${amount.toInt()}"
                binding.tvTotalFee.text = "Rs ${amount.toInt()}"
            }
    }

    private fun setupPaymentMethods() {
        // Initialize Methods
        binding.btnEasypaisa.apply {
            tvTitle.text = "Easypaisa"
            tvDesc.text  = "Instant Mobile Transfer"
            ivIcon.setImageResource(com.example.doctorappointmentapp.R.drawable.ic_money)
            root.setOnClickListener { updateMethodUI("easypaisa") }
        }
        binding.btnJazzcash.apply {
            tvTitle.text = "JazzCash"
            tvDesc.text  = "Mobile Wallet"
            ivIcon.setImageResource(com.example.doctorappointmentapp.R.drawable.ic_money)
            root.setOnClickListener { updateMethodUI("jazzcash") }
        }
        binding.btnDebit.apply {
            tvTitle.text = "Debit Card"
            tvDesc.text  = "Secure Payment via Visa/Mastercard"
            ivIcon.setImageResource(com.example.doctorappointmentapp.R.drawable.ic_payment_card)
            root.setOnClickListener { updateMethodUI("debit") }
        }
        binding.btnCredit.apply {
            tvTitle.text = "Credit Card"
            tvDesc.text  = "Credit Card Payment"
            ivIcon.setImageResource(com.example.doctorappointmentapp.R.drawable.ic_payment_card)
            root.setOnClickListener { updateMethodUI("credit") }
        }
    }

    private fun updateMethodUI(method: String) {
        selectedMethod = method
        
        val methods = listOf(
            "easypaisa" to binding.btnEasypaisa,
            "jazzcash"  to binding.btnJazzcash,
            "debit"     to binding.btnDebit,
            "credit"    to binding.btnCredit
        )

        methods.forEach { (key, binder) ->
            val isSelected = key == method
            binder.radioButton.isChecked = isSelected
            binder.root.strokeColor = getColor(if (isSelected) 
                com.example.doctorappointmentapp.R.color.medical_blue 
                else com.example.doctorappointmentapp.R.color.gray_200)
            binder.root.strokeWidth = if (isSelected) 4 else 2
        }

        val needsScreenshot = method == "easypaisa" || method == "jazzcash"
        binding.layoutScreenshot.visibility = if (needsScreenshot) View.VISIBLE else View.GONE
    }

    private fun submitPayment() {
        if (selectedMethod.isEmpty()) {
            com.google.android.material.snackbar.Snackbar
                .make(binding.root, "Please select a payment method", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT).show()
            return
        }
        val uid = auth.currentUser?.uid ?: return
        binding.btnPay.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        fun savePayment(screenshotUrl: String) {
            val payment = PaymentModel(
                appointmentId = appointmentId,
                patientId     = uid,
                doctorId      = doctorId,
                amount        = amount,
                method        = selectedMethod,
                screenshotUrl = screenshotUrl,
                status        = if (selectedMethod in listOf("easypaisa","jazzcash"))
                    Constants.PAYMENT_VERIFICATION else Constants.PAYMENT_PENDING
            )
            viewModel.submitPayment(payment)
        }

        if (screenshotUri != null && selectedMethod in listOf("easypaisa","jazzcash")) {
            val ref = storage.reference.child("${Constants.STORAGE_PAYMENTS}/$uid/${System.currentTimeMillis()}.jpg")
            ref.putFile(screenshotUri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url -> savePayment(url.toString()) }
            }.addOnFailureListener { savePayment("") }
        } else {
            savePayment("")
        }
    }

    private fun observeViewModel() {
        viewModel.paymentState.observe(this) { res ->
            when (res) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.layoutSuccess.visibility = View.VISIBLE
                    binding.layoutPayment.visibility = View.GONE
                    binding.root.postDelayed({ finish() }, 2000)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnPay.isEnabled = true
                    com.google.android.material.snackbar.Snackbar
                        .make(binding.root, res.message, com.google.android.material.snackbar.Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        observeViewModel()
    }
}
