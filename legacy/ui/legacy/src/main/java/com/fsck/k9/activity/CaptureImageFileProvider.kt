package com.fsck.k9.activity

import androidx.core.content.FileProvider

/**
 * Used to expose account information read from QR codes via a content URI to the settings import code.
 */
class CaptureImageFileProvider : FileProvider()
