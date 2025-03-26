package com.rip.shrimptank.model

import android.content.Context
import android.graphics.Bitmap
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.policy.GlobalUploadPolicy
import com.cloudinary.android.policy.UploadPolicy
import com.rip.shrimptank.ShrimpTank
import java.io.File
import java.io.FileOutputStream

class Cloudinary private constructor() {

    companion object {
        val shared = Cloudinary()
    }

    init {
        val config = mapOf(
            "cloud_name" to "dj6vs7wh5",
            "api_key" to "487468159787585",
            "api_secret" to "KPLrfncLx7iL5Led2QVa0tPqeLY"
        )

        ShrimpTank.Globals.context?.let {
            MediaManager.init(it, config)
            MediaManager.get().globalUploadPolicy = GlobalUploadPolicy.Builder()
                .maxConcurrentRequests(3)
                .networkPolicy(UploadPolicy.NetworkType.UNMETERED)
                .build()
        }
    }

    fun uploadBitmap(
        bitmap: Bitmap,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val context = ShrimpTank.Globals.context ?: return
        val file = bitmapToFile(bitmap, context)

        MediaManager.get().upload(file.path)
            .option("folder", "images")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val publicUrl = resultData["secure_url"] as? String ?: ""
                    onSuccess(publicUrl)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    onError(error?.description ?: "Unknown error")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()
    }

    private fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return file
    }

    fun add(user: User, profileImage: Bitmap?, storage: Storage, callback: () -> Unit) {
        profileImage?.let {
            when (storage) {
                Storage.CLOUDINARY -> {
                    uploadBitmap(it, { url ->
                        val updatedUser = user.copy(avatar = url)
                        // Save updated user to Firebase or local DB (if needed)
                        callback()
                    }, { callback() })
                }
            }
        } ?: callback()
    }

    enum class Storage {
        CLOUDINARY
    }
}
