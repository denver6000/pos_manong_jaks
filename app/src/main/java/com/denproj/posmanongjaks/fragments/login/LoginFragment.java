package com.denproj.posmanongjaks.fragments.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denproj.posmanongjaks.databinding.FragmentLoginBinding;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.denproj.posmanongjaks.viewModel.LoginFragmentViewmodel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    LoginFragmentViewmodel viewmodel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentLoginBinding.inflate(inflater, container, false);
        this.viewmodel = new ViewModelProvider(requireActivity()).get(LoginFragmentViewmodel.class);
        NavController navController = NavHostFragment.findNavController(this);
        binding.loginActionButton.setOnClickListener(view -> {
            String email = binding.getEmail();
            String password = binding.getPassword();
            if (true) {
                this.viewmodel.loginUserAndFetchBranchId("denverballesteros7@gmail.com", "Pogiako012004", new OnUpdateUI<String>() {
                    @Override
                    public void onSuccess(String branchId) {
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity(branchId));
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(requireContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }
}