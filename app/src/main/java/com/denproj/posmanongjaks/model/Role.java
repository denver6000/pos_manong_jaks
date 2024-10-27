package com.denproj.posmanongjaks.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Role implements Parcelable {
    private String role_name;

    @PrimaryKey
    @NonNull
    private String role_id;

    public Role() {
    }

    public Role(String roleId, String role_name) {
        this.role_id = roleId;
        this.role_name = role_name;
    }

    protected Role(Parcel in) {
        role_name = in.readString();
        role_id = in.readString();
    }

    public static final Creator<Role> CREATOR = new Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel in) {
            return new Role(in);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(role_name);
        parcel.writeString(role_id);
    }
}
