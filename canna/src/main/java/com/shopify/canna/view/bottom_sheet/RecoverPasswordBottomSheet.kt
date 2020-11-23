package com.shopify.canna.view.bottom_sheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shopify.buy3.GraphCallResult
import com.shopify.buy3.Storefront.*
import com.shopify.canna.R
import com.shopify.canna.SampleApplication
import com.shopify.canna.util.Utils
import com.shopify.canna.view.base.BaseRoundedBottomSheetFragment
import kotlinx.android.synthetic.main.bottomsheet_recover_password.*

class RecoverPasswordBottomSheet : BaseRoundedBottomSheetFragment() {

    companion object {
        fun showDialog(fragmentManager: FragmentManager) {
            val dialog = RecoverPasswordBottomSheet()
            dialog.show(fragmentManager, RecoverPasswordBottomSheet::class.simpleName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_recover_password, container, false)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog ->
            val dialogSheet = dialog as BottomSheetDialog
            val bottomSheet =
                    dialogSheet.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
                BottomSheetBehavior.from(bottomSheet).isHideable = true
            }
        }
        return bottomSheetDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_submit.setOnClickListener {
            progress.visibility = View.VISIBLE
            button_submit.visibility = View.GONE
            if (validInput(edt_email.text.toString())) {
                val mutationQuery = mutation { mutation: MutationQuery ->
                    mutation
                            .customerRecover(edt_email.text.toString()
                            ) { query: CustomerRecoverPayloadQuery ->
                                query
                                        .userErrors { userError: UserErrorQuery ->
                                            userError
                                                    .field()
                                                    .message()
                                        }
                            }
                }

                SampleApplication.graphClient().mutateGraph(mutationQuery).enqueue {
                    if (it is GraphCallResult.Success) {
                        if (!it.response.errors.isNullOrEmpty()){
                            Utils.showHideView(progress, View.GONE)
                            Utils.showHideView(button_submit, View.VISIBLE)
                            Utils.showToast(requireContext(), it.response.errors[0].message())
                        }else{
                            val customerRecoverPayload = it.response.data?.customerRecover
                            customerRecoverPayload?.let { recoverPayload ->
                                if (recoverPayload.userErrors.isNotEmpty()) {
                                    Utils.showHideView(progress, View.GONE)
                                    Utils.showHideView(button_submit, View.VISIBLE)
                                    Utils.showToast(requireContext(), recoverPayload.userErrors[0].message)
                                } else {
                                    Utils.showToast(requireContext(), getString(R.string.reset_password_success))
                                    dismissAllowingStateLoss()
                                }
                            }
                        }
                    } else {
                        Utils.showHideView(progress, View.GONE)
                        Utils.showHideView(button_submit, View.VISIBLE)
                        Utils.showToast(requireContext(), getString(R.string.something_wrong))
                    }
                }
            }else{
                button_submit.visibility = View.VISIBLE
                progress.visibility = View.GONE
            }
        }
    }

    private fun validInput(email: String): Boolean {
        if (email.isEmpty() || !email.contains("@")) {
            Utils.showToast(requireContext(), getString(R.string.invalid_input))
            return false
        }
        return true
    }
}