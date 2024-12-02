package com.denproj.posmanongjaks.model


class SaleItem {
    var item_id: Int = 0
    var saleId: Int? = null
    var amount: Int = 0

    var itemName: String? = null

    constructor()

    constructor(item_id: Int, saleId: Int?, amount: Int, itemName: String?) {
        this.item_id = item_id
        this.saleId = saleId
        this.amount = amount
        this.itemName = itemName
    }

    override fun toString(): String {
        return itemName!!
    }
}
