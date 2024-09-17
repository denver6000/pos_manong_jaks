package com.denproj.posmanongjaks.dialog;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denproj.posmanongjaks.R;

public class ChangeProductPropertiesFragment extends DialogFragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_change_product_properties, container, false);
    }
}