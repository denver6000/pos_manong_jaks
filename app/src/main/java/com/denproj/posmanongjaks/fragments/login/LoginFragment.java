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
import com.denproj.posmanongjaks.model.SavedLoginCredentials;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.util.OnLoginSuccessful;
import com.denproj.posmanongjaks.util.OnUpdateUI;
import com.denproj.posmanongjaks.viewModel.LoginFragmentViewmodel;
import com.denproj.posmanongjaks.viewModel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LoginFragment extends Fragment implements OnLoginSuccessful {

    private FragmentLoginBinding binding;
    LoginFragmentViewmodel loginFragmentViewmodel;
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
        this.loginFragmentViewmodel = new ViewModelProvider(requireActivity()).get(LoginFragmentViewmodel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        navController = NavHostFragment.findNavController(this);

        binding.setLoginViewmodel(this.loginFragmentViewmodel);

        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(getChildFragmentManager(), "");



        LoginFragment.this.loginFragmentViewmodel.firebaseLogin(new LoginFragmentViewmodel.IsFirebaseAuthCachePresent() {
            @Override
            public void cachePresent() {
                loadingDialog.dismiss();
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity());
            }

            @Override
            public void cacheAbsent() {
                loadingDialog.dismiss();
                binding.loginActionButton.setOnClickListener(view -> setupLoginButton(LoginFragment.this));
            }
        });


//        PingUtil.isInternetReachable(requireContext(), new OnUpdateUI<Boolean>() {
//            @Override
//            public void onSuccess(Boolean result) {
//                mainViewModel.getBooleanLiveData().setValue(result);
//            }
//
//            @Override
//            public void onFail(Exception e) {
//                mainViewModel.getBooleanLiveData().setValue(false);
//            }
//        });

//        mainViewModel.getBooleanLiveData().observe(getViewLifecycleOwner(), isNetAvailable -> {
//            if (viewmodel.isUserSignedIn()) {
//                viewmodel.attemptLogin().thenAcceptAsync(session -> {
//                    redirectAndSaveNetworkStateToSession(session, isNetAvailable);
//                }, ContextCompat.getMainExecutor(requireContext())).exceptionally(throwable -> {
//                    viewmodel.clearLocalDb().thenAccept(unused -> setupLoginButton(isNetAvailable));
//                    return null;
//                });
//            } else {
//                viewmodel.clearLocalDb().thenAccept(unused -> setupLoginButton(isNetAvailable));
//                Toast.makeText(requireContext(), "User not previously signed in, or session expired..", Toast.LENGTH_SHORT).show();
//            }
//        });
        return binding.getRoot();
    }

    public void setupLoginButton(OnLoginSuccessful onLoginSuccessful) {
        loginFragmentViewmodel.emailPasswordLogin(onLoginSuccessful);
    }


    public void setupLoginButton(Boolean isNetAvailable) {
        binding.loginActionButton.setOnClickListener(view -> {
            String email = "denverballesteros7@gmail.com";
            String password = "111111";
            if (true) {
                loginFragmentViewmodel.loginUserAndGetSession(email, password, new OnUpdateUI<Session>() {
                    @Override
                    public void onSuccess(Session session) {
                        if (!isNetAvailable) {
                            Toast.makeText(requireContext(), "Connection Not Available and No Saved Logins", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mainViewModel.sync(session.getBranch().getBranch_id()).thenAccept(unused -> {
                            loginFragmentViewmodel.saveLogin(session).thenAccept(unused1 -> {
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
//        navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity(session.getBranch(), session.getRole(), session.getUser(), isNetAvailable));
    }

    @Override
    public void onLoginSuccess() {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToHomeActivity());
    }

    @Override
    public void onLoginFailed(Exception e) {
        Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }




}