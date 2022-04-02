package eu.kanade.tachiyomi.ui.base

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.appbar.MaterialToolbar
import eu.kanade.tachiyomi.R

open class BaseToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    MaterialToolbar(context, attrs) {

    protected lateinit var toolbarTitle: TextView
    private val defStyleRes = com.google.android.material.R.style.Widget_Material3_Toolbar

    protected val titleTextAppearance: Int

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.Toolbar,
            0,
            defStyleRes
        )
        titleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0)
        a.recycle()
    }

    override fun setTitle(resId: Int) {
        setCustomTitle(context.getString(resId))
    }

    override fun setTitle(title: CharSequence?) {
        setCustomTitle(title)
    }

    protected open fun setCustomTitle(title: CharSequence?) {
        toolbarTitle.isVisible = true
        toolbarTitle.text = title
        super.setTitle(null)
    }
}
