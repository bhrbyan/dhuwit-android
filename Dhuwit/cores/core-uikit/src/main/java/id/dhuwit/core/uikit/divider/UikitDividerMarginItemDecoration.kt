package id.dhuwit.core.uikit.divider

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import androidx.recyclerview.widget.DividerItemDecoration

/* Add margin start and end */
class UikitDividerMarginItemDecoration(
    context: Context,
    orientation: Int,
    margin: Int
) : DividerItemDecoration(context, orientation) {

    init {
        val attributes: IntArray = intArrayOf(android.R.attr.listDivider)
        val attribute: TypedArray = context.obtainStyledAttributes(attributes)
        val divider: Drawable? = attribute.getDrawable(0)
        val marginDivider = InsetDrawable(divider, margin, 0, margin, 0)
        attribute.recycle()
        setDrawable(marginDivider)
    }

}