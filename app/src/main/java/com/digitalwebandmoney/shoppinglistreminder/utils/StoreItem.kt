package com.digitalwebandmoney.shoppinglistreminder.utils

import android.graphics.Typeface
import com.digitalwebandmoney.shoppinglistreminder.R
import com.digitalwebandmoney.shoppinglistreminder.model.Store
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.shopping_item_row.view.*
import kotlinx.android.synthetic.main.store_row.view.*

class StoreItem (var store: Store)  : Item() {

    override fun getLayout() = R.layout.store_row

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.storeName.text = "${store.storeName}"
        viewHolder.itemView.storeName.setTypeface(null, Typeface.BOLD)
    }


}