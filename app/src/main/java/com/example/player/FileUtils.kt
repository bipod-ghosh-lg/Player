package com.example.player

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

object FileUtils {
    fun getDecryptedOutputFile(context: Context, fileName: String): File {
        val dir = File(context.getExternalFilesDir(null), "decrypted_videos")
        if (!dir.exists()) dir.mkdirs()

        // Replace .aes with .mp4 or append .mp4
        val cleanName = fileName.removeSuffix(".aes") + ".mp4"
        return File(dir, cleanName)
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        returnCursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            return it.getString(nameIndex)
        }
        return null
    }
}