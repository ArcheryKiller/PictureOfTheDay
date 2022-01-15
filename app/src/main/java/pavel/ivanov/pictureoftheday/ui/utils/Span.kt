package pavel.ivanov.pictureoftheday.ui.utils

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.TypefaceSpan
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import pavel.ivanov.pictureoftheday.R

class Span {
    fun initSpan (title: String, date: String, context: Context): SpannableStringBuilder {
        val spanned = SpannableStringBuilder(" $title \n $date")
        val endIndex = title!!.length
        spanned.setSpan(ImageSpan(context, R.drawable.ic_star_24), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanned.setSpan(ImageSpan(context, R.drawable.ic_star_24), endIndex, endIndex+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanned.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.space_red)), 1, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            spanned.setSpan(TypefaceSpan(
                Typeface.create(
                    ResourcesCompat.getFont(
                        context,
                        R.font.roboto_slab
                    ), Typeface.BOLD
                )
            ), 1, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spanned
    }
}