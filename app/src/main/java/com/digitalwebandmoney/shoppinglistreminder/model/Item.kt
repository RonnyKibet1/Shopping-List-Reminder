package com.digitalwebandmoney.shoppinglistreminder.model

//Shopping item model
class Item (id: String, purchased: Boolean, itemTitle: String, storeForTheItem: String){

    var id: String = id
    var purchased: Boolean = purchased
    var itemTitle: String = itemTitle
    var storeForTheItem: String = storeForTheItem

    constructor():this("", false, "", ""){
    }
}