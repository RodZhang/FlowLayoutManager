package com.rod.flowlayoutmanager.common

/**
 * No pains, no gains.
 *
 * Created by Rod on 2018/6/29.
 */
class DataFactory {
    companion object {
        fun getStringList(size: Int): List<String> {
            val prefixArr = arrayOf("aa", "bbbbbb", "ddddd")
            return (0 until size).map { "${prefixArr[(Math.random() * prefixArr.size).toInt()]}-$it" }
        }
    }
}