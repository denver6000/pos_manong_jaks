package com.denproj.posmanongjaks.fragments.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.FragmentSettingsBinding;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.denproj.posmanongjaks.viewModel.SettingsFragmentViewmodel;

public class SettingsFragment extends Fragment {
    HomeActivityViewmodel homeActivityViewmodel;
    SettingsFragmentViewmodel settingsFragmentViewmodel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSettingsBinding binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        this.homeActivityViewmodel = new ViewModelProvider(requireActivity()).get(HomeActivityViewmodel.class);
        this.settingsFragmentViewmodel = new ViewModelProvider(requireActivity()).get(SettingsFragmentViewmodel.class);
        this.homeActivityViewmodel.getIsConnectionReachableLiveData().observe(getViewLifecycleOwner(), isConnectionReachable -> {
            binding.changePassword.setOnClickListener(view -> {
                settingsFragmentViewmodel.resetPassword(isConnectionReachable, new SettingsFragmentViewmodel.OnPasswordResetFinished() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireActivity(), "Password reset email sent on email address used for this account.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(requireActivity(), "Connection is not reachable or illegal user session.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            binding.logOutBtn.setOnClickListener(view -> {
                settingsFragmentViewmodel.clearSavedLoginAndLogout(new SettingsFragmentViewmodel.OnLogOutSuccessful() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireActivity(), "Logged out without errors.", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(requireActivity(), "Logged out with errors.", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    }
                });
            });
        });

        return binding.getRoot();
    }
}