package com.denproj.posmanongjaks.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SavedLoginCredentials {

    private String email;
    private String password;
    private String branchId;


    @PrimaryKey
    @NonNull
    private String user_id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SavedLoginCredentials(String email, String password, String branchId, @NonNull String user_id) {
        this.email = email;
        this.password = password;
        this.branchId = branchId;
        this.user_id = user_id;
    }

    public SavedLoginCredentials() {
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
