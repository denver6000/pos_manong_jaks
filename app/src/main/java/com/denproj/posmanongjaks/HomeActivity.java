package com.denproj.posmanongjaks;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.denproj.posmanongjaks.databinding.ActivityHomeBinding;
import com.denproj.posmanongjaks.databinding.ActivityMainBinding;
import com.denproj.posmanongjaks.viewModel.BranchFragmentViewmodel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BranchFragmentViewmodel viewmodel = new ViewModelProvider(this).get(BranchFragmentViewmodel.class);
        viewmodel.branchIdLiveData.setValue(HomeActivityArgs.fromBundle(getIntent().getExtras()).getBranchId());
        DrawerLayout layout = binding.main;
        NavigationView navigationView = binding.navView;
        setSupportActionBar(binding.toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.stock_view,
                R.id.sales_view,
                R.id.manage_stocks,
                R.id.manage_sales,
                R.id.settings).setOpenableLayout(layout).build();

        NavController navController = ((NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2)).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}