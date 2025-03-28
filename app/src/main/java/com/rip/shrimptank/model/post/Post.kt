package com.rip.shrimptank.model.post

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firestore.v1.DeleteDocumentRequest
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity
data class Post(
    @PrimaryKey
    val id: String,
    val type: PostType,
    val userId: String,
    val title: String,
    val text: String,
    var isDeleted: Boolean = false,
    var postImage: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
) : Serializable {
    constructor() : this("", PostType.OTHER, "", "", "")
}

enum class PostType {
    LIVESTOCK,
    PLANTS,
    PARAMETER_TEST,
    PESTS,
    FEEDING,
    MAINTENANCE,
    OTHER
}