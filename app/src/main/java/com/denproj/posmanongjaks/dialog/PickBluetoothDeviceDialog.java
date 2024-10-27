package com.denproj.posmanongjaks.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.FragmentPickBluetoothDeviceDialogBinding;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PickBluetoothDeviceDialog extends DialogFragment {
    FragmentPickBluetoothDeviceDialogBinding binding;
    private OnBluetoothPermissionsGranted onBluetoothPermissionsGranted;
    private BluetoothConnection selectedDevice;
    CompleteSaleInfo completeSaleInfo;

    public PickBluetoothDeviceDialog(CompleteSaleInfo completeSaleInfo) {
        this.completeSaleInfo = completeSaleInfo;
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentPickBluetoothDeviceDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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
                    Button button = binding.printerName;
                    button.setText(items[i1]);
                });
            }

            builder.setPositiveButton("Print", (dialogInterface, i) -> print().thenAccept(unused -> {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
            }).exceptionally(new Function<Throwable, Void>() {
                @Override
                public Void apply(Throwable throwable) {
                    Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }
            }));

        });
        builder.setCancelable(false);
        builder.setView(binding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        return alertDialog;
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

    public void checkBluetoothPermissions(OnBluetoothPermissionsGranted onBluetoothPermissionsGranted) {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Please allow permissions", Toast.LENGTH_SHORT).show();
        }
        onBluetoothPermissionsGranted.onPermissionsGranted();
    }
    public interface OnBluetoothPermissionsGranted {
        void onPermissionsGranted();
    }
}