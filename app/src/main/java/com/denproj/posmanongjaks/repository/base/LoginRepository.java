package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.util.OnDataReceived;

public interface LoginRepository {
    void loginUser(String email, String password, OnDataReceived<String> onUserLoggedIn);
    void getUserBranchId(OnDataReceived<User> onBranchIdFetched);
    void getUserRole(String roleId, OnDataReceived<Role> onUserRoleFetched);
}
