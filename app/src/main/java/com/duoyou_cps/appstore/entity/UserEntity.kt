package com.duoyou_cps.appstore.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

 class UserEntity : BaseObservable() {
     private var name: String? = null
    var age: Int? = null

//    @Bindable
    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
        notifyChange()
    }


}
