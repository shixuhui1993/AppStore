package com.duoyou.develop.utils.eventbus

data class ChangeUserEvent(val userToken: String){
    companion object{
        var currentIsChangeUser = false
    }


}