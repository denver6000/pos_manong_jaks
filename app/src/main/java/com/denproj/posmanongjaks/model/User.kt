package com.denproj.posmanongjaks.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import com.google.firebase.firestore.IgnoreExtraProperties

@Entity
@IgnoreExtraProperties
class User : Parcelable {
    var role: String? = null
    var branch_id: String? = null
    var email: String? = null
    var role_id: String? = null
    var branch_location: String? = null
    var username: String? = null

    @Ignore
    var branches: List<String>? = null
    var user_id: String? = null

    constructor()


    protected constructor(`in`: Parcel) {
        branch_id = `in`.readString()
        email = `in`.readString()
        role_id = `in`.readString()
        branch_location = `in`.readString()
        username = `in`.readString()
        user_id = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(branch_id)
        parcel.writeString(email)
        parcel.writeString(role_id)
        parcel.writeString(branch_location)
        parcel.writeString(username)
        parcel.writeString(user_id)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
