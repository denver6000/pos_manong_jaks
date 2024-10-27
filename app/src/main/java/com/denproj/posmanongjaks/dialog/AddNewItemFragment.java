package com.denproj.posmanongjaks.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.FragmentAddNewItemBinding;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class AddNewItemFragment extends DialogFragment {
    private final String MIME_TYPE = "image";
    private FragmentAddNewItemBinding binding;

    private Uri selectedUri = null;
    private String selectedUnit = null;
    private String selectedCategory = null;

    ActivityResultLauncher<Intent> pickMedia = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
        if (o.getResultCode() == Activity.RESULT_OK && o.getData() != null) {
            selectedUri = o.getData().getData();
        }
    });

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentAddNewItemBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        binding.chooseImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*.jpg");
            pickMedia.launch(intent);
        });

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.categories, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.units, android.R.layout.simple_spinner_dropdown_item);

        binding.unitSpinner.setAdapter(unitAdapter);
        binding.categorySpinner.setAdapter(categoryAdapter);

        setupSpinners();
        builder.setView(binding.getRoot());
//        builder.setPositiveButton("Save", (dialogInterface, i) -> {
//            String itemName = binding.getItemName();
//            int itemQuantity = Integer.valueOf(binding.getItemQuantity());
//            Double itemPrice = Double.valueOf(binding.getItemPrice());
//            if ((itemName != null && !itemName.isEmpty()) && itemQuantity > 0 && itemPrice > 0 && selectedCategory != null && selectedUnit != null) {
//                saveImageToFirebaseStorage(new OnDataReceived<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        int itemId = generateRandomId();
//                        Item item = new Item(result, itemId, itemName, selectedCategory, itemQuantity, selectedUnit, itemPrice);
//                        insertToGlobalItemList(item, new OnDataReceived<Void>() {
//                            @Override
//                            public void onSuccess(Void result) {
//                                Toast.makeText(requireContext(), "Item Inserted to Global List", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFail(Exception e) {
//                                Toast.makeText(requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                                Log.e("Exception", e.getMessage());
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                        Log.e("Error", e.getMessage());
//                    }
//                });
//            }
//        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dismiss();
        });

        return builder.show();
    }

    public void setupSpinners() {
        binding.unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUnit = adapterView.getAdapter().getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedUnit = null;
            }
        });
        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = adapterView.getAdapter().getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCategory = null;
            }
        });
    }


    public void saveImageToFirebaseStorage(OnDataReceived<String> onDataReceived) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        String path = "item_images/" + System.currentTimeMillis() + ".jpg";
        StorageReference storageReference = storage.getReference(path);
        storageReference.putFile(this.selectedUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    onDataReceived.onSuccess(path);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                onDataReceived.onFail(task.getException());
            }
        });
    }

    public void insertToGlobalItemList(Item item, OnDataReceived<Void> onDataReceived) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("/items_list/").add(item);
    }

    public int generateRandomId() {
        return new Random().nextInt(100000 - 1) + 1;
    }

}