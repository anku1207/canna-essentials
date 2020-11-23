package com.shopify.canna.view.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utils;
import com.shopify.canna.util.VolleyResponse;
import com.shopify.canna.view.base.OrderListRecyclerViewAdapter;
import com.shopify.canna.view.base.ShippingAddressRecyclerViewAdapter;

import java.util.List;

import kotlin.Unit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    ProgressBar progress;
    LinearLayout linearLayoutNoOrders;
    AppCompatButton buttonContinueShopping;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.recyclerView);
        progress=view.findViewById(R.id.progress);
        linearLayoutNoOrders = view.findViewById(R.id.linear_no_orders);
        buttonContinueShopping = view.findViewById(R.id.button_continue_shopping);

        ((HomeActivity) getContext()).changeTitleByActivity(getContext(),false,"My Order");


        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);


        getOrderList(Prefs.INSTANCE.getAccessToken(),new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
            List<Storefront.OrderEdge> customer  = (List<Storefront.OrderEdge>) success;
            if (customer == null || customer.isEmpty()){
                linearLayoutNoOrders.setVisibility(View.VISIBLE);
            }else {
                linearLayoutNoOrders.setVisibility(View.GONE);
            }
            OrderListRecyclerViewAdapter s = new OrderListRecyclerViewAdapter(getActivity(),customer,R.layout.order_list_design);
            recyclerView.setAdapter(s);
            recyclerView.getAdapter().notifyDataSetChanged();

        }));

        buttonContinueShopping.setOnClickListener(v -> {
            if (getActivity() instanceof HomeActivity){
                ((HomeActivity) getActivity()).onContinueShoppingClick();
            }
        });
    }



    public void getOrderList(String tokenId, VolleyResponse volleyResponse){
        progress.setVisibility(View.VISIBLE);
        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .customer(tokenId, customer -> customer
                        .orders(arg -> arg.first(10), connection -> connection
                                .edges(edge -> edge
                                        .node(node -> node
                                                .orderNumber()
                                                .totalPrice()
                                                .financialStatus()
                                                .processedAt()

                                        )
                                )
                        )
                )
        );
        SampleApplication.graphClient().queryGraph(query).enqueue(new Handler(Looper.getMainLooper()), result -> {
            progress.setVisibility(View.GONE);
            if (result instanceof GraphCallResult.Success){
                if (((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse() != null){
                    List<Storefront.OrderEdge> customer= ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getCustomer().getOrders().getEdges();
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