package com.denproj.posmanongjaks.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Branch implements Parcelable {
    private String branch_contact;
    @PrimaryKey
    @NonNull
    private String branch_id;
    private String branch_location;
    private String branch_manager;
    private String branch_name;

    public Branch() {
    }

    public Branch(@NonNull String branch_id, String branch_location, String branch_contact, String branch_manager, String branch_name) {
        this.branch_id = branch_id;
        this.branch_location = branch_location;
        this.branch_contact = branch_contact;
        this.branch_manager = branch_manager;
        this.branch_name = branch_name;
    }

    protected Branch(Parcel in) {
        branch_contact = in.readString();
        branch_id = in.readString();
        branch_location = in.readString();
        branch_manager = in.readString();
        branch_name = in.readString();
    }

    public static final Creator<Branch> CREATOR = new Creator<Branch>() {
        @Override
        public Branch createFromParcel(Parcel in) {
            return new Branch(in);
        }

        @Override
        public Branch[] newArray(int size) {
            return new Branch[size];
        }
    };

    public String getBranch_contact() {
        return branch_contact;
    }

    public void setBranch_contact(String branch_contact) {
        this.branch_contact = branch_contact;
    }

    @NonNull
    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(@NonNull String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_location() {
        return branch_location;
    }

    public void setBranch_location(String branch_location) {
        this.branch_location = branch_location;
    }

    public String getBranch_manager() {
        return branch_manager;
    }

    public void setBranch_manager(String branch_manager) {
        this.branch_manager = branch_manager;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(branch_contact);
        parcel.writeString(branch_id);
        parcel.writeString(branch_location);
        parcel.writeString(branch_manager);
        parcel.writeString(branch_name);
    }
}
