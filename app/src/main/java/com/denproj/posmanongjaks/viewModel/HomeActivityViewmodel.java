package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.FirebaseImpl;
import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.repository.base.BranchRepository;
import com.denproj.posmanongjaks.repository.base.RoleRepository;
import com.denproj.posmanongjaks.repository.base.UserRepository;
import com.denproj.posmanongjaks.session.Session;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class HomeActivityViewmodel extends ViewModel {

    BranchRepository branchRepository;
    RoleRepository roleRepository;
    UserRepository userRepository;

    @Inject

    public HomeActivityViewmodel(@FirebaseImpl BranchRepository branchRepository,@FirebaseImpl RoleRepository roleRepository, @FirebaseImpl UserRepository userRepository) {
        this.branchRepository = branchRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public MutableLiveData<Boolean> isConnectionReachableLiveData = new MutableLiveData<>();
    public MutableLiveData<Session> sessionMutableLiveData = new MutableLiveData<>();

    private CompletableFuture<Branch> getBranch(String branchId) {
        return branchRepository.getBranch(branchId);
    }

    private CompletableFuture<User> getUser() {
        return userRepository.getUserByUid();
    }

    private CompletableFuture<Role> getRole(String roleId) {
        return roleRepository.getRole(roleId);
    }

    public CompletableFuture<Session> getSession() {

        CompletableFuture<Session> sessionCompletableFuture = new CompletableFuture<>();
        getUser()
                .thenAccept(user -> {
                    getBranch(user.getBranches().get(0))
                            .exceptionally(throwable -> {
                                sessionCompletableFuture.completeExceptionally(throwable);
                                return null;
                            }).thenAccept(branch -> getRole(user.getRole_id())
                                    .thenAccept(role ->
                                            sessionCompletableFuture.complete(new Session(branch, user, role))));;
                });
        return sessionCompletableFuture;
    }

    public MutableLiveData<Boolean> getIsConnectionReachableLiveData() {
        return isConnectionReachableLiveData;
    }
    public BranchRepository getBranchRepository() {
        return branchRepository;
    }

    public void setBranchRepository(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }



}
