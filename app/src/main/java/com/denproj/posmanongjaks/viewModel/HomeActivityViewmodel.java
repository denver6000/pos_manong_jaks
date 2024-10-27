package com.denproj.posmanongjaks.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.repository.base.BranchRepository;
import com.denproj.posmanongjaks.repository.base.RoleRepository;
import com.denproj.posmanongjaks.session.Session;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class HomeActivityViewmodel extends ViewModel {

    BranchRepository branchRepository;
    RoleRepository roleRepository;

    @Inject
    public HomeActivityViewmodel() {

    }


    public MutableLiveData<Boolean> isConnectionReachableLiveData = new MutableLiveData<>();
    public MutableLiveData<Session> sessionMutableLiveData = new MutableLiveData<>();

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
