package com.rod.uidemo.permission

/**
 *
 * @author Rod
 * @date 2019/6/3
 */
data class Permission(val opStr: String, var granted: Boolean, var showRational: Boolean = false)