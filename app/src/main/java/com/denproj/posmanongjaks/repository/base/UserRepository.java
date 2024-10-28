package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.User;

import java.util.concurrent.CompletableFuture;

public interface UserRepository {

    CompletableFuture<User> getUserByUid();

}
