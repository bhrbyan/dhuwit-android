package id.dhuwit.core.uikit.divider

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

/* Divider for View Pager */
class UikitDividerMarginItemDecorationViewPager(
    context: Context,
    @DimenRes marginInDp: Int,
    private val isLandscape: Boolean
) :
    RecyclerView.ItemDecoration() {

    private val marginInPx: Int =
        context.resources.getDimension(marginInDp).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (isLandscape) {
            outRect.top = marginInPx
            outRect.bottom = marginInPx
        } else {
            outRect.right = marginInPx
            outRect.left = marginInPx
        }
    }
}