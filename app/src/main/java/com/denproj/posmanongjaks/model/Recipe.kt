package com.denproj.posmanongjaks.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Recipe {
    var amount: Int? = null
    var ItemName: String? = null


    override fun toString(): String {
        return """${ItemName}
 x ${amount}"""
    }
}
