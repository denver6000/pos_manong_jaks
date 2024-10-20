package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.SavedLoginCredentials;
import com.denproj.posmanongjaks.repository.base.SavedLoginRepository;
import com.denproj.posmanongjaks.room.dao.UserDao;

import javax.inject.Inject;

public class SavedLoginRepositoryImpl implements SavedLoginRepository {

    private UserDao userDao;

    @Inject
    public SavedLoginRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean checkSavedLogin() {
        return false;
    }

    @Override
    public SavedLoginCredentials getSavedLogin() {
        return userDao.getSavedLoginCredentials();
    }

    @Override
    public void saveLoginCredentials(SavedLoginCredentials savedLoginCredentials) {
        userDao.insertLoginCredential(savedLoginCredentials);
    }
}
