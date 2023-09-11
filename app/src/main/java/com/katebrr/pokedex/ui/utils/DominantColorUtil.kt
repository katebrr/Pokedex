package com.katebrr.pokedex.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

suspend fun getDominantColor(imageURL: String): Int? {
    return withContext(Dispatchers.IO) {
        try {
            val image = URL(imageURL)
            val connection = image.openConnection()
            connection.connect()

            val contentLength = connection.contentLength
            val input = connection.getInputStream().buffered()

            // Decode the input stream into a Bitmap
            val options = BitmapFactory.Options().apply {
                inSampleSize = 1
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }
            val bitmap = BitmapFactory.decodeStream(input, null, options)

            // Generate a palette from the bitmap and get the dominant color
            Palette.from(bitmap!!).generate().dominantSwatch?.rgb

        } catch (e: Exception) {
            null
        }
    }
}