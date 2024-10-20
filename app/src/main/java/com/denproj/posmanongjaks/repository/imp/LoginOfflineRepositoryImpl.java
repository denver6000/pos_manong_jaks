package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.repository.base.LoginRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;

public class LoginOfflineRepositoryImpl implements LoginRepository {
    @Override
    public void loginUser(String email, String password, OnDataReceived<String> onUserLoggedIn) {

    }

    @Override
    public void getUserBranchId(OnDataReceived<User> onBranchIdFetched) {

    }

    @Override
    public void getUserRole(String roleId, OnDataReceived<Role> onUserRoleFetched) {

    }
}
