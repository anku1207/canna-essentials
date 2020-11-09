package com.shopify.canna.Interface;

public class DateInterface {

    private OnSuccess onSuccess;

    public DateInterface(OnSuccess onSuccess){
        this.onSuccess  = onSuccess;
    }


    public void onSuccess(String s){
        onSuccess.onSuccess(s);
    }


    public interface OnSuccess {
        void onSuccess(String s);
    }

}
