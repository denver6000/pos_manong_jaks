package com.denproj.posmanongjaks.model

import com.google.firebase.firestore.Exclude
import java.util.Random
import kotlin.math.min

class Sale {
    var saleId: Int? = null
    var branchId: String? = null
    var total: Double = 0.0
    var paidAmount: Double = 0.0
    var change: Double = 0.0


    var date: String? = null
    var time: String? = null
    var hour: Int? = null
    var minute: Int? = null


    constructor(
        saleId: Int,
        branchId: String?,
        total: Double,
        paidAmount: Double,
        change: Double
    ) {
        this.saleId = saleId
        this.branchId = branchId
        this.total = total
        this.paidAmount = paidAmount
        this.change = change
    }

    constructor()

    companion object {
        fun generateId(): String {
            val random = Random()
            val randomSixDigit = 100000 + random.nextInt(900000)
            return "10$randomSixDigit"
        }
    }
}
