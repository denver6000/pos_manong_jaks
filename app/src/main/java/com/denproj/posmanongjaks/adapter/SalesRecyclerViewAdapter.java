package com.denproj.posmanongjaks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denproj.posmanongjaks.R;
import com.denproj.posmanongjaks.databinding.SalesWithProductCardBinding;
import com.denproj.posmanongjaks.dialog.SaleBreakDownDialog;
import com.denproj.posmanongjaks.model.Sale;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesRecyclerViewAdapter extends RecyclerView.Adapter<SalesRecyclerViewAdapter.ViewHolder> {

    List<Sale> salesWithProducts = new ArrayList<>();
    View.OnClickListener onClickListener;
    FragmentManager fragmentActivity;

    public SalesRecyclerViewAdapter() {

    }

    public SalesRecyclerViewAdapter(FragmentManager fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public List<Sale> getSalesWithProducts() {
        return salesWithProducts;
    }

    public void setSalesWithProducts(List<Sale> salesWithProducts)
    {
        this.salesWithProducts.clear();
        this.salesWithProducts.addAll(salesWithProducts);
        notifyItemRangeInserted(0, salesWithProducts.size());
    }

    public SalesRecyclerViewAdapter(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_with_product_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalesWithProductCardBinding binding = holder.binding;
        Sale sales = salesWithProducts.get(position);
        binding.setSale(sales);

        binding.setChange(sales.getChange());
        binding.setTotal(sales.getTotal());
        binding.setPaymentAmount(sales.getPaidAmount());
        Calendar.getInstance().set(sales.getYear(), sales.getMonth(), sales.getDay());
        Date date = Calendar.getInstance().getTime();

        binding.setFormattedDate(formatDate(date));

        binding.getRoot().setOnClickListener(view -> new SaleBreakDownDialog(sales).show(fragmentActivity, ""));

        Calendar.getInstance().clear();

    }

    @Override
    public int getItemCount() {
        return salesWithProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SalesWithProductCardBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SalesWithProductCardBinding.bind(itemView);
        }
    }



    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("d-MMMM-yyyy HH:mm:ss", Locale.getDefault());
        return formatter.format(date);
    }
}
