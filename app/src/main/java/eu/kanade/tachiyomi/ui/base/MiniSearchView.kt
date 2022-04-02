package eu.kanade.tachiyomi.ui.base

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.ColorUtils
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.util.system.getResourceColor

@RequiresApi(Build.VERSION_CODES.M)
class MiniSearchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    SearchView(context, attrs) {

    init {
        val searchTextView =
            findViewById<SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
        searchTextView?.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
        val actionColorAlpha =
            ColorUtils.setAlphaComponent(context.getResourceColor(R.attr.colorOnSurface), 200)
        searchTextView?.setTextColor(actionColorAlpha)
        searchTextView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        searchTextView?.setHintTextColor(actionColorAlpha)
    }
}
