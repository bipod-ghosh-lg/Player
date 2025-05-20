package com.example.player

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object VideoDecryptor {

    private const val KEY_STRING = "1234567890123456"
    private const val IV_STRING = "abcdefghijklmnop"
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"

    private val KEY = KEY_STRING.toByteArray(Charsets.UTF_8)
    private val IV = IV_STRING.toByteArray(Charsets.UTF_8)

    fun decryptToFile(context: Context, inputUri: Uri, outputFile: File, onDone: (Boolean) -> Unit) {
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            val keySpec = SecretKeySpec(KEY, "AES")
            val ivSpec = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

            val inputStream: InputStream = context.contentResolver.openInputStream(inputUri)
                ?: throw Exception("Invalid input URI")

            val cipherInputStream = CipherInputStream(inputStream, cipher)
            val outputStream: OutputStream = outputFile.outputStream()

            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (cipherInputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            cipherInputStream.close()
            outputStream.flush()
            outputStream.close()

            onDone(true)
        } catch (e: Exception) {
            Log.e("Decrypt", "Decryption failed: ${e.message}", e)
            onDone(false)
        }
    }
}