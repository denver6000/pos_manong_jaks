package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.repository.base.SavedLoginRepository;
import com.denproj.posmanongjaks.room.dao.UserInfoDao;
import com.denproj.posmanongjaks.session.Session;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.inject.Inject;

public class SavedLoginRepositoryImpl implements SavedLoginRepository {

    UserInfoDao userInfoDao;

    @Inject
    public SavedLoginRepositoryImpl(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    @Override
    public CompletableFuture<Session> getSavedInfoToSession() {

        return CompletableFuture.supplyAsync((Supplier<Session>) () -> {

            List<User> users = userInfoDao.getUser();
            if (users.isEmpty()) {
                throw new RuntimeException("No saved user");
            }
            User user = users.get(0);

            List<Branch> branches = userInfoDao.getBranchInfoWithBranchId(user.getBranch_id());
            if (branches.isEmpty()) {
                throw new RuntimeException("No saved branches, invalid cache.");
            }
            Branch branch = branches.get(0);


            List<Role> roles = userInfoDao.getRoleWithRoleName(user.getRole_id());
            if (roles.isEmpty()) {
                throw new RuntimeException("No saved roles, invalid cache.");
            }
            Role role = roles.get(0);

            return new Session(branch, user, role);
        });
    }

    @Override
    public CompletableFuture<Void> saveSessionToLocal(Session session) {
        return CompletableFuture.supplyAsync(() -> {
            Branch branch = session.getBranch();
            Role role = session.getRole();
            User user = session.getUser();

            if (branch == null || role == null || user == null) {
                throw new RuntimeException("A field is blank. Please Re-Login.");
            }

            userInfoDao.saveBranchInfo(branch);
            userInfoDao.saveRoleInfo(role);
            userInfoDao.saveUser(user);
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> clearUserCredentials() {
        return CompletableFuture.supplyAsync(() -> {
            userInfoDao.clearBranch();
            userInfoDao.clearUser();
            userInfoDao.clearRole();
            return null;
        });
    }
}
