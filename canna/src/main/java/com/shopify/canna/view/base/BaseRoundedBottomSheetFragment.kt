package com.shopify.canna.view.base

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopify.canna.R

open class BaseRoundedBottomSheetFragment: BottomSheetDialogFragment(){

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()

        view?.let {
            it.post {
                val parent = it.parent as View
                parent.background = ContextCompat.getDrawable(parent.context, R.drawable.bg_bottom_sheet)
            }
        }
    }

}