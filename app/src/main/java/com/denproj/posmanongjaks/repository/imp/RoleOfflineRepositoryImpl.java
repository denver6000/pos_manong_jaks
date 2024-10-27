package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.repository.base.RoleRepository;

import java.util.concurrent.CompletableFuture;

public class RoleOfflineRepositoryImpl implements RoleRepository {
    @Override
    public CompletableFuture<Role> getRole(String roleId) {
        return null;
    }
}
