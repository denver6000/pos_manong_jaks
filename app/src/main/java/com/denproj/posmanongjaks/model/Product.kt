package com.denproj.posmanongjaks.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
class Product {

    var product_id: Long? = null
    var product_name: String? = null
    var product_image_path: String? = null
    var product_price: Float = 0f
    var product_category: String? = null

    @Exclude
    var uri: Uri? = null

    @JvmField
    @Ignore
    var recipes: HashMap<String, Recipe>? = null

    constructor()

    constructor(
        product_id: Long,
        product_name: String?,
        product_image_path: String?,
        product_price: Float,
        product_category: String?,
        recipes: HashMap<String, Recipe>?
    ) {
        this.product_id = product_id
        this.product_name = product_name
        this.product_image_path = product_image_path
        this.product_price = product_price
        this.product_category = product_category
        this.recipes = recipes
    }

    override fun toString(): String {
        return product_id.toString()
    }
}