package com.example.doctorappointmentapp.utils

object Constants {
    // ── Firestore collections ─────────────────────────────────────
    const val USERS_COLLECTION         = "Users"
    const val APPOINTMENTS_COLLECTION  = "Appointments"
    const val DOCTORS_COLLECTION       = "Doctors"
    const val REVIEWS_COLLECTION       = "Reviews"
    const val CHATS_COLLECTION         = "Chats"
    const val MESSAGES_SUBCOLLECTION   = "Messages"
    const val PAYMENTS_COLLECTION      = "Payments"
    const val PRESCRIPTIONS_COLLECTION = "Prescriptions"
    const val REPORTS_COLLECTION       = "MedicalReports"

    // ── Firebase Storage paths ────────────────────────────────────
    const val STORAGE_PROFILE_PICS   = "profile_pictures"
    const val STORAGE_REPORTS        = "medical_reports"
    const val STORAGE_PAYMENTS       = "payment_screenshots"
    const val STORAGE_PRESCRIPTIONS  = "prescriptions"
    const val STORAGE_CHAT_FILES     = "chat_files"

    // ── Shared-Prefs ─────────────────────────────────────────────
    const val PREFS_NAME   = "app_prefs"
    const val IS_FIRST_TIME = "isFirstTime"

    // ── Roles ─────────────────────────────────────────────────────
    const val ROLE_PATIENT = "Patient"
    const val ROLE_DOCTOR  = "Doctor"
    const val ROLE_ADMIN   = "Admin"

    // ── Genders ───────────────────────────────────────────────────
    const val GENDER_MALE   = "Male"
    const val GENDER_FEMALE = "Female"
    const val GENDER_OTHER  = "Other"

    // ── Appointment statuses ──────────────────────────────────────
    const val STATUS_UPCOMING  = "Upcoming"
    const val STATUS_ACCEPTED  = "Accepted"
    const val STATUS_PENDING   = "Pending"
    const val STATUS_CONFIRMED = "Confirmed"
    const val STATUS_COMPLETED = "Completed"
    const val STATUS_CANCELLED = "Cancelled"

    // ── Payment statuses ──────────────────────────────────────────
    const val PAYMENT_UNPAID       = "Unpaid"
    const val PAYMENT_PENDING      = "Pending"
    const val PAYMENT_VERIFICATION = "UnderVerification"
    const val PAYMENT_APPROVED     = "Approved"
    const val PAYMENT_REJECTED     = "Rejected"

    // ── Intent extras ─────────────────────────────────────────────
    const val EXTRA_DOCTOR_ID       = "doctor_id"
    const val EXTRA_APPOINTMENT_ID  = "appointment_id"
    const val EXTRA_PAYMENT_ID      = "payment_id"
    const val EXTRA_PRESCRIPTION_ID = "prescription_id"
    const val EXTRA_RECEIVER_ID     = "receiver_id"
    const val EXTRA_RECEIVER_NAME   = "receiver_name"
    const val EXTRA_RECEIVER_IMAGE  = "receiver_image"
    const val EXTRA_DOCTOR_NAME     = "doctor_name"
    const val EXTRA_CALL_TYPE       = "call_type"   // "audio" | "video"
}
