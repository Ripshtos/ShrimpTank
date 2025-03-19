package com.rip.shrimptank.model

import java.io.Serializable

data class User(
    var id: String = "",
    var name: String = "",
    val email: String = "",
    var avatar: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Serializable
