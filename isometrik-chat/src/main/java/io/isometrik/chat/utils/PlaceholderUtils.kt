package io.isometrik.chat.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import io.isometrik.chat.R
import io.isometrik.ui.messages.chat.common.ChatConfig
import java.util.Locale

/**
 * The helper class to generate dynamic placeholders using initials of text, code to check if image
 * url is valid.
 */
object PlaceholderUtils {
    /**
     * The constant BITMAP_HEIGHT.
     */
    const val BITMAP_HEIGHT: Int = 30

    /**
     * The constant BITMAP_WIDTH.
     */
    const val BITMAP_WIDTH: Int = 30

    /**
     * Is valid image url boolean.
     *
     * @param url the url
     * @return the boolean
     */
    @JvmStatic
    fun isValidImageUrl(url: String?): Boolean {
        return !url.isNullOrEmpty() && url != ChatConfig.DEFAULT_PLACEHOLDER_IMAGE_URL
    }


    /**
     * Sets text round drawable.
     *
     * @param context the context
     * @param firstName the first name
     * @param ivProfile the iv profile
     * @param position the position
     * @param fontSize the font size
     */
    @JvmStatic
    fun setTextRoundDrawable(
        context: Context, firstName: String?, ivProfile: ImageView,
        position: Int, fontSize: Int
    ) {
        val initials: String = extractInitials(firstName)
        try {
            val density = context.resources.displayMetrics.density
            ivProfile.setImageDrawable(
                TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(((fontSize) * density).toInt())
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(
                        initials,
                        ColorsUtil.getThemeColor()
                    )
            )
        } catch (ignore: Exception) {
        }
    }

    /**
     * Sets text round drawable.
     *
     * @param context the context
     * @param firstName the first name
     * @param ivProfile the iv profile
     * @param fontSize the font size
     */
    @JvmStatic
    fun setTextRoundDrawable(
        context: Context, firstName: String?, ivProfile: ImageView,
        fontSize: Int
    ) {
        val initials = extractInitials(firstName)

        try {
            val density = context.resources.displayMetrics.density
            ivProfile.setImageDrawable(
                TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(((fontSize) * density).toInt())
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(
                        initials,
                        ColorsUtil.getThemeColor()
                    )
            )
        } catch (ignore: Exception) {
        }
    }

    /**
     * Extracts initials from the full name, using the first character from the first two words.
     */
    private fun extractInitials(fullName: String?): String {
        if (!fullName.isNullOrBlank()) {
            val names = fullName.trim()
                .split("\\s+".toRegex())
                .filter { it.isNotBlank() }

            return if (names.size >= 2) {
                (names[0][0].toString() + names[1][0]).uppercase(Locale.getDefault())
            } else if (names.size == 1) {
                val firstName = names[0]
                if (firstName.length >= 2) {
                    firstName.substring(0, 2).uppercase(Locale.getDefault())
                } else if (firstName.isNotEmpty()) {
                    firstName[0].toString().uppercase(Locale.getDefault())
                } else {
                    "#" // <- make sure this exists
                }
            } else {
                "#" // <- also needed if size is 0
            }
        }
        return "#"
    }






    /**
     * Sets text round rectangle drawable.
     *
     * @param context the context
     * @param firstName the first name
     * @param ivProfile the iv profile
     * @param fontSize the font size
     * @param radius the radius
     */
    @JvmStatic
    fun setTextRoundRectangleDrawable(
        context: Context, firstName: String?,
        ivProfile: ImageView, fontSize: Int, radius: Int
    ) {
        var initials = ""
        if (firstName != null) {
            initials = if (firstName.length >= 2) {
                firstName.substring(0, 2)
            } else {
                initials + firstName[0]
            }
        }
        try {
            val density = context.resources.displayMetrics.density
            ivProfile.setImageDrawable(
                TextDrawable.builder()
                    .beginConfig() //.height((int) ((100) * density))
                    //.width((int) ((100) * density))
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(((fontSize) * density).toInt())
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRoundRect(
                        initials, ContextCompat.getColor(context, R.color.ism_contact_background),
                        ((radius) * density).toInt()
                    )
            )
        } catch (ignore: Exception) {
        }
    }

    /**
     * Gets bitmap.
     *
     * @param context the context
     * @param firstName the first name
     * @param timestamp the timestamp
     * @return the bitmap
     */
    fun getBitmap(context: Context, firstName: String?, timestamp: Long): Bitmap? {
        var initials = ""
        if (firstName != null) {
            initials = if (firstName.length >= 2) {
                firstName.substring(0, 2)
            } else {
                initials + firstName[0]
            }
        }
        try {
            val density = context.resources.displayMetrics.density
            val drawable: Drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(((BITMAP_WIDTH / 3) * density).toInt())
                .height(((BITMAP_HEIGHT) * density).toInt())
                .width(((BITMAP_WIDTH) * density).toInt())
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(
                    initials,
                    ColorsUtil.getThemeColor()
                )
            return drawableToBitmap(drawable)
        } catch (ignore: Exception) {
        }
        return null
    }

    /**
     * Drawable to bitmap bitmap.
     *
     * @param drawable the drawable
     * @return the bitmap
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }

        val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1, 1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}