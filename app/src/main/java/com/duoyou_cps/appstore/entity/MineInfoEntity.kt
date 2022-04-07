package com.duoyou_cps.appstore.entity

data class MineInfoEntity(
    val account: String,
    val avatar: String,
    val code: String,
    val coupon_nums: Double,
    val email: String,
    val game_id: Int,
    val game_special_coins: Int,
    val login_type: String,
    val mobile: String,
    val nickname: String,
    val openid: String,
    val payamount: Double,
    val platform_coins: Double,
    val real: Double,
    val real_name: String,
    val special_coins: Double,
    val uid: Int,
    val unionid: String
)