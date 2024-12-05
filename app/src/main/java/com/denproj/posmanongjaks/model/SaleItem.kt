package com.denproj.posmanongjaks.model


class SaleItem {
    var item_id: Int = 0
    var saleId: Int? = null
    var amount: Int = 0
    var itemName: String? = null
    var imagePath: String? = null

    constructor()
    constructor(imagePath: String?, itemName: String?, amount: Int, saleId: Int?, item_id: Int) {
        this.imagePath = imagePath
        this.itemName = itemName
        this.amount = amount
        this.saleId = saleId
        this.item_id = item_id
    }


    override fun toString(): String {
        return itemName!!
    }
}
