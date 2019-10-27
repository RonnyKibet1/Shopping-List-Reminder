package com.digitalwebandmoney.shoppinglistreminder.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.digitalwebandmoney.shoppinglistreminder.model.Store

class StoreOpenDBHelper(context: Context,
                        factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME,
        factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME
                + " TEXT" + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addStore(store: Store) {
        val values = ContentValues()
        values.put(COLUMN_NAME, store.storeName)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getAllStores(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
    fun deleteStore(store: Store): Boolean {
        var result = false

        val query =
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME = \"${store.storeName}\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "stores.db"
        val TABLE_NAME = "stores"
        val COLUMN_ID = "id"
        val COLUMN_NAME = "storeName"
    }
}