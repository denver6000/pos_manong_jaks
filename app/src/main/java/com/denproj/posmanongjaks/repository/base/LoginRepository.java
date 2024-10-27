package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.util.OnDataReceived;

import java.util.concurrent.CompletableFuture;

public interface LoginRepository {
    CompletableFuture<Session> loginUser(String email, String password, OnDataReceived<Session> onUserLoggedIn);
}
