package com.denproj.posmanongjaks.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity
class Item {

    var item_image_path: String? = null
    var image_url: Uri? = null

    var item_id: Int = 0

    var item_name: String? = null
    var item_category: String? = null

    var item_quantity: Double = 0.0

    var item_unit: String? = null

    var item_price: Double = 0.0
    var isAds_on: Boolean = false


    @Ignore
    constructor(
        item_image_path: String?,
        item_id: Int,
        item_name: String?,
        item_category: String?,
        item_quantity: Double,
        item_unit: String?,
        item_price: Double,
        ads_on: Boolean
    ) {
        this.item_image_path = item_image_path
        this.item_id = item_id
        this.item_name = item_name
        this.item_category = item_category
        this.item_quantity = item_quantity
        this.item_unit = item_unit
        this.item_price = item_price
        this.isAds_on = ads_on
    }

    constructor()

    override fun toString(): String {
        return item_id.toString()
    }
}

