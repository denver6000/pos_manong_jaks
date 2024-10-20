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
import com.denproj.posmanongjaks.model.SavedLoginCredentials;
import com.denproj.posmanongjaks.session.SessionManager;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.denproj.posmanongjaks.util.PingUtil;
import com.denproj.posmanongjaks.viewModel.LoginFragmentViewmodel;
import com.denproj.posmanongjaks.viewModel.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    LoginFragmentViewmodel viewmodel;
    MainViewModel mainViewModel;
    NavController navController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentLoginBinding.inflate(inflater, container, false);
        this.viewmodel = new ViewModelProvider(requireActivity()).get(LoginFragmentViewmodel.class);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        navController = NavHostFragment.findNavController(this);

        mainViewModel.getBooleanLiveData().observe(getViewLifecycleOwner(), isNetAvailable -> {
            viewmodel.attemptLogin(new LoginFragmentViewmodel.IsSavedLoginPresent() {
                @Override
                public void onHasSaved(SavedLoginCredentials savedLoginCredentials) {
                    SessionManager.getInstance(savedLoginCredentials.getBranchId());
                    navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity(savedLoginCredentials.getBranchId(), isNetAvailable));
                }

                @Override
                public void onNoSavedLogin() {
                    setupLoginButton(isNetAvailable);
                }
            });
        });

        PingUtil.isInternetReachable(requireContext(), new OnUpdateUI<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mainViewModel.getBooleanLiveData().setValue(false);
            }

            @Override
            public void onFail(Exception e) {
                mainViewModel.getBooleanLiveData().setValue(false);
            }
        });

        return binding.getRoot();
    }


    public void setupLoginButton(Boolean isNetAvailable) {
        binding.loginActionButton.setOnClickListener(view -> {
            String email = "denverballesteros7@gmail.com";
            String password = "Pogiako012004";
            if (true) {
                viewmodel.loginUserAndFetchBranchId(email, password, new OnUpdateUI<String>() {
                    @Override
                    public void onSuccess(String branchId) {
                        SessionManager.getInstance(branchId);
                        mainViewModel.sync();
                        navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity(branchId, isNetAvailable));
                        viewmodel.saveLogin(email, password,  FirebaseAuth.getInstance().getUid(), branchId);
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
    }


}