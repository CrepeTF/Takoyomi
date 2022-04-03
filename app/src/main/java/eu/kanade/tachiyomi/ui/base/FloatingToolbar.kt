package eu.kanade.tachiyomi.ui.base

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils
import androidx.core.view.updateLayoutParams
import com.google.android.material.textview.MaterialTextView
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.util.system.getResourceColor

class FloatingToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    BaseToolbar(context, attrs) {

    private val actionColorAlpha = ColorUtils.setAlphaComponent(context.getResourceColor(R.attr.colorOnSurface), 200)

    private val defStyleRes = com.google.android.material.R.style.Widget_Material3_Toolbar

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.Toolbar,
            0,
            defStyleRes
        )
        a.recycle()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onFinishInflate() {
        super.onFinishInflate()
        toolbarTitle = findViewById<MaterialTextView>(R.id.card_title)
        toolbarTitle.setTextAppearance(titleTextAppearance)
        toolbarTitle.setTextColor(actionColorAlpha)

        setNavigationIconTint(actionColorAlpha)
    }

    override fun setCustomTitle(title: CharSequence?) {
        super.setCustomTitle(title)
        toolbarTitle.updateLayoutParams<LinearLayout.LayoutParams> {
            gravity = Gravity.START
        }
    }
}
