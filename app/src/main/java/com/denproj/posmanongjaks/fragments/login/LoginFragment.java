package com.denproj.posmanongjaks.fragments.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.denproj.posmanongjaks.databinding.FragmentLoginBinding;
import com.denproj.posmanongjaks.dialog.LoadingDialog;
import com.denproj.posmanongjaks.session.SessionSimple;
import com.denproj.posmanongjaks.util.OnLoginSuccessful;
import com.denproj.posmanongjaks.viewModel.LoginFragmentViewmodel;
import com.denproj.posmanongjaks.viewModel.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LoginFragment extends Fragment implements OnLoginSuccessful {

    private FragmentLoginBinding binding;
    LoginFragmentViewmodel loginFragmentViewmodel;
    MainViewModel mainViewModel;
    NavController navController;
    LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentLoginBinding.inflate(inflater, container, false);
        this.loginFragmentViewmodel = new ViewModelProvider(requireActivity()).get(LoginFragmentViewmodel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        navController = NavHostFragment.findNavController(this);

        binding.setLoginViewmodel(this.loginFragmentViewmodel);

        this.loadingDialog = new LoadingDialog("Detecting Saved Login", "Loading");


        binding.loginActionButton.setOnClickListener(view -> setupLoginButton(LoginFragment.this));
        return binding.getRoot();
    }

    public void setupLogin() {
        loadingDialog.show(getChildFragmentManager(), "");
        LoginFragment.this.loginFragmentViewmodel.firebaseLogin(new LoginFragmentViewmodel.IsFirebaseAuthCachePresent() {
            @Override
            public void cachePresent(@NonNull SessionSimple sessionSimple) {
                Toast.makeText(requireContext(), "Previous Login Detected", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity(sessionSimple.getUserId(), sessionSimple.getName(), sessionSimple.getBranchName(), sessionSimple.getBranchId(), sessionSimple.getRoleName()));
            }

            @Override
            public void cacheAbsent() {
                loadingDialog.dismiss();
            }
        });
    }

    public void setupLoginButton(OnLoginSuccessful onLoginSuccessful) {
        loginFragmentViewmodel.emailPasswordLogin(onLoginSuccessful);
    }

    @Override
    public void onLoginSuccess(SessionSimple sessionSimple) {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity(sessionSimple.getUserId(), sessionSimple.getName(), sessionSimple.getBranchName(), sessionSimple.getBranchId(), sessionSimple.getRoleName()));
    }

    @Override
    public void onLoginFailed(Exception e) {
        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        setupLogin();
        super.onResume();
    }
}