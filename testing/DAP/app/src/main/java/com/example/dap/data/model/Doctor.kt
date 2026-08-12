package com.example.dap.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val hospital: String,
    val rating: Double,
    val reviewsCount: Int,
    val fee: String,
    val photoName: String,
    val about: String = "Dr. $name is a highly experienced $specialty at $hospital with over 10 years of experience in the medical field."
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(specialty)
        parcel.writeString(hospital)
        parcel.writeDouble(rating)
        parcel.writeInt(reviewsCount)
        parcel.writeString(fee)
        parcel.writeString(photoName)
        parcel.writeString(about)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Doctor> {
        override fun createFromParcel(parcel: Parcel): Doctor {
            return Doctor(parcel)
        }

        override fun newArray(size: Int): Array<Doctor?> {
            return arrayOfNulls(size)
        }
    }
}
