package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.util.OnDataReceived;

public interface LoginRepository {
    void loginUser(String email, String password, OnDataReceived<String> onUserLoggedIn);
    void getUserBranchId(OnDataReceived<String> onBranchIdFetched);
}
