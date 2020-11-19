package com.shopify.canna.view.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utils;
import com.shopify.canna.util.VolleyResponse;
import com.shopify.canna.view.login.User_Login;
import com.shopify.graphql.support.Input;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import kotlin.Unit;
import timber.log.Timber;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextInputEditText edt_first_name,edit_last_name,address,apartment,city,country,province,postal,phone_number;
    AppCompatButton save;

    TextInputEditText [] textInputEditTexts;

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

        textInputEditTexts = new TextInputEditText[]{edt_first_name,edit_last_name,address,apartment,city,country,province,postal,phone_number};

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validInput(textInputEditTexts)){
                    try {
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

                        saveAddress(jsonObject,Prefs.INSTANCE.getAccessToken(),new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
                            Storefront.CustomerAddressCreatePayload token= (Storefront.CustomerAddressCreatePayload) success;

                            Toast.makeText(getContext(), "Successfully Save Address", Toast.LENGTH_SHORT).show();
                        }));
                     }catch (Exception e){
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    public boolean validInput(TextInputEditText [] textInputEditTexts){
        boolean validate=true;
        for(TextInputEditText textInputEditText :textInputEditTexts ){
            if(textInputEditText.getText().toString().isEmpty()){
                textInputEditText.setError(textInputEditText.getHint().toString()+"is required field");
                validate=false;
            }
        }
        return validate;
    }

    public void saveAddress(JSONObject dataObject , String customerTokenId , VolleyResponse volleyResponse) throws JSONException {
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
}