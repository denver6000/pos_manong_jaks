package com.denproj.posmanongjaks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.denproj.posmanongjaks.databinding.ActivityPrintBinding;
import com.denproj.posmanongjaks.dialog.PickBluetoothDeviceDialog;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class PrintActivity extends AppCompatActivity {

    private PickBluetoothDeviceDialog.OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;
    private BluetoothConnection selectedDevice;
    CompleteSaleInfo completeSaleInfo;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityPrintBinding binding = ActivityPrintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        checkBluetoothPermissions(() -> {
            BluetoothConnection[] bluetoothConnections = (new BluetoothConnections()).getList();
            if (bluetoothConnections != null) {
                final String[] items = new String[bluetoothConnections.length + 1];
                items[0] = "Default printer";
                int i = 0;
                for (BluetoothConnection device : bluetoothConnections) {
                    items[++i] = device.getDevice().getName();
                }

                builder.setItems(items, (dialogInterface, i1) -> {
                    int index = i1 - 1;
                    if (index == -1) {
                        selectedDevice = null;
                    } else {
                        selectedDevice = bluetoothConnections[index];
                    }
                    Button button = binding.name;
                    button.setText(items[i1]);
                });
            }

            builder.setPositiveButton("Print", (dialogInterface, i) -> print().thenAccept(unused -> {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            }).exceptionally(new Function<Throwable, Void>() {
                @Override
                public Void apply(Throwable throwable) {
                    Toast.makeText(PrintActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }
            }));
        });
        builder.setCancelable(false);
        builder.setView(binding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
    }

    public CompletableFuture<Void> print()  {
        return CompletableFuture.supplyAsync((Supplier<Void>) () -> {
            EscPosPrinter printer = null;
            try {
                selectedDevice.connect();
                printer = new EscPosPrinter(selectedDevice, 203, 58f, 32);
                printer.printFormattedText("[L]" + completeSaleInfo.getSaleItems());
            } catch (EscPosConnectionException | EscPosEncodingException | EscPosBarcodeException |
                     EscPosParserException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public void checkBluetoothPermissions(PickBluetoothDeviceDialog.OnBluetoothPermissionsGranted onBluetoothPermissionsGranted) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please allow permissions", Toast.LENGTH_SHORT).show();
        }
        onBluetoothPermissionsGranted.onPermissionsGranted();
    }
    public interface OnBluetoothPermissionsGranted {
        void onPermissionsGranted();
    }
}