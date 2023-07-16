package com.interview.diagnal.main

import android.R.attr.spacing
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Charles Raj I on 09/07/23.
 * @author Charles Raj I
 */
class GridSpaceItemDecorator(private val spanCount: Int,private val spacing: Int, private val includeEdge: Boolean) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column: Int = position % spanCount

        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount
            outRect.right =
                (column + 1) * spacing / spanCount
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right =
                spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}