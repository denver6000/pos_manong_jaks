package com.denproj.posmanongjaks.repository.base;

import com.denproj.posmanongjaks.model.Role;

import java.util.concurrent.CompletableFuture;

public interface RoleRepository {
    CompletableFuture<Role> getRole(String roleId);
}
