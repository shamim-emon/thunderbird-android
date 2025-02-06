package com.fsck.k9.activity

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

internal class CaptureImageFileWriter(private val context: Context){

    fun getFileUri() : Uri {
        val file = getCaptureImageFile()
        return FileProvider.getUriForFile(context, "${context.packageName}.activity", file)
    }

    private fun getCaptureImageFile(): File {
        return File(getDirectory(), FILENAME)
    }

    private fun getDirectory(): File {
        val directory = File(context.filesDir, DIRECTORY_NAME)
        directory.mkdirs()

        return directory
    }

    companion object {
        private const val DIRECTORY_NAME = "captureImage"
        private const val FILENAME = "captureImage.jpg"
    }
}
