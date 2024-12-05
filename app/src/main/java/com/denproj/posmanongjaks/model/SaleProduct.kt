package com.denproj.posmanongjaks.model

class SaleProduct {
    var productId: Long? = null
    var amount: Int = 0
    var saleId: Int? = null
    var name: String? = null
    var imagePath: String? = null


    constructor()
    constructor(imagePath: String?, name: String?, saleId: Int?, productId: Long?, amount: Int) {
        this.imagePath = imagePath
        this.name = name
        this.saleId = saleId
        this.productId = productId
        this.amount = amount
    }


    override fun toString(): String {
        return name!!
    }
}
