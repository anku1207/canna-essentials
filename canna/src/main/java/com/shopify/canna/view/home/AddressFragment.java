package com.shopify.canna.view.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.constant.ApplicationConstant;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utility;
import com.shopify.canna.util.Utils;
import com.shopify.canna.util.VolleyResponse;
import com.shopify.graphql.support.ID;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import kotlin.Unit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String EXTRAS_ADDRESS_DETAILS = "address_details";
    public static final String EXTRAS_TITLE = "title";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextInputEditText edt_first_name,edit_last_name,address,apartment,city,country,province,postal,phone_number;
    AppCompatButton save;

    TextInputEditText [] textInputEditTexts;
    ProgressBar progress;
    Storefront.MailingAddress collectionEdge = null;

    public AddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_first_name = view.findViewById(R.id.edt_first_name);
        edit_last_name = view.findViewById(R.id.edit_last_name);
        address =view.findViewById(R.id.address);
        apartment =view.findViewById(R.id.apartment);
        city=view.findViewById(R.id.city);
        country=view.findViewById(R.id.country);
        province=view.findViewById(R.id.province);
        postal=view.findViewById(R.id.postal);
        phone_number=view.findViewById(R.id.phone_number);
        save =view.findViewById(R.id.save);
        progress=view.findViewById(R.id.progress);

        TextView title = view.findViewById(R.id.title);
        title.setText("Add Address");

        title.setVisibility(View.GONE);
        ((HomeActivity) getContext()).changeTitleByActivity(getContext(),false,"Add Address",true);
        textInputEditTexts = new TextInputEditText[]{edt_first_name,edit_last_name,address,apartment,city,country,province,postal,phone_number};
        if((getArguments() != null ? getArguments().getSerializable(EXTRAS_ADDRESS_DETAILS) : null) !=null){
            ((HomeActivity) getContext()).changeTitleByActivity(getContext(),false,"Update Address",true);
            title.setText(getArguments().getString(EXTRAS_TITLE) );
            collectionEdge = (Storefront.MailingAddress) getArguments().getSerializable(EXTRAS_ADDRESS_DETAILS);
            setDateInEditText(collectionEdge);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validInput(textInputEditTexts)){
                    try {
                        if (!phone_number.getText().toString().equals("") &&  Utility.validatePattern(phone_number.getText().toString().trim(), ApplicationConstant.MOBILENO_VALIDATION)!=null){
                            phone_number.setError(Utility.validatePattern(phone_number.getText().toString().trim(),ApplicationConstant.MOBILENO_VALIDATION));
                            return;
                        }
                        if(!postal.getText().toString().isEmpty() && postal.getText().toString().length()<6){
                            postal.setError("Postal code accepts only number and length should be 6");
                            return;
                        }

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("firstname",edt_first_name.getText().toString().trim());
                        jsonObject.put("lastname",edit_last_name.getText().toString().trim());
                        jsonObject.put("address",address.getText().toString().trim());
                        jsonObject.put("apartment",apartment.getText().toString().trim());
                        jsonObject.put("city",city.getText().toString().trim());
                        jsonObject.put("country",country.getText().toString().trim());
                        jsonObject.put("province",province.getText().toString().trim());
                        jsonObject.put("postal",postal.getText().toString().trim());
                        jsonObject.put("phone",phone_number.getText().toString().trim());

                        if(collectionEdge!=null ){
                            updateAddress(jsonObject,Prefs.INSTANCE.getAccessToken(),collectionEdge.getId() ,new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
                                Storefront.CustomerAddressUpdatePayload token= (Storefront.CustomerAddressUpdatePayload) success;
                                myDialog(getContext(),"Alert","Update Successfully ","Ok");
                            }));
                        }else {
                            saveAddress(jsonObject,Prefs.INSTANCE.getAccessToken(),new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
                                Storefront.CustomerAddressCreatePayload token= (Storefront.CustomerAddressCreatePayload) success;
                                myDialog(getContext(),"Alert","Successfully Save Address","Ok");
                            }));
                        }
                     }catch (Exception e){
                        Log.w("error",e.getMessage());
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void setDateInEditText( Storefront.MailingAddress collectionEdge) {
        edt_first_name.setText(collectionEdge.getFirstName());
        edit_last_name.setText(collectionEdge.getLastName());
        address.setText(collectionEdge.getAddress1());
        apartment.setText(collectionEdge.getAddress2());
        city.setText(collectionEdge.getCity());
        country.setText(collectionEdge.getCountry());
        province.setText(collectionEdge.getProvince());
        postal.setText(collectionEdge.getZip());
        phone_number.setText(collectionEdge.getPhone());
    }

    public boolean validInput(TextInputEditText [] textInputEditTexts){
        boolean validate=true;
        for(TextInputEditText textInputEditText :textInputEditTexts ){
            if(textInputEditText.getText().toString().isEmpty()){
                textInputEditText.setError(textInputEditText.getHint().toString());
                validate=false;
            }
        }
        return validate;
    }


     public void  updateAddress(JSONObject dataObject , String customerTokenId , ID  addressId, VolleyResponse volleyResponse) throws JSONException {
        progress.setVisibility(View.VISIBLE);


        Log.w("addressID",addressId.toString());

        Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
                .setAddress1(dataObject.getString("address"))
                .setAddress2(dataObject.getString("apartment"))
                .setCity(dataObject.getString("city"))
                .setCountry(dataObject.getString("country"))
                .setFirstName(dataObject.getString("firstname"))
                .setLastName(dataObject.getString("lastname"))
                .setPhone(dataObject.getString("phone"))
                .setProvince(dataObject.getString("province"))
                .setZip(dataObject.getString("postal"));

        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerAddressUpdate(customerTokenId, addressId,input, query -> query
                        .customerAddress(customerAddress -> customerAddress
                                .address1()
                                .address2()
                        )
                        .customerUserErrors(userError -> userError
                                .field()
                                .message()
                        )
                )
        );
        SampleApplication.graphClient().mutateGraph(mutationQuery).enqueue(new Handler(Looper.getMainLooper()), result -> {
            progress.setVisibility(View.GONE);
            if (result instanceof GraphCallResult.Success){
                if (((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData().getCustomerAddressUpdate() != null){
                    Storefront.CustomerAddressUpdatePayload token = Objects.requireNonNull(((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData()).getCustomerAddressUpdate();
                    if (token.getCustomerUserErrors() != null && !token.getCustomerUserErrors().isEmpty()){
                        Utils.INSTANCE.showToast(getContext(), token.getCustomerUserErrors().get(0).getMessage());
                    }else {
                        volleyResponse.onSuccess(token);                    }
                }else{
                    Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
                }
            }else {
                Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
            }
            return Unit.INSTANCE;
        });
    }

    public void saveAddress(JSONObject dataObject , String customerTokenId , VolleyResponse volleyResponse) throws JSONException {
        progress.setVisibility(View.VISIBLE);
        Storefront.MailingAddressInput input = new Storefront.MailingAddressInput()
                .setAddress1(dataObject.getString("address"))
                .setAddress2(dataObject.getString("apartment"))
                .setCity(dataObject.getString("city"))
                .setCountry(dataObject.getString("country"))
                .setFirstName(dataObject.getString("firstname"))
                .setLastName(dataObject.getString("lastname"))
                .setPhone(dataObject.getString("phone"))
                .setProvince(dataObject.getString("province"))
                .setZip(dataObject.getString("postal"));

        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerAddressCreate(customerTokenId, input, query -> query
                        .customerAddress(customerAddress -> customerAddress
                                .address1()
                                .address2()
                        )
                        .userErrors(userError -> userError
                                .field()
                                .message()
                        )
                )
        );
        SampleApplication.graphClient().mutateGraph(mutationQuery).enqueue(new Handler(Looper.getMainLooper()), result -> {
            progress.setVisibility(View.GONE);
            if (result instanceof GraphCallResult.Success){
                if (((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData().getCustomerAddressCreate() != null){
                    Storefront.CustomerAddressCreatePayload token = Objects.requireNonNull(((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData()).getCustomerAddressCreate();
                    if (token.getUserErrors() != null && !token.getUserErrors().isEmpty()){
                        Utils.INSTANCE.showToast(getContext(), token.getUserErrors().get(0).getMessage());
                    }else {
                        volleyResponse.onSuccess(token);                    }
                }else{
                    Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
                }
            }else {
                Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
            }
            return Unit.INSTANCE;
        });
    }

    public static void  myDialog(Context context, String title , String msg , String buttonname){

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ShippingAddressFragment())
                        .commit();
            }
        });
        if(!((Activity)context).isFinishing() && !alertDialog.isShowing())  alertDialog.show();


    }
}