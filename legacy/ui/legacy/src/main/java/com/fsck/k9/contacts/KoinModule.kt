package com.fsck.k9.contacts

import coil3.ImageLoader
import coil3.request.crossfade
import org.koin.dsl.module

val contactsModule = module {
    single { ContactLetterExtractor() }
    factory { ContactLetterBitmapConfig(context = get(), themeManager = get(), generalSettingsManager = get()) }
    factory { ContactLetterBitmapCreator(letterExtractor = get(), config = get()) }
    factory { ContactPhotoLoader(contentResolver = get(), contactRepository = get()) }
    factory { ContactPictureLoader(context = get(), contactLetterBitmapCreator = get()) }
    factory { ContactImageBitmapDecoderFactory(contactPhotoLoader = get()) }
    single { ContactImageKeyer() }
    single {
        ImageLoader.Builder(context = get())
            .components {
                add(get<ContactImageKeyer>())
                add(
                    ContactImageFetcher.Factory(
                        contactPhotoLoader = get(),
                        contactLetterBitmapCreator = get(),
                        logger = get(),
                    ),
                )
            }
            .crossfade(true)
            .build()
    }
}
