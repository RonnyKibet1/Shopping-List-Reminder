package com.digitalwebandmoney.shoppinglistreminder.model

//Store model
class Store(id:String, storeName: String) {
    var id: String = id
    var storeName:String = storeName

    constructor():this("", ""){
    }
}