package com.denproj.posmanongjaks.room.view;

import androidx.room.DatabaseView;

@DatabaseView("SELECT Sale.saleAmount, Product.product_name, Product.product_price FROM Sale INNER JOIN Product ON Sale.productId = Product.product_id")
public class SalesWithProduct {
}
