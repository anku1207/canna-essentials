package com.shopify.canna.Interface;

import android.app.Dialog;

public class ConfirmationDialogInterface {
    private OnOk onOk;
    private OnCancel onCancel;


    public ConfirmationDialogInterface(OnOk onOk, OnCancel onCancel) {
        this.onOk = onOk;
        this.onCancel = onCancel;
    }

    public ConfirmationDialogInterface(OnOk onOk) {
        this.onOk = onOk;
    }

    public void onOk(Dialog s) {
        onOk.onOk(s);
    }

    public void onCancel(Dialog s) {
        onCancel.onCancel(s);
    }

    public interface OnOk {

        void onOk(Dialog s);
    }

    public interface OnCancel {
        void onCancel(Dialog s) ;
    }
}