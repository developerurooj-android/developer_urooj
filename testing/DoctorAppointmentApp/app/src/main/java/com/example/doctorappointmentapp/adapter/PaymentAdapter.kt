package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.databinding.ItemPaymentHistoryBinding
import com.example.doctorappointmentapp.model.PaymentModel
import com.example.doctorappointmentapp.utils.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentAdapter(private var list: List<PaymentModel>) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPaymentHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            tvDoctorName.text = "Appointment Ref: ${item.appointmentId.takeLast(8).uppercase()}"
            tvAmount.text = "Rs ${item.amount.toInt()}"
            
            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            tvDate.text = sdf.format(Date(item.createdAt))

            tvStatus.text = item.status
            when (item.status) {
                Constants.PAYMENT_APPROVED -> {
                    tvStatus.setTextColor(root.context.getColor(R.color.success_green))
                }
                Constants.PAYMENT_VERIFICATION -> {
                    tvStatus.setTextColor(root.context.getColor(R.color.warning_orange))
                    tvStatus.text = "Under Verification"
                }
                Constants.PAYMENT_REJECTED -> {
                    tvStatus.setTextColor(root.context.getColor(R.color.error_red))
                }
                else -> {
                    tvStatus.setTextColor(root.context.getColor(R.color.text_secondary))
                }
            }

            val icon = when (item.method.lowercase()) {
                "easypaisa", "jazzcash" -> R.drawable.ic_money
                "debit", "credit" -> R.drawable.ic_payment_card
                else -> R.drawable.ic_money
            }
            ivMethodIcon.setImageResource(icon)
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<PaymentModel>) {
        list = newList
        notifyDataSetChanged()
    }
}
