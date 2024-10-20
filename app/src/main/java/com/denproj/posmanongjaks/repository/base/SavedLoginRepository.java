package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.SavedLoginCredentials;

public interface SavedLoginRepository {

    boolean checkSavedLogin();
    SavedLoginCredentials getSavedLogin();

    void saveLoginCredentials(SavedLoginCredentials savedLoginCredentials);


}
