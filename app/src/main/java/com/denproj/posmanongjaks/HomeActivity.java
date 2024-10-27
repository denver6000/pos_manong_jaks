package com.denproj.posmanongjaks;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.denproj.posmanongjaks.databinding.ActivityHomeBinding;
import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.hilt.qualifier.OnlineImpl;
import com.denproj.posmanongjaks.repository.base.BranchRepository;
import com.denproj.posmanongjaks.repository.base.RoleRepository;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel;
import com.google.android.material.navigation.NavigationView;
import android.Manifest;

import java.util.Map;
import java.util.function.BiConsumer;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private HomeActivityViewmodel homeActivityViewmodel;

    ActivityResultLauncher<String[]> requestBluetoothPerms = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> o) {
            o.forEach((permission, isAllowed) -> {
                if (isAllowed) {
                    if (shouldShowRequestPermissionRationale(permission)) {
                        showDialog(permission).show();
                    }
                }
            });
        }
    });


    @Inject
    @OnlineImpl
    BranchRepository branchOnlineRepository;

    @Inject
    @OfflineImpl
    BranchRepository branchOfflineRepository;

    @Inject
    @OnlineImpl
    RoleRepository roleOnlineRepository;

    @Inject
    @OfflineImpl
    RoleRepository roleOfflineRepository;

    String[] permissions = new String[] {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HomeActivityArgs args = HomeActivityArgs.fromBundle(getIntent().getExtras());
        Session session = new Session(args.getBranchInfo(), args.getUserInfo(), args.getRoleInfo());
        session.setConnectionReachable(args.getIsConnectionReachable());
        this.homeActivityViewmodel = new ViewModelProvider(this).get(HomeActivityViewmodel.class);
        if (session.isConnectionReachable()) {
            this.homeActivityViewmodel.setBranchRepository(branchOnlineRepository);
            this.homeActivityViewmodel.setRoleRepository(roleOnlineRepository);
        } else {
            this.homeActivityViewmodel.setBranchRepository(branchOfflineRepository);
            this.homeActivityViewmodel.setRoleRepository(roleOfflineRepository);
        }

        if (!hasPermissions()) {
            requestBluetoothPerms.launch(permissions);
        }

        MutableLiveData<Session> sessionMutableLiveData = this.homeActivityViewmodel.sessionMutableLiveData;
        sessionMutableLiveData.setValue(session);

        DrawerLayout layout = binding.main;
        NavigationView navigationView = binding.navView;
        setSupportActionBar(binding.toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.stock_view,
                R.id.sales_view,
                R.id.manage_sales,
                R.id.salesHistoryFragment,
                R.id.settings).setOpenableLayout(layout).build();

        NavController navController = ((NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2)).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public boolean hasPermissions() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public AlertDialog showDialog(String permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {

        });
        builder.setTitle("Permission " + permission + " is required for printing function. Please allow in settings");
        builder.setMessage("It seems that you have rejected bluetooth permissions which prevents the app from contacting a printer.");

        return builder.create();
    }

}