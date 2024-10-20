package com.denproj.posmanongjaks.model;

import com.google.firebase.Timestamp;

import java.util.HashMap;

public class SaleRecord {
    String saleId;
    HashMap<String, SaleItem> itemsSold;
    Timestamp timestamp;
    Float total;
    Float paidAmount;
    Float change;

}
