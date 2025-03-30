package com.rip.shrimptank.model.user

import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(
    @PrimaryKey
    val id: String,
    val name: String,
    var avatar: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Serializable {
    constructor() : this("", "")
}
