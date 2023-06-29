package com.example.emstrainer.adapters
/*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.emstrainer.R

abstract class OLDRecyclerItemLeftSwipeListenener(
    context: Context?,
    dragDir: Int,
    swipeDir: Int
) : ItemTouchHelper.SimpleCallback(dragDir, swipeDir) {
    private val background = ColorDrawable(Color.RED)
    @SuppressLint("UseCompatLoadingForDrawables")
    private val iconDelete = context?.getDrawable(R.drawable.ic_baseline_delete_white_55)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        background.setBounds(
            viewHolder.itemView.right + dX.toInt(),
            viewHolder.itemView.top,
            viewHolder.itemView.right,
            viewHolder.itemView.bottom
        )
        background.draw(canvas)

        val intrinsicHeight = iconDelete?.intrinsicHeight
        val intrinsicWidth = iconDelete?.intrinsicWidth
        val itemHeight: Int = viewHolder.itemView.height
        val deleteIconTop: Int = viewHolder.itemView.top + (itemHeight - intrinsicHeight!!) / 2
        val deleteIconMargin: Int = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft: Int = viewHolder.itemView.right - deleteIconMargin - intrinsicWidth!!
        val deleteIconRight: Int = viewHolder.itemView.right - deleteIconMargin
        val deleteIconBottom: Int = deleteIconTop + intrinsicHeight
        iconDelete?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        iconDelete?.draw(canvas);
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
}*/