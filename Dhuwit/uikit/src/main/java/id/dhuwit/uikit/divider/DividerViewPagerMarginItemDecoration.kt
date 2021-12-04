package id.dhuwit.uikit.divider

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class DividerViewPagerMarginItemDecoration(
    private val startMargin: Int,
    private val endMargin: Int,
    private val firstItemMargin: Int,
    private val lastItemMargin: Int
) : RecyclerView.ItemDecoration() {

    constructor(
        context: Context,
        @DimenRes horizontalMargin: Int,
        @DimenRes firstItemMargin: Int,
        @DimenRes lastItemMargin: Int
    ) : this(
        context.resources.getDimensionPixelSize(horizontalMargin),
        context.resources.getDimensionPixelSize(horizontalMargin),
        context.resources.getDimensionPixelSize(firstItemMargin),
        context.resources.getDimensionPixelSize(lastItemMargin)
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        val isFirstItem = position == 0
        val isLastItem = position == itemCount - 1

        outRect.left = if (isFirstItem) firstItemMargin else startMargin
        outRect.right = if (isLastItem) lastItemMargin else endMargin
    }
}