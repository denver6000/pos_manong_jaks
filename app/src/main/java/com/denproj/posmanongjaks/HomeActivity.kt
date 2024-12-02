package com.denproj.posmanongjaks

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.denproj.posmanongjaks.databinding.ActivityHomeBinding
import com.denproj.posmanongjaks.databinding.NavHeaderBinding
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseItemRepository
import com.denproj.posmanongjaks.repository.firebaseImpl.FirebaseProductRepository
import com.denproj.posmanongjaks.session.SessionSimple
import com.denproj.posmanongjaks.viewModel.HomeActivityViewmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var appBarConfiguration: AppBarConfiguration? = null
    private val homeActivityViewmodel: HomeActivityViewmodel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private val homeActArgs by navArgs<HomeActivityArgs>()

    var requestBluetoothPerms = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.forEach { (permission: String, isAllowed: Boolean) ->
            if (isAllowed) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    showDialog(permission).show()
                }
            }
        }
    }
    var permissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(
            layoutInflater
        )
        setContentView(binding.getRoot())
        if (!hasPermissions()) {
            requestBluetoothPerms.launch(permissions)
        }

        loadSession(SessionSimple(userId = homeActArgs.userId, branchId = homeActArgs.branchId, branchName = homeActArgs.branchName, name = homeActArgs.username, roleName = homeActArgs.roleName))

        val layout = binding.main
        val navigationView = binding.navView
        setSupportActionBar(binding.toolbar)
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.stock_view,
            R.id.sales_view,
            R.id.salesHistoryFragment,
            R.id.stock_report_fragment,
            R.id.settings
        ).setOpenableLayout(layout).build()
        val navController =
            (supportFragmentManager.findFragmentById(R.id.homeFragmentContainerView) as NavHostFragment?)!!.navController
        setupActionBarWithNavController(this, navController, appBarConfiguration!!)
        setupWithNavController(navigationView, navController)
    }

    fun hasPermissions(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.homeFragmentContainerView)
        return (navigateUp(navController, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    private fun showDialog(permission: String): AlertDialog {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton("Ok") { dialogInterface: DialogInterface?, i: Int -> }
        builder.setTitle("Permission $permission is required for printing function. Please allow in settings")
        builder.setMessage("It seems that you have rejected bluetooth permissions which prevents the app from contacting a printer.")
        return builder.create()
    }

    private fun loadSession(session: SessionSimple) {
        val email = FirebaseAuth.getInstance().currentUser!!.email!!
        val username = session.name!!
         loadCredentialsToUI(username, email)
        this@HomeActivity.homeActivityViewmodel.saveSession(SessionSimple(session.userId, session.name, session.branchId, session.branchName, session.roleName))
        this@HomeActivity
            .homeActivityViewmodel.updateLiveSession(session)
        val branchId = session.branchId
        val itemsOnBranches = FirebaseDatabase.getInstance()
            .getReference(FirebaseItemRepository.PATH_TO_ITEMS_LIST + "/" + branchId)
        itemsOnBranches.keepSynced(true)
        val productsOnBranches = FirebaseDatabase.getInstance()
            .getReference(FirebaseProductRepository.PATH_TO_BRANCH_PRODUCTS + "/" + branchId)
        productsOnBranches.keepSynced(true)
    }

    private fun loadCredentialsToUI(staffName: String, staffEmail: String) {
        val headerBinding: NavHeaderBinding = NavHeaderBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.staffName = staffName
        headerBinding.staffEmail = staffEmail
    }
}