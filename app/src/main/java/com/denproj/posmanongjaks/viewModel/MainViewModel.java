package com.denproj.posmanongjaks.viewModel;

import android.content.ClipData;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @Inject
    public MainViewModel() {

    }

    public void loadStocksAndProductsInRoom() {

    }

}
