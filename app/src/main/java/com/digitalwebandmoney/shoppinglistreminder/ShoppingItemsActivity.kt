package com.digitalwebandmoney.shoppinglistreminder

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.digitalwebandmoney.shoppinglistreminder.model.Item
import com.digitalwebandmoney.shoppinglistreminder.model.Store
import com.digitalwebandmoney.shoppinglistreminder.utils.ShoppingItem
import com.digitalwebandmoney.shoppinglistreminder.utils.ShoppingItemOpenDBHelper
import com.digitalwebandmoney.shoppinglistreminder.utils.StoreItem
import com.digitalwebandmoney.shoppinglistreminder.utils.StoreOpenDBHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.add_store_dialog.view.*

class ShoppingItemsActivity : AppCompatActivity() {

    var groupAdapter = GroupAdapter<ViewHolder>()
    private lateinit var shoppingRecyclerView: RecyclerView
    private var  mStoreName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_items)

        shoppingRecyclerView = findViewById(R.id.shoppingItemsRecyclerView)

        mStoreName = intent.getStringExtra("STORE_NAME")
        val actionBar = supportActionBar
        actionBar!!.title = "$mStoreName's List"

        queryAllItems()
        shoppingRecyclerView.adapter = groupAdapter

        groupAdapter.setOnItemClickListener { item, view ->
            //item on shopping cart clicked, option to delete or edit
            var shoppingItem = item as ShoppingItem
            var item = shoppingItem.item
        }

        groupAdapter.setOnItemLongClickListener { item, view ->
            val shoppingItem = item as ShoppingItem
            val item = shoppingItem.item
            showDeleteAlertDialog(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.addNewShoppingItem -> {
                showAlertDialogForAddingItem()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialogForAddingItem(){
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_item_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Add Item")
        //show dialog
        val  mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.dialogAddStoreBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout
            val itemToShop = mDialogView.dialogStoreName.text.toString()
            val item = Item("", false, itemToShop, mStoreName)
            //set the input text in TextView
            //save item to database. then close dialog.
            val shoppingItemOpenDBHelper = ShoppingItemOpenDBHelper(this, null)
            shoppingItemOpenDBHelper.addItem(item)
            queryAllItems()
            Toast.makeText(this@ShoppingItemsActivity, "Item added successfully.", Toast.LENGTH_SHORT).show()
        }
        //cancel button click of custom layout
        mDialogView.dialogCancelAddStoreBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun queryAllItems(){
        groupAdapter.clear()
        val dbHandler = ShoppingItemOpenDBHelper(this, null)
        val cursor = dbHandler.getAllItems(mStoreName)
        cursor!!.moveToFirst()

        while (cursor.moveToNext()) {
            val id = (cursor.getString(cursor.getColumnIndex(ShoppingItemOpenDBHelper.COLUMN_ID)))
            val itemName = (cursor.getString(cursor.getColumnIndex(ShoppingItemOpenDBHelper.COLUMN_NAME)))
            val purchased = (cursor.getInt(cursor.getColumnIndex(ShoppingItemOpenDBHelper.COLUMN_PURCHASED)))
            val storeName = (cursor.getString(cursor.getColumnIndex(ShoppingItemOpenDBHelper.COLUMN_STORE_FOR_ITEM)))

            var isPurchased = false
            if(purchased == 1){
                isPurchased = true
            }else{
                isPurchased = false
            }

            val item = ShoppingItem(Item(id, isPurchased, itemName, storeName))
            groupAdapter.add(item)
        }
        cursor.close()
    }

    private fun showDeleteAlertDialog(item: Item): Boolean {
        val dialogBuilder = AlertDialog.Builder(this)
        // set message of alert dialog
        dialogBuilder.setMessage("Delete item?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
            // negative button text and action
            .setNegativeButton("Delete", DialogInterface.OnClickListener {
                    dialog, id -> deleteItem(item)
                    queryAllItems()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Remove ${item.itemTitle}")
        // show alert dialog
        alert.show()
        return false
    }

    private fun deleteItem(item: Item){
        val dbHandler = ShoppingItemOpenDBHelper(this, null)
        dbHandler.deleteItem(item.itemTitle)
        Toast.makeText(this@ShoppingItemsActivity, "Item deleted successfully.", Toast.LENGTH_SHORT).show()

    }


}
