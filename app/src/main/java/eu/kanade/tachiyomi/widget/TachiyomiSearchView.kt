package eu.kanade.tachiyomi.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.ColorUtils
import androidx.core.view.inputmethod.EditorInfoCompat
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import eu.kanade.tachiyomi.util.preference.asImmediateFlow
import eu.kanade.tachiyomi.util.system.getResourceColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * A custom [SearchView] that sets [EditorInfoCompat.IME_FLAG_NO_PERSONALIZED_LEARNING] to imeOptions
 * if [PreferencesHelper.incognitoMode] is true. Some IMEs may not respect this flag.
 */
class TachiyomiSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.searchViewStyle
) : SearchView(context, attrs, defStyleAttr) {

    init {
        val searchTextView =
            findViewById<SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
        searchTextView?.setTextAppearance(android.R.style.TextAppearance_Material_Body1)
        val actionColorAlpha =
            ColorUtils.setAlphaComponent(context.getResourceColor(R.attr.colorOnSurface), 200)
        searchTextView?.setTextColor(actionColorAlpha)
        searchTextView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        searchTextView?.setHintTextColor(actionColorAlpha)

        val searchPlateView = findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlateView.setBackgroundColor(Color.TRANSPARENT)

        setIconifiedByDefault(false)

        val searchMagIconImageView = findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchMagIconImageView.layoutParams = LinearLayout.LayoutParams(0, 0)
    }

    private var scope: CoroutineScope? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        Injekt.get<PreferencesHelper>().incognitoMode().asImmediateFlow {
            imeOptions = if (it) {
                imeOptions or EditorInfoCompat.IME_FLAG_NO_PERSONALIZED_LEARNING
            } else {
                imeOptions and EditorInfoCompat.IME_FLAG_NO_PERSONALIZED_LEARNING.inv()
            }
        }.launchIn(scope!!)
    }

    override fun setOnQueryTextListener(listener: OnQueryTextListener?) {
        super.setOnQueryTextListener(listener)
        val searchAutoComplete: SearchAutoComplete = findViewById(R.id.search_src_text)
        searchAutoComplete.setOnEditorActionListener { _, actionID, _ ->
            if (actionID == EditorInfo.IME_ACTION_SEARCH || actionID == EditorInfo.IME_NULL) {
                clearFocus()
                listener?.onQueryTextSubmit(query.toString())
                true
            } else false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scope?.cancel()
        scope = null
    }
}
