package com.shopify.canna.view.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utils;
import com.shopify.canna.util.VolleyResponse;
import com.shopify.canna.view.base.ShippingAddressRecyclerViewAdapter;
import com.shopify.graphql.support.ID;

import java.util.List;
import java.util.Objects;

import kotlin.Unit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShippingAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingAddressFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    TextView add_address;
    Animation animation;
    ProgressBar progress;

    QueryGraphCall dd;

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
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.imageviewclickeffect);

        recyclerView=view.findViewById(R.id.recyclerView);
        add_address=view.findViewById(R.id.add_address);
        progress=view.findViewById(R.id.progress);

        add_address.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);


        progress.setVisibility(View.VISIBLE);
        getCustomerAddressList(Prefs.INSTANCE.getAccessToken(),new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
            progress.setVisibility(View.GONE);
            List<Storefront.MailingAddressEdge> customerAddress = (List<Storefront.MailingAddressEdge>) success;
            ShippingAddressRecyclerViewAdapter s = new ShippingAddressRecyclerViewAdapter(getActivity(),customerAddress,R.layout.shipping_address_design,ShippingAddressFragment.this);
            recyclerView.setAdapter(s);
            recyclerView.getAdapter().notifyDataSetChanged();
        }));
    }

    public void getCustomerAddressList(String tokenId, VolleyResponse volleyResponse){
        progress.setVisibility(View.VISIBLE);
        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(tokenId, customer -> customer
                        .addresses(arg -> arg.first(30), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .address1()
                                                .address2()
                                                .city()
                                                .province()
                                                .country()
                                                .firstName()
                                                .lastName()
                                                .phone()
                                                .zip()
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
                    progress.setVisibility(View.GONE);
                    Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
                }
            }else {
                progress.setVisibility(View.GONE);
                Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
            }
            return Unit.INSTANCE;
        });
    }



    public  void removeCustomerAddress(String accessId , ID addressId){
        progress.setVisibility(View.VISIBLE);
        Storefront.MutationQuery mutationQuery = Storefront.mutation(mutation -> mutation
                .customerAddressDelete(addressId, accessId,query -> query
                        .customerUserErrors(userError -> userError
                                .field()
                                .message()
                        )
                        .deletedCustomerAddressId()
                )
        );
        SampleApplication.graphClient().mutateGraph(mutationQuery).enqueue(new Handler(Looper.getMainLooper()), result -> {
            progress.setVisibility(View.GONE);
            if (result instanceof GraphCallResult.Success){
                if (((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData().getCustomerAddressDelete() != null){
                    Storefront.CustomerAddressDeletePayload token = Objects.requireNonNull(((GraphCallResult.Success<Storefront.Mutation>) result).getResponse().getData()).getCustomerAddressDelete();
                    if (token.getCustomerUserErrors() != null && !token.getCustomerUserErrors().isEmpty()){
                        Utils.INSTANCE.showToast(getContext(), token.getCustomerUserErrors().get(0).getMessage());
                    }else {
                        myDialog(getActivity(),"Alert","Remove SuccessFully","Ok" );                   }
                }else{
                    Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
                }
            }else {
                Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
            }
            return Unit.INSTANCE;
        });
    }


    public  void  myDialog(Context context, String title , String msg , String buttonname ){

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonname, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getCustomerAddressList(Prefs.INSTANCE.getAccessToken(),new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
                    progress.setVisibility(View.GONE);
                    List<Storefront.MailingAddressEdge> customerAddress = (List<Storefront.MailingAddressEdge>) success;
                    ShippingAddressRecyclerViewAdapter s = new ShippingAddressRecyclerViewAdapter(getActivity(),customerAddress,R.layout.shipping_address_design,ShippingAddressFragment.this);
                    recyclerView.setAdapter(s);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }));
            }
        });
        if(!((Activity)context).isFinishing() && !alertDialog.isShowing())  alertDialog.show();


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_address:
                add_address.startAnimation(animation);
                ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AddressFragment())
                        .addToBackStack(null)
                        .commit();
                break;
        }

    }
}