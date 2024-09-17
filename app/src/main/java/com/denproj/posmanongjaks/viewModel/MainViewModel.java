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

    public void  getRegisterItems(OnItemFetched<Item> onItemFetched) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("items");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Item> items = new ArrayList<>();
                snapshot.getChildren().forEach(dataSnapshot -> {
                    items.add(dataSnapshot.getValue(Item.class));
                });
                onItemFetched.onFetched(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onItemFetched.onError(error.toException());
            }
        });
    }

    public void loadStocksAndProductsInRoom() {



    }

    public interface OnItemFetched<T> {
       void onFetched(ArrayList<T> result);
        void onError(Exception e);
    }

}
