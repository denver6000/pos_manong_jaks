package com.denproj.posmanongjaks.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

@Entity
@IgnoreExtraProperties
public class User implements Parcelable {
    private String branch_id;
    private String email;
    private String role;
    private String branch_location;
    private String username;

    @Ignore
    private List<String> branches;
    @PrimaryKey
    @NonNull
    private String user_id;

    public User() {
    }


    protected User(Parcel in) {
        branch_id = in.readString();
        email = in.readString();
        role = in.readString();
        branch_location = in.readString();
        username = in.readString();
        user_id = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole_id() {
        return role;
    }

    public void setRole_id(String role_id) {
        this.role = role_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBranch_location() {
        return branch_location;
    }

    public void setBranch_location(String branch_location) {
        this.branch_location = branch_location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(branch_id);
        parcel.writeString(email);
        parcel.writeString(role);
        parcel.writeString(branch_location);
        parcel.writeString(username);
        parcel.writeString(user_id);
    }
}
