package com.duoyou.develop.utils.eventbus

class TabSelectedEvent(index: Int) {
    var tabIndex: Int = index.coerceAtLeast(0)
    var boxTab: Int = -1
}