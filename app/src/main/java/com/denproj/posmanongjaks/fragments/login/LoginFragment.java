package com.denproj.posmanongjaks.fragments.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.denproj.posmanongjaks.databinding.FragmentLoginBinding;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.denproj.posmanongjaks.util.PingUtil;
import com.denproj.posmanongjaks.viewModel.LoginFragmentViewmodel;
import com.denproj.posmanongjaks.viewModel.MainViewModel;

import java.util.function.Consumer;
import java.util.function.Function;

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

        PingUtil.isInternetReachable(requireContext(), new OnUpdateUI<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                mainViewModel.getBooleanLiveData().setValue(result);
            }

            @Override
            public void onFail(Exception e) {
                mainViewModel.getBooleanLiveData().setValue(false);
            }
        });

        mainViewModel.getBooleanLiveData().observe(getViewLifecycleOwner(), isNetAvailable -> {
            if (viewmodel.isUserSignedIn()) {
                viewmodel.attemptLogin().thenAcceptAsync(session -> {
                    redirectAndSaveNetworkStateToSession(session, isNetAvailable);
                }, ContextCompat.getMainExecutor(requireContext())).exceptionally(throwable -> {
                    viewmodel.clearLocalDb().thenAccept(unused -> setupLoginButton(isNetAvailable));
                    return null;
                });
            } else {
                viewmodel.clearLocalDb().thenAccept(unused -> setupLoginButton(isNetAvailable));
                Toast.makeText(requireContext(), "User not previously signed in, or session expired..", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }


    public void setupLoginButton(Boolean isNetAvailable) {
        binding.loginActionButton.setOnClickListener(view -> {
            String email = binding.getEmail();
            String password = binding.getPassword();
            if (email != null && password != null && !password.isEmpty() && !email.isEmpty()) {
                viewmodel.loginUserAndGetSession(email, password, new OnUpdateUI<Session>() {
                    @Override
                    public void onSuccess(Session session) {
                        if (!isNetAvailable) {
                            Toast.makeText(requireContext(), "Connection Not Available and No Saved Logins", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mainViewModel.sync(session.getBranch().getBranch_id()).thenAccept(unused -> {
                            viewmodel.saveLogin(session).thenAccept(unused1 -> {
                                redirectAndSaveNetworkStateToSession(session, isNetAvailable);
                            });
                        }).exceptionally(throwable -> {
                            Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            return null;
                        });
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

    public void redirectAndSaveNetworkStateToSession(Session session, boolean isNetAvailable) {
        session.setConnectionReachable(isNetAvailable);
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity(session.getBranch(), session.getRole(), session.getUser(), isNetAvailable));
    }


}