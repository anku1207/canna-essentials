package com.shopify.canna.view.bottom_sheet

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shopify.buy3.GraphCallResult
import com.shopify.buy3.Storefront.*
import com.shopify.canna.R
import com.shopify.canna.SampleApplication
import com.shopify.canna.util.Utils
import com.shopify.canna.view.base.BaseRoundedBottomSheetFragment
import com.shopify.canna.view.webview.WebViewActivity
import kotlinx.android.synthetic.main.bottomsheet_create_account.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateAccountBottomSheet : BaseRoundedBottomSheetFragment() {

    companion object {
        fun showDialog(fragmentManager: FragmentManager) {
            val dialog = CreateAccountBottomSheet()
            dialog.show(fragmentManager, CreateAccountBottomSheet::class.simpleName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_create_account, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpannableString()

        button_create_account.setOnClickListener {
            progress.visibility = View.VISIBLE
            if (validInput()) {
                val input = CustomerCreateInput(edt_email.text.toString(), edt_password.text.toString())
                        .setFirstName(edt_first_name.text.toString())
                        .setLastName(edt_last_name.text.toString())
                        .setAcceptsMarketing(true)

                val mutationQuery = mutation { mutation: MutationQuery ->
                    mutation
                            .customerCreate(input
                            ) { query: CustomerCreatePayloadQuery ->
                                query
                                        .customer { customer: CustomerQuery ->
                                            customer
                                                    .id()
                                                    .email()
                                                    .firstName()
                                                    .lastName()
                                        }
                                        .userErrors { userError: UserErrorQuery ->
                                            userError
                                                    .field()
                                                    .message()
                                        }
                            }
                }

                SampleApplication.graphClient().mutateGraph(mutationQuery).enqueue { graphCallResult ->
                    if (graphCallResult is GraphCallResult.Success) {
                        val customerCreatePayloadQuery = graphCallResult.response.data?.customerCreate
                        customerCreatePayloadQuery?.let { customerCreatePayload ->
                            if (!customerCreatePayload.userErrors.isNullOrEmpty()) {
                                Utils.showToast(requireContext(), customerCreatePayload.userErrors[0].message)
                            } else {
                                Utils.showToast(requireContext(), getString(R.string.sign_up_success))
                                dismissAllowingStateLoss()
                            }
                        }
                    } else {
                        Utils.showToast(requireContext(), getString(R.string.something_wrong))
                    }
                    progress.visibility = View.GONE
                }
            }else{
                progress.visibility = View.GONE
            }
        }

    }

    private fun setSpannableString() {
        val initialText = SpannableStringBuilder(text_terms_and_conditions.text.toString())

        initialText.append(getString(R.string.privacy_policy))
        initialText.setSpan(object : ClickableSpan(){
            override fun onClick(widget: View) {
                WebViewActivity.launchActivity(startingActivity = requireActivity(), title = getString(R.string.privacy_policy), url = getString(R.string.privacy_policy_url))
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#2C66E3")
                text_terms_and_conditions.invalidate()
            }

        }, initialText.length - getString(R.string.privacy_policy).length, initialText.length, 0)

        initialText.append(" and ")
        initialText.append(getString(R.string.terms_conditions_text))
        initialText.setSpan(object : ClickableSpan(){
            override fun onClick(widget: View) {
                WebViewActivity.launchActivity(startingActivity = requireActivity(), title = getString(R.string.terms_conditions_text), url = getString(R.string.terms_url))
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#2C66E3")
                text_terms_and_conditions.invalidate()
            }

        }, initialText.length - getString(R.string.terms_conditions_text).length, initialText.length, 0)

        text_terms_and_conditions.movementMethod = LinkMovementMethod.getInstance()
        text_terms_and_conditions.setText(initialText, TextView.BufferType.SPANNABLE)
    }

    private fun validInput(): Boolean {
        if (edt_first_name.text.isNullOrEmpty()) {
            Utils.showToast(requireContext(), getString(R.string.first_name_validation))
            return false
        }


        if (edt_last_name.text.isNullOrEmpty()) {
            Utils.showToast(requireContext(), getString(R.string.last_name_validation))
            return false
        }



        if (edt_email.text.isNullOrEmpty() || (edt_email.text != null && !edt_email.text?.contains("@")!!)) {
            Utils.showToast(requireContext(), getString(R.string.email_validation))
            return false
        }


        if (edt_password.text.isNullOrEmpty() || edt_password.text?.length ?: 0 < 6) {
            Utils.showToast(requireContext(), getString(R.string.password_validation))
            return false
        }

        return true
    }
}