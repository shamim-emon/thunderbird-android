package com.fsck.k9.contacts

import coil3.key.Keyer
import coil3.request.Options

class ContactImageKeyer : Keyer<ContactImageModel> {
    override fun key(data: ContactImageModel, options: Options): String {
        return "${data.address}_${data.contactLetterOnly}"
    }
}
