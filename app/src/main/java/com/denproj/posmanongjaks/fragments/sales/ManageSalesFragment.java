package com.denproj.posmanongjaks.fragments.sales;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.FragmentManageSalesBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ManageSalesFragment extends Fragment {


    FragmentManageSalesBinding binding;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("MMMMM d, yyyy", Locale.ENGLISH);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageSalesBinding.inflate(inflater);

        return binding.getRoot();
    }
}