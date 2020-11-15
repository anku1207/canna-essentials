package com.shopify.canna.view

import android.graphics.Typeface
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

object ViewExtensions {

    fun Toolbar.changeToolbarFont(){
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is TextView && view.text == title) {
                view.typeface = Typeface.createFromAsset(view.context.assets, "font/sweetsanspromedium")
                break
            }
        }
    }
}