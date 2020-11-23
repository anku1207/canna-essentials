package com.shopify.canna.view.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shopify.canna.Interface.CallBackInterface;
import com.shopify.canna.Interface.VolleyResponse;
import com.shopify.canna.R;
import com.shopify.canna.view.base.SingleImageRecyclerViewAdapter;
import com.shopify.canna.vo.ConnectionVO;
import com.shopify.canna.volley.VolleyResponseListener;
import com.shopify.canna.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrandsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrandsFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SingleImageRecyclerViewAdapter singleImageRecyclerViewAdapter;

    public BrandsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrandsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrandsFragment newInstance(String param1, String param2) {
        BrandsFragment fragment = new BrandsFragment();
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
        return inflater.inflate(R.layout.fragment_brands, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recyclerView);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        ((HomeActivity) getContext()).changeTitleByActivity(getContext(),true,null,false);


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                // loadRecyclerViewData();
                loadRecyclerViewData();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

    }
    @Override
    public void onRefresh() {
        // Fetching data from server
        loadRecyclerViewData();
    }


    private void loadRecyclerViewData(){
        mSwipeRefreshLayout.setRefreshing(true);
        getdata(new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
            try {
                JSONObject jsonObject = (JSONObject) success;
                singleImageRecyclerViewAdapter=new SingleImageRecyclerViewAdapter(getContext(), jsonObject.getJSONArray("data"),R.layout.brandsimagerecyclerviewdesign);
                recyclerView.setAdapter(singleImageRecyclerViewAdapter);
                recyclerView.getAdapter().notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }catch (Exception e ){
                Toast.makeText(getContext(), "sdfsdfsf", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void getdata(VolleyResponse volleyResponse){
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            ConnectionVO connectionVO = new ConnectionVO();
            connectionVO.setMethodName("category-brand-api.php?apicall=brands");
            connectionVO.setRequestType(ConnectionVO.REQUEST_GET);
            connectionVO.setLoaderAvoided(true);
            VolleyUtils.makeJsonObjectRequest(getContext(),connectionVO, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                }
                @Override
                public void onResponse(Object response) throws JSONException {
                    JSONObject responseObject = (JSONObject) response;
                    volleyResponse.onSuccess(responseObject);
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "sdfsdfsf", Toast.LENGTH_SHORT).show();
        }

    }
}