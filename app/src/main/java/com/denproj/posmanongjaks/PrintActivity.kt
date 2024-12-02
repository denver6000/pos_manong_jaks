package com.denproj.posmanongjaks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.denproj.posmanongjaks.databinding.ActivityPrintBinding;
import com.denproj.posmanongjaks.dialog.LoadingDialog;
import com.denproj.posmanongjaks.dialog.PickBluetoothDeviceDialog;
import com.denproj.posmanongjaks.viewModel.PrintActivityViewmodel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PrintActivity extends AppCompatActivity implements View.OnClickListener{

    private PickBluetoothDeviceDialog.OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;
    private BluetoothConnection selectedDevice = null;
    private PrintActivityViewmodel printActivityViewmodel;
    String[] permissions = getPermissions();
    ActivityPrintBinding binding;
    String stringToPrint;

    ActivityResultLauncher<String[]> permissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> o) {
            if (o.containsValue(false)) {
                Toast.makeText(PrintActivity.this, "There was a permission not allowed.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrintActivity.this);
                alertDialog.setTitle("Please Allow All Permissions To Access Print Feature").setMessage("Allow or Deny")
                        .setPositiveButton("Allow", (dialogInterface, i) -> {
                            permissionsLauncher.launch(permissions);
                        }).setNegativeButton("Deny", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            PrintActivity.this.finish();
                        }).show();
            } else {
                if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    AlertDialog.Builder alerBuilder = new AlertDialog.Builder(PrintActivity.this);
                    alerBuilder.setTitle("Bluetooth is Disabled")
                            .setMessage("Please enable bluetooth")
                            .setPositiveButton("Open Settings", (dialogInterface, i) -> {
                                checkIfBluetoothIsEnabled();
                            }).setOnDismissListener(dialogInterface -> {
                                checkIfBluetoothIsEnabled();
                            });
                    alerBuilder.create().show();
                }
            }
        }
    });


    ActivityResultLauncher<Intent> bluetoothSettingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK || BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                Toast.makeText(PrintActivity.this, "You can now print.", Toast.LENGTH_SHORT).show();
                setupDevices();
                binding.printData.setOnClickListener(PrintActivity.this);
            } else {
                Toast.makeText(PrintActivity.this, "Please Enable Bluetooth.", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.binding = ActivityPrintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        PrintActivityArgs args = PrintActivityArgs.fromBundle(getIntent().getExtras());
        this.stringToPrint = args.getPrintStr();
        this.printActivityViewmodel = new ViewModelProvider(this).get(PrintActivityViewmodel.class);

        checkBluetoothPermissions();
    }

    public CompletableFuture<Void> print(String strToPrint) {
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(getSupportFragmentManager(), "");
        return CompletableFuture.supplyAsync((Supplier<Void>) () -> {
            EscPosPrinter printer = null;
            try {
                selectedDevice.connect();
                printer = new EscPosPrinter(selectedDevice, 203, 58f, 32);
                printer.printFormattedText(strToPrint);
                PrintActivity.this.finish();
                printer.disconnectPrinter();
            } catch (EscPosConnectionException | EscPosEncodingException | EscPosBarcodeException |
                     EscPosParserException e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }).thenAccept(unused -> {
            loadingDialog.dismiss();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            checkIfBluetoothIsEnabled();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void checkBluetoothPermissions() {
        permissionsLauncher.launch(permissions);
    }

    public void checkIfBluetoothIsEnabled() {
        if (BluetoothAdapter.getDefaultAdapter() == null || !BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            bluetoothSettingLauncher.launch(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
        } else {
            setupDevices();
            binding.printData.setOnClickListener(this);
        }
    }

    public void setupDevices() {
        binding.chooseDevice.setOnClickListener(view -> {
            final BluetoothConnection[] bluetoothDevicesList = (new BluetoothPrintersConnections()).getList();
            if (bluetoothDevicesList != null) {
                final String[] items = new String[bluetoothDevicesList.length + 1];
                items[0] = "Default printer";
                int i = 0;
                for (BluetoothConnection device : bluetoothDevicesList) {
                    items[++i] = device.getDevice().getName();
                }

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(PrintActivity.this);
                alertDialog.setTitle("Bluetooth printer selection");
                alertDialog.setItems(
                        items,
                        (dialogInterface, i1) -> {
                            int index = i1 - 1;
                            if (index == -1) {
                                selectedDevice = null;
                            } else {
                                selectedDevice = bluetoothDevicesList[index];
                            }
                            binding.name.setText(items[i1]);
                        }
                );

                android.app.AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            } else {
                Toast.makeText(this, "Bluetooth might not be enabled.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String[] getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADMIN
            };
        } else {
            return new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        }
    }

    @Override
    public void onClick(View view) {
        print(this.stringToPrint);
    }

}