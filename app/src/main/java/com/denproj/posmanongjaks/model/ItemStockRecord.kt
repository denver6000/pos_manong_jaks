package com.denproj.posmanongjaks.model

import android.net.Uri

class ItemStockRecord (var item_name: String?, var item_id: Int, var stock_at_start: Int, var total_reduced_stock_amount: Int, var item_image_path: String?) {
    constructor() : this(null, 0, 0, 0, null) {

    }
}