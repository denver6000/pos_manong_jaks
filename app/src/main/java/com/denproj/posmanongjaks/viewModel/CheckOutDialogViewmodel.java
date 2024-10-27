package com.denproj.posmanongjaks.viewModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.lifecycle.ViewModel;

import com.denproj.posmanongjaks.hilt.qualifier.OfflineImpl;
import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.ProductWrapper;
import com.denproj.posmanongjaks.model.CompleteSaleInfo;
import com.denproj.posmanongjaks.model.Item;
import com.denproj.posmanongjaks.model.User;
import com.denproj.posmanongjaks.repository.base.SaleRepository;
import com.denproj.posmanongjaks.session.Session;
import com.denproj.posmanongjaks.util.OnDataReceived;
import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CheckOutDialogViewmodel extends ViewModel {
    SaleRepository salesRepository;

    @Inject
    public CheckOutDialogViewmodel(@OfflineImpl SaleRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public void sell(String branchId, HashMap<Long, ProductWrapper> selectedItemsToSell, HashMap<Item, Integer> addOns, int year, int month, int day, Double total, Double payAmount, OnDataReceived<CompleteSaleInfo> onDataReceived) {
        salesRepository.insertSaleRecord(branchId, selectedItemsToSell, addOns, year, month, day, total, payAmount, onDataReceived);
    }

    public Bitmap printReceipt(Context context, CompleteSaleInfo sale, Session session) {
        Branch branch = session.getBranch();
        User user = session.getUser();
        ReceiptBuilder receipt = new ReceiptBuilder(1200);
//        receipt
//                .setMargin(30, 20)
//                .setAlign(Paint.Align.CENTER)
//                .setColor(Color.BLACK)
//                .setTextSize(60)
//                .setTypeface(context, "fonts/RobotoMono-Regular.ttf")
//                .addText("Manong Jak's Burger")
//                .setTextSize(50)
//                .setTypeface(context, "fonts/RobotoMono-Bold.ttf")
//                .addText(branch.getBranch_name())
//                .addBlankSpace(50)
//                .setAlign(Paint.Align.LEFT)
//                .setTypeface(context, "fonts/RobotoMono-Bold.ttf")
//                .setTextSize(50)
//                .addText("Products")
//                .addBlankSpace(30)
//                .setTextSize(40)
//                .setTypeface(context, "fonts/RobotoMono-Regular.ttf");
//        sale.getSaleProducts().forEach(saleProduct -> {
//            receipt
//                    .setAlign(Paint.Align.LEFT)
//                    .addText(saleProduct.getName(), false)
//                    .setAlign(Paint.Align.RIGHT)
//                    .addText("x " + saleProduct.getAmount());
//        });
//        receipt
//                .addBlankSpace(90)
//                .setTypeface(context, "fonts/RobotoMono-Bold.ttf")
//                .setTextSize(50)
//                .setAlign(Paint.Align.LEFT)
//                .addText("Add-Ons")
//                .addBlankSpace(20)
//                .setTypeface(context, "fonts/RobotoMono-Regular.ttf")
//                .setTextSize(40);
//        sale.getSaleItems().forEach(saleItem -> {
//            receipt.setAlign(Paint.Align.LEFT).
//                    addText(saleItem.getItemName(), false).
//                    setAlign(Paint.Align.RIGHT)
//                    .addText("x " + saleItem.getAmount());
//        });
//
//
//        receipt
//                .addBlankSpace(50)
//                .setTypeface(context, "fonts/RobotoMono-Bold.ttf")
//                .setAlign(Paint.Align.LEFT)
//                .addText("Amount", false)
//                .setTypeface(context, "fonts/RobotoMono-Regular.ttf")
//                .setAlign(Paint.Align.RIGHT)
//                .addText(sale.getSale().getTotal() + "Pesos");
//
//        receipt
//                .setTypeface(context, "fonts/RobotoMono-Bold.ttf")
//                .setAlign(Paint.Align.LEFT)
//                .addText("Paid Amount", false)
//                .setTypeface(context, "fonts/RobotoMono-Regular.ttf")
//                .setAlign(Paint.Align.RIGHT)
//                .addText(sale.getSale().getPaidAmount() + "Pesos");
//
//        receipt
//                .setTypeface(context, "fonts/RobotoMono-Bold.ttf")
//                .setAlign(Paint.Align.LEFT)
//                .addText("Change", false)
//                .setTypeface(context, "fonts/RobotoMono-Regular.ttf")
//                .setAlign(Paint.Align.RIGHT)
//                .addText(sale.getSale().getChange() + "Pesos");
//
//        receipt
//                .addBlankSpace(50)
//                .setAlign(Paint.Align.CENTER)
//                .addText("1111000000 TEST")
//                .addBlankSpace(50)
//                .addText("Staff Name: ", false)
//                .addText(user.getUsername());
        return receipt.build();
    }

}
