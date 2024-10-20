package com.denproj.posmanongjaks.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.denproj.posmanongjaks.model.SavedLoginCredentials;

@Dao
public interface UserDao {

    @Query("SELECT * FROM SavedLoginCredentials")
    SavedLoginCredentials getSavedLoginCredentials();

    @Insert
    void insertLoginCredential(SavedLoginCredentials savedLoginCredentials);

    @Query("DELETE FROM SavedLoginCredentials")
    void deleteAllSavedLogin();

}
