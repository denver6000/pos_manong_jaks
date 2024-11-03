package com.denproj.posmanongjaks.fragments.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.denproj.posmanongjaks.databinding.FragmentSettingsBinding;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.denproj.posmanongjaks.viewModel.SettingsFragmentViewmodel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {
    HomeActivityViewmodel homeActivityViewmodel;
    SettingsFragmentViewmodel settingsFragmentViewmodel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSettingsBinding binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        this.homeActivityViewmodel = new ViewModelProvider(requireActivity()).get(HomeActivityViewmodel.class);
        this.settingsFragmentViewmodel = new ViewModelProvider(requireActivity()).get(SettingsFragmentViewmodel.class);
        this.homeActivityViewmodel.sessionMutableLiveData.observe(getViewLifecycleOwner(), session -> {
            binding.changePassword.setOnClickListener(view -> {
                settingsFragmentViewmodel.resetPassword(session.isConnectionReachable(), new SettingsFragmentViewmodel.OnPasswordResetFinished() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireActivity(), "Password reset email sent on email address used for this account.", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(requireActivity(), "Connection is not reachable or illegal user session.", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            binding.logOutBtn.setOnClickListener(view -> {
                settingsFragmentViewmodel.logout(new SettingsFragmentViewmodel.OnLogOutSuccessful() {
                    @Override
                    public void onSuccess() {
                        requireActivity().finish();
                    }

                    @Override
                    public void onFail(Exception e) {
                        requireActivity().finish();
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        return binding.getRoot();
    }

}