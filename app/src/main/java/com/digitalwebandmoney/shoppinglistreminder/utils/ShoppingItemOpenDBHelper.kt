package com.digitalwebandmoney.shoppinglistreminder.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.digitalwebandmoney.shoppinglistreminder.model.Item
import com.digitalwebandmoney.shoppinglistreminder.model.Store

class ShoppingItemOpenDBHelper(context: Context,
                               factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PURCHASED + " TINYINT,"
                + COLUMN_STORE_FOR_ITEM + " TEXT"
                + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addItem(item: Item) {
        val values = ContentValues()
        values.put(COLUMN_NAME, item.itemTitle)
        values.put(COLUMN_PURCHASED, item.purchased)
        values.put(COLUMN_STORE_FOR_ITEM, item.storeForTheItem)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getAllItems(storeName: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_STORE_FOR_ITEM = \"${storeName}\"", null)
    }
    fun updatePurchasedItem(item: Item){
        val db = this.writableDatabase
        var values = ContentValues()
        values.put(COLUMN_NAME, item.itemTitle)
        values.put(COLUMN_PURCHASED, item.purchased)
        values.put(COLUMN_STORE_FOR_ITEM, item.storeForTheItem)
        val retVal = db.update("$TABLE_NAME", values, "$COLUMN_ID = " + item.id, null)
        if (retVal >= 1) {
            Log.v("@@@WWe", " Record updated")
        } else {
            Log.v("@@@WWe", " Not updated")
        }
        db.close()
    }

    fun updateEditItem(item: Item){
        val db = this.writableDatabase
        var values = ContentValues()
        values.put(COLUMN_NAME, item.itemTitle)
        val retVal = db.update("$TABLE_NAME", values, "$COLUMN_ID = " + item.id, null)
        if (retVal >= 1) {
            Log.v("@@@WWe", " Record updated")
        } else {
            Log.v("@@@WWe", " Not updated")
        }
        db.close()
    }

    fun deleteItem(itemName: String): Boolean {
        var result = false

        val query =
            "SELECT * FROM ${TABLE_NAME} WHERE ${COLUMN_NAME} = \"${itemName}\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(
                TABLE_NAME, COLUMN_ID + " = ?",
                arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    fun isItemPurchased(itemId: String): Boolean{
        var result = false

        val query =
            "SELECT * FROM ${TABLE_NAME} WHERE ${COLUMN_ID} = \"${itemId}\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)
        cursor!!.moveToFirst()
        while (cursor.moveToNext()) {
            val id = (cursor.getString(cursor.getColumnIndex(COLUMN_ID)))
            val itemName = (cursor.getString(cursor.getColumnIndex(COLUMN_NAME)))
            val purchased = (cursor.getString(cursor.getColumnIndex(COLUMN_PURCHASED)))
            val storeName = (cursor.getString(cursor.getColumnIndex(COLUMN_STORE_FOR_ITEM)))

            val item = ShoppingItem(Item(id, purchased.toBoolean(), itemName, storeName))
            result = purchased.toBoolean()
        }
        cursor.close()


        return result
    }
    companion object {
        private val DATABASE_VERSION = 4
        private val DATABASE_NAME = "shoppingItems.db"
        val TABLE_NAME = "items"
        val COLUMN_ID = "id"
        val COLUMN_NAME = "itemName"
        val COLUMN_PURCHASED = "purchased"
        val COLUMN_STORE_FOR_ITEM = "storeForTheItem"

    }
}