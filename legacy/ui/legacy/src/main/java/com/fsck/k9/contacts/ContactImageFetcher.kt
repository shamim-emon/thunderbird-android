package com.fsck.k9.contacts

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import coil3.ImageLoader
import coil3.decode.DataSource
import coil3.decode.ImageSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import coil3.size.pxOrElse
import com.fsck.k9.mail.Address
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import net.thunderbird.core.logging.Logger
import okio.FileSystem

private const val TAG = "ContactImageFetcher"
private const val DEFAULT_CONTACT_IMAGE_SIZE = 128
private const val PNG_QUALITY = 100

class ContactImageFetcher private constructor(
    private val contactPhotoLoader: ContactPhotoLoader,
    private val contactLetterBitmapCreator: ContactLetterBitmapCreator,
    private val data: ContactImageModel,
    private val options: Options,
    private val logger: Logger,
) : Fetcher {

    override suspend fun fetch(): FetchResult? = withContext(Dispatchers.IO) {
        val width = options.size.width.pxOrElse { DEFAULT_CONTACT_IMAGE_SIZE }
        val height = options.size.height.pxOrElse { DEFAULT_CONTACT_IMAGE_SIZE }

        val bitmap: Bitmap = try {
            loadContactPhoto(width, height) ?: createContactLetterBitmap(width, height)
        } catch (e: IOException) {
            logger.error(TAG, throwable = e) { "Failed to load contact photo: ${e.message}" }
            createContactLetterBitmap(width, height)
        } catch (e: IllegalArgumentException) {
            logger.error(TAG, throwable = e) { "Failed to scale bitmap: ${e.message}" }
            createContactLetterBitmap(width, height)
        }

        val buffer = okio.Buffer().apply {
            bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, outputStream())
        }

        val imageSource = ImageSource(buffer, FileSystem.SYSTEM)

        SourceFetchResult(
            source = imageSource,
            mimeType = "image/png",
            dataSource = DataSource.DISK,
        )
    }

    private fun loadContactPhoto(width: Int, height: Int): Bitmap? {
        if (data.contactLetterOnly) return null
        return contactPhotoLoader.loadContactPhoto(data.address.address)?.scale(width, height)
    }

    private fun createContactLetterBitmap(width: Int, height: Int): Bitmap {
        val bitmap = createBitmap(width, height)
        return contactLetterBitmapCreator.drawBitmap(bitmap, width, Address(data.address))
    }

    class Factory(
        private val contactPhotoLoader: ContactPhotoLoader,
        private val contactLetterBitmapCreator: ContactLetterBitmapCreator,
        private val logger: Logger,
    ) : Fetcher.Factory<ContactImageModel> {

        override fun create(data: ContactImageModel, options: Options, imageLoader: ImageLoader): Fetcher {
            return ContactImageFetcher(
                contactPhotoLoader,
                contactLetterBitmapCreator,
                data,
                options,
                logger,
            )
        }
    }
}

data class ContactImageModel(
    val address: Address,
    val contactLetterOnly: Boolean = false,
)
