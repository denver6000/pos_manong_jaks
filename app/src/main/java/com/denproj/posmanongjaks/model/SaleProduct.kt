package com.denproj.posmanongjaks.model

class SaleProduct {
    var productId: Long? = null
    var amount: Int = 0
    var saleId: Int? = null
    var name: String? = null


    constructor()

    constructor(product_id: Long?, amount: Int, sale_id: Int?, name: String?) {
        this.productId = product_id
        this.amount = amount
        this.saleId = sale_id
        this.name = name
    }

    override fun toString(): String {
        return name!!
    }
}
