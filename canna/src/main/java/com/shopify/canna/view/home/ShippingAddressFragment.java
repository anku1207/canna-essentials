package com.shopify.canna.view.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utils;
import com.shopify.canna.util.VolleyResponse;

import java.util.List;

import kotlin.Unit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShippingAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingAddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;

    public ShippingAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShippingAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShippingAddressFragment newInstance(String param1, String param2) {
        ShippingAddressFragment fragment = new ShippingAddressFragment();
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
        return inflater.inflate(R.layout.fragment_shipping_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        getCustomerAddressList(Prefs.INSTANCE.getAccessToken(),new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
            List<Storefront.MailingAddressEdge> customerAddress = (List<Storefront.MailingAddressEdge>) success;

            Log.w("customerAddress",new Gson().toJson(customerAddress));
        }));

    }

    public void getCustomerAddressList(String tokenId, VolleyResponse volleyResponse){
        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(tokenId, customer -> customer
                        .addresses(arg -> arg.first(10), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .address1()
                                                .address2()
                                                .city()
                                                .province()
                                                .country()
                                        )
                                )
                        )
                )
        );

        SampleApplication.graphClient().queryGraph(query).enqueue(new Handler(Looper.getMainLooper()), result -> {
            if (result instanceof GraphCallResult.Success){

                if (((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse() != null){
                    List<Storefront.MailingAddressEdge> customer= ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getCustomer().getAddresses().getEdges();
                    volleyResponse.onSuccess(customer);
                }else {
                    Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
                }
            }else {
                Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
            }
            return Unit.INSTANCE;
        });
    }
}