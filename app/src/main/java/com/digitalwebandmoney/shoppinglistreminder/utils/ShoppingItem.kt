package com.digitalwebandmoney.shoppinglistreminder.utils

import android.graphics.Paint
import android.graphics.Typeface
import com.digitalwebandmoney.shoppinglistreminder.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.shopping_item_row.view.*

//Shopping item class for each row
class ShoppingItem (var item: com.digitalwebandmoney.shoppinglistreminder.model.Item)  : Item(){

    override fun getLayout() = R.layout.shopping_item_row


    override fun bind(viewHolder: ViewHolder, position: Int) {
        //get all shopping items and populate,
        viewHolder.itemView.itemPurchasedCheckbox.isChecked = item.purchased
        viewHolder.itemView.itemTitleTextView.text = "${item.itemTitle}"
        viewHolder.itemView.itemTitleTextView.setTypeface(null, Typeface.BOLD)

        if(item.purchased){
            viewHolder.itemView.itemTitleTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            viewHolder.itemView.itemTitleTextView.setTypeface(null, Typeface.NORMAL)
        }else{
            viewHolder.itemView.itemTitleTextView.setTypeface(null, Typeface.BOLD)
            viewHolder.itemView.itemTitleTextView.paintFlags = Paint.LINEAR_TEXT_FLAG
        }

        viewHolder.itemView.itemPriceTextView.text = ""

        val shoppingItemOpenDBHelper = ShoppingItemOpenDBHelper(viewHolder.itemView.context, null)

        //listen to complete item check
        viewHolder.itemView.itemPurchasedCheckbox.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked){
                //ischecked
                viewHolder.itemView.itemTitleTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                viewHolder.itemView.itemTitleTextView.setTypeface(null, Typeface.NORMAL)
                item.purchased = true
                shoppingItemOpenDBHelper.updatePurchasedItem(item)
            }else{
                viewHolder.itemView.itemTitleTextView.setTypeface(null, Typeface.BOLD)
                viewHolder.itemView.itemTitleTextView.paintFlags = Paint.LINEAR_TEXT_FLAG
                item.purchased = false
                shoppingItemOpenDBHelper.updatePurchasedItem(item)
            }
        }

    }



}