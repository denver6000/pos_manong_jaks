package com.denproj.posmanongjaks

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException
import com.dantsu.escposprinter.exceptions.EscPosConnectionException
import com.dantsu.escposprinter.exceptions.EscPosEncodingException
import com.dantsu.escposprinter.exceptions.EscPosParserException
import com.denproj.posmanongjaks.databinding.ActivityPrintBinding
import com.denproj.posmanongjaks.dialog.LoadingDialog
import com.denproj.posmanongjaks.dialog.PickBluetoothDeviceDialog.OnBluetoothPermissionsGranted
import com.denproj.posmanongjaks.viewModel.PrintActivityViewmodel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

@AndroidEntryPoint
class PrintActivity : AppCompatActivity(), View.OnClickListener {
    private val onBluetoothPermissionsGranted: OnBluetoothPermissionsGranted? = null
    private var selectedDevice: BluetoothConnection? = null
    private var printActivityViewmodel: PrintActivityViewmodel? = null

    var binding: ActivityPrintBinding? = null
    var stringToPrint: String? = null

    var permissionsLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult<Array<String>, Map<String, Boolean>>(
            ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.containsValue(false)) {
                Toast.makeText(
                    this@PrintActivity,
                    "There was a permission not allowed.",
                    Toast.LENGTH_SHORT
                ).show()
                val alertDialog = AlertDialog.Builder(this@PrintActivity)
                alertDialog.setTitle("Please Allow All Permissions To Access Print Feature")
                    .setMessage("Allow or Deny")
                    .setPositiveButton(
                        "Allow"
                    ) { dialogInterface: DialogInterface?, i: Int ->
                        //TODO: Add A Permission Re Launcher
                        // permissionsLauncher.launch(permissions)
                    }
                    .setNegativeButton("Deny") { dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.dismiss()
                        this@PrintActivity.finish()
                    }.show()
            } else {
                if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
                    val alerBuilder = AlertDialog.Builder(this@PrintActivity)
                    alerBuilder.setTitle("Bluetooth is Disabled")
                        .setMessage("Please enable bluetooth")
                        .setPositiveButton(
                            "Open Settings"
                        ) { dialogInterface: DialogInterface?, i: Int ->
                            checkIfBluetoothIsEnabled()
                        }.setOnDismissListener { dialogInterface: DialogInterface? ->
                            checkIfBluetoothIsEnabled()
                        }
                    alerBuilder.create().show()
                }
            }
        }


    var bluetoothSettingLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK || BluetoothAdapter.getDefaultAdapter().isEnabled) {
                Toast.makeText(this@PrintActivity, "You can now print.", Toast.LENGTH_SHORT)
                    .show()
                setupDevices()
                binding!!.printData.setOnClickListener(this@PrintActivity)
            } else {
                Toast.makeText(
                    this@PrintActivity,
                    "Please Enable Bluetooth.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    val printActivityArgs: PrintActivityArgs by navArgs()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        this.binding = ActivityPrintBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        this.stringToPrint = printActivityArgs.printStr
        this.printActivityViewmodel = ViewModelProvider(this).get(
            PrintActivityViewmodel::class.java
        )

        checkBluetoothPermissions()
    }

    fun print(strToPrint: String?): CompletableFuture<Void> {
        val loadingDialog = LoadingDialog()
        loadingDialog.show(supportFragmentManager, "")
        return CompletableFuture.supplyAsync(Supplier<Void?> {
            var printer: EscPosPrinter? = null
            try {
                selectedDevice!!.connect()
                printer = EscPosPrinter(selectedDevice, 203, 58f, 32)
                printer.printFormattedText(strToPrint)
                this@PrintActivity.finish()
                printer.disconnectPrinter()
            } catch (e: EscPosConnectionException) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            } catch (e: EscPosEncodingException) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            } catch (e: EscPosBarcodeException) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            } catch (e: EscPosParserException) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            null
        }).thenAccept { unused: Void? ->
            loadingDialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        if (BluetoothAdapter.getDefaultAdapter().isEnabled) {
            checkIfBluetoothIsEnabled()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun checkBluetoothPermissions() {
        permissionsLauncher.launch(getPermissions())
    }

    fun checkIfBluetoothIsEnabled() {
        if (BluetoothAdapter.getDefaultAdapter() == null || !BluetoothAdapter.getDefaultAdapter().isEnabled) {
            bluetoothSettingLauncher.launch(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
        } else {
            setupDevices()
            binding!!.printData.setOnClickListener(this)
        }
    }

    fun setupDevices() {
        binding!!.chooseDevice.setOnClickListener { view: View? ->
            val bluetoothDevicesList = (BluetoothPrintersConnections()).list
            if (bluetoothDevicesList != null) {
                val items = arrayOfNulls<String>(bluetoothDevicesList.size + 1)
                items[0] = "Default printer"
                var i = 0
                for (device in bluetoothDevicesList) {
                    items[++i] = device.device.name
                }

                val alertDialog = android.app.AlertDialog.Builder(this@PrintActivity)
                alertDialog.setTitle("Bluetooth printer selection")
                alertDialog.setItems(
                    items
                ) { dialogInterface: DialogInterface?, i1: Int ->
                    val index = i1 - 1
                    selectedDevice = if (index == -1) {
                        null
                    } else {
                        bluetoothDevicesList[index]
                    }
                    binding!!.name.text = items[i1]
                }

                val alert = alertDialog.create()
                alert.setCanceledOnTouchOutside(false)
                alert.show()
            } else {
                Toast.makeText(
                    this,
                    "Bluetooth might not be enabled.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun getPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onClick(view: View) {
        print(this.stringToPrint)
    }
}