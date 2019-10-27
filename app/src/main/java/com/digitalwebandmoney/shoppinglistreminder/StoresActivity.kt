package com.digitalwebandmoney.shoppinglistreminder

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView
import com.digitalwebandmoney.shoppinglistreminder.model.Store
import com.digitalwebandmoney.shoppinglistreminder.utils.StoreItem
import com.digitalwebandmoney.shoppinglistreminder.utils.StoreOpenDBHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.activity_stores.*
import kotlinx.android.synthetic.main.add_store_dialog.view.*

class StoresActivity : AppCompatActivity() {

    var groupAdapter = GroupAdapter<ViewHolder>()
    private lateinit var storesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores)

        storesRecyclerView = findViewById(R.id.shoppingStoresRecyclerView)

        val actionBar = supportActionBar
        actionBar!!.title = "Shopping List Reminder"
        actionBar!!.subtitle = "Stores"


        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            showALertDialogForAddingItem()

        }


        queryAllStores()
        storesRecyclerView.adapter = groupAdapter

        groupAdapter.setOnItemClickListener { item, view ->
            //item on shopping cart clicked, option to delete or edit
            var storeItem = item as StoreItem
            var store = storeItem.store

            val goToItems = Intent(this, ShoppingItemsActivity::class.java)
            goToItems.putExtra("STORE_NAME", store.storeName)
            startActivity(goToItems)
        }

        groupAdapter.setOnItemLongClickListener  { item, view ->
            var storeItem = item as StoreItem
            var store = storeItem.store
            // build alert dialog
            showDeleteAlertDialog(store)





        }



    }

    private fun showDeleteAlertDialog(store: Store): Boolean {
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("Delete store?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
            // negative button text and action
            .setNegativeButton("Delete", DialogInterface.OnClickListener {
                    dialog, id -> deleteStore(store)
                    queryAllStores()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Remove ${store.storeName}")
        // show alert dialog
        alert.show()
        return false
    }

    private fun deleteStore(store: Store){
        val dbHandler = StoreOpenDBHelper(this, null)
        dbHandler.deleteStore(store)
        Toast.makeText(this@StoresActivity, "Store deleted successfully.", Toast.LENGTH_SHORT).show()


    }

    private fun queryAllStores(){
        groupAdapter.clear()
        val dbHandler = StoreOpenDBHelper(this, null)
        val cursor = dbHandler.getAllStores()
        cursor!!.moveToFirst()

        while (cursor.moveToNext()) {
            val store = StoreItem(Store((cursor.getString(cursor.getColumnIndex(StoreOpenDBHelper.COLUMN_ID))), (cursor.getString(cursor.getColumnIndex(StoreOpenDBHelper.COLUMN_NAME)))))
            groupAdapter.add(store)
        }
        cursor.close()
    }

    private fun showALertDialogForAddingItem(){
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_store_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Add Store")
        //show dialog
        val  mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.dialogAddStoreBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout
            val storeName = mDialogView.dialogStoreName.text.toString()
            val store = Store("", storeName)
            //set the input text in TextView
            //save store name to database. then close dialog.
            val dbHandler = StoreOpenDBHelper(this, null)
            dbHandler.addStore(store)
            queryAllStores()
            Toast.makeText(this@StoresActivity, "Store added successfully.", Toast.LENGTH_SHORT).show()


        }
        //cancel button click of custom layout
        mDialogView.dialogCancelAddStoreBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }


}
