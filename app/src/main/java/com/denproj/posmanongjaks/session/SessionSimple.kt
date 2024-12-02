package com.denproj.posmanongjaks.session

import android.os.Parcel
import android.os.Parcelable

class SessionSimple(val userId: String?,
                    val name: String?,
                    val branchId: String?,
                    val branchName: String?,
                    val roleName: String?) : Parcelable {
    constructor(parcel: Parcel) : this(null, null, null, null, null) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SessionSimple> {
        override fun createFromParcel(parcel: Parcel): SessionSimple {
            return SessionSimple(parcel)
        }

        override fun newArray(size: Int): Array<SessionSimple?> {
            return arrayOfNulls(size)
        }
    }

}
