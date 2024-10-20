package com.denproj.posmanongjaks.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    private String branch_id;
    private String email;
    private String role;
    private String branch_location;
    private String username;
    @PrimaryKey
    private String user_id;

    public User() {
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
}
