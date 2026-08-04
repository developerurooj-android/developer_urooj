package com.example.doctorappointmentapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.activities.DoctorDetailsActivity
import com.example.doctorappointmentapp.activities.DoctorListActivity
import com.example.doctorappointmentapp.adapter.CategoryAdapter
import com.example.doctorappointmentapp.adapter.HealthTipAdapter
import com.example.doctorappointmentapp.adapter.PopularDoctorAdapter
import com.example.doctorappointmentapp.databinding.FragmentHomeBinding
import com.example.doctorappointmentapp.model.Category
import com.example.doctorappointmentapp.model.Doctor
import com.example.doctorappointmentapp.model.HealthTip
import com.example.doctorappointmentapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db   by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            binding.layoutHeader.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        loadUserProfile()
        setupCategories()
        setupTopDoctors()
        setupNearbyDoctors()
        setupHealthTips()
        setupUpcomingAppointment()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.cvSearch.setOnClickListener {
            startActivity(Intent(requireContext(), DoctorListActivity::class.java))
        }

        binding.tvSeeAllCategories.setOnClickListener {
            startActivity(Intent(requireContext(), DoctorListActivity::class.java))
        }

        binding.tvSeeAllAppointments.setOnClickListener {
            val intent = Intent()
            intent.setClassName(requireContext(), "com.example.doctorappointmentapp.activities.AppointmentsActivity")
            startActivity(intent)
        }

        binding.tvSeeAllTopDoctors.setOnClickListener {
            startActivity(Intent(requireContext(), DoctorListActivity::class.java))
        }

        binding.tvSeeAllNearby.setOnClickListener {
            startActivity(Intent(requireContext(), DoctorListActivity::class.java))
        }

        binding.tvSeeAllTips.setOnClickListener { }

        binding.btnBannerBook.setOnClickListener {
            startActivity(Intent(requireContext(), DoctorListActivity::class.java))
        }

        binding.cvNotification.setOnClickListener { }

        binding.btnCall.setOnClickListener { }

        binding.btnMessage.setOnClickListener { }
    }

    private fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        db.collection(Constants.USERS_COLLECTION).document(uid).get()
            .addOnSuccessListener { doc ->
                if (_binding == null) return@addOnSuccessListener
                
                val name      = doc.getString("name") ?: "User"
                val firstName = name.split(" ").firstOrNull() ?: name
                val hour      = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val greeting  = when {
                    hour < 12 -> "Good Morning"
                    hour < 17 -> "Good Afternoon"
                    else      -> "Good Evening"
                }
                binding.tvGreetingLabel.text = "$greeting,"
                binding.tvGreeting.text = firstName
                val imageUrl = doc.getString("imageUrl") ?: ""
                if (imageUrl.isNotEmpty()) {
                    Glide.with(this).load(imageUrl)
                        .placeholder(R.drawable.ic_user).circleCrop().into(binding.ivProfile)
                }
            }
    }

    private fun setupCategories() {
        val categories = listOf(
            Category("General",    R.drawable.ic_cat_general),
            Category("Dentist",    R.drawable.ic_cat_dentist),
            Category("Cardiology", R.drawable.ic_cat_heart),
            Category("Skin",       R.drawable.ic_cat_skin),
            Category("Brain",      R.drawable.ic_cat_brain),
            Category("Pediatric",  R.drawable.ic_cat_baby),
            Category("Orthopedic", R.drawable.ic_cat_bone),
            Category("Gynecology", R.drawable.ic_cat_mother)
        )
        binding.rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategories.adapter = CategoryAdapter(categories) { category ->
            startActivity(Intent(requireContext(), DoctorListActivity::class.java))
        }
    }

    private fun setupTopDoctors() {
        val doctors = listOf(
            Doctor("1","James Wilson","Cardiologist",4.8,120,"","",experience="8 years",hospital="City Hospital",fee=1500.0,isOnline=true,isVerified = true),
            Doctor("2","Sarah Ahmed","Dermatologist",4.7,98,"","",experience="5 years",hospital="Skin Care Clinic",fee=1200.0,isOnline=false,isVerified = true),
            Doctor("3","Michael Chen","Neurologist",4.9,200,"","",experience="12 years",hospital="Neuro Center",fee=2000.0,isOnline=true,isVerified = true),
            Doctor("4","Priya Sharma","Pediatrician",4.6,85,"","",experience="7 years",hospital="Kids Hospital",fee=1000.0,isOnline=true,isVerified = false)
        )
        val adapter = PopularDoctorAdapter { doctor ->
            startActivity(Intent(requireContext(), DoctorDetailsActivity::class.java)
                .putExtra(Constants.EXTRA_DOCTOR_ID, doctor.id))
        }
        binding.rvPopularDoctors.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularDoctors.adapter = adapter
        adapter.submitList(doctors)
    }

    private fun setupNearbyDoctors() {
        val doctors = listOf(
            Doctor("5","Amina Khalid","Gynecologist",4.5,60,"","",experience="10 years",hospital="Women Care",fee=1800.0,isOnline=false,isVerified = true),
            Doctor("6","David Smith","Orthopedic",4.4,45,"","",experience="6 years",hospital="Bone & Joint",fee=1300.0,isOnline=true,isVerified = true),
            Doctor("7","Sofia Vergara","Dentist",4.9,310,"","",experience="15 years",hospital="Elite Dental",fee=2500.0,isOnline=true,isVerified = true)
        )
        val adapter = PopularDoctorAdapter { doctor ->
            startActivity(Intent(requireContext(), DoctorDetailsActivity::class.java)
                .putExtra(Constants.EXTRA_DOCTOR_ID, doctor.id))
        }
        binding.rvNearbyDoctors.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvNearbyDoctors.adapter = adapter
        adapter.submitList(doctors)
    }

    private fun setupHealthTips() {
        val tips = listOf(
            HealthTip("1", "Stay Hydrated", "Drink 8 glasses of water daily for better focus and sustained energy throughout your busy day.", R.drawable.ic_notification),
            HealthTip("2", "Sleep Well", "Get 7-8 hours of quality sleep each night to support mental clarity and immune health.", R.drawable.ic_clock),
            HealthTip("3", "Regular Exercise", "A 30-minute daily walk can significantly improve heart health and reduce stress levels.", R.drawable.ic_cat_heart)
        )
        val adapter = HealthTipAdapter()
        binding.rvHealthTips.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHealthTips.adapter = adapter
        adapter.submitList(tips)
    }

    private fun setupUpcomingAppointment() {
        val uid = auth.currentUser?.uid ?: return
        db.collection(Constants.APPOINTMENTS_COLLECTION)
            .whereEqualTo("patientId", uid)
            .whereIn("status", listOf("confirmed", "pending", "Confirmed", "Pending"))
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                if (_binding == null) return@addOnSuccessListener
                if (snapshot.documents.isNotEmpty()) {
                    val appointment = snapshot.documents.first()
                    val doctorId = appointment.getString("doctorId") ?: ""
                    val dateStr  = appointment.getString("date") ?: ""
                    val timeStr  = appointment.getString("time") ?: ""
                    val status   = appointment.getString("status") ?: "Confirmed"

                    if (dateStr.isNotEmpty()) {
                        val parts = dateStr.split(" ")
                        if (parts.size >= 2) {
                            binding.tvApptDate.text = parts[0]
                            binding.tvApptDay.text  = parts[1]
                        }
                    }
                    binding.tvApptTime.text       = "Your Appointment"
                    binding.tvApptTimeDetail.text = timeStr.ifEmpty { "10:00 AM" }
                    binding.tvApptStatus.text     = status.uppercase()

                    if (doctorId.isNotEmpty()) {
                        db.collection(Constants.DOCTORS_COLLECTION).document(doctorId).get()
                            .addOnSuccessListener { doc ->
                                if (_binding == null) return@addOnSuccessListener
                                val name     = doc.getString("name") ?: "Doctor"
                                val specialty = doc.getString("specialty") ?: "Specialist"
                                val hospital = doc.getString("hospital") ?: ""
                                val imageUrl = doc.getString("imageUrl") ?: ""
                                val verified = doc.getBoolean("isVerified") ?: true

                                binding.tvApptDoctorName.text = "Dr. $name"
                                binding.tvApptSpecialty.text  = specialty
                                binding.tvApptHospital.text   = hospital.ifEmpty { "Private Clinic" }
                                binding.ivApptVerified.visibility = if (verified) View.VISIBLE else View.GONE

                                if (imageUrl.isNotEmpty()) {
                                    Glide.with(this).load(imageUrl)
                                        .placeholder(R.drawable.ic_doctor).circleCrop()
                                        .into(binding.ivApptDoctor)
                                }
                            }
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
