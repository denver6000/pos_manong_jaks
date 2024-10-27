package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.session.Session;

import java.util.concurrent.CompletableFuture;

public interface SavedLoginRepository {

    CompletableFuture<Session> getSavedInfoToSession();
    CompletableFuture<Void> saveSessionToLocal(Session session);

    CompletableFuture<Void> clearUserCredentials();

}
