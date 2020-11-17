package com.shopify.canna.view.home;

import android.content.Intent;
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
import android.widget.SearchView;

import com.google.gson.Gson;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utils;
import com.shopify.canna.util.VolleyResponse;
import com.shopify.canna.view.base.SearchItemRecyclerViewAdapter;
import com.shopify.canna.view.base.SingleImageRecyclerViewAdapter;
import com.shopify.canna.view.login.User_Login;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SearchView searchView;
    RecyclerView recyclerView;
    SearchItemRecyclerViewAdapter searchItemRecyclerViewAdapter;


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView=view.findViewById(R.id.search_view);
        recyclerView=view.findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // collapse the view ?
                //menu.findItem(R.id.menu_search).collapseActionView();
                Log.e("queryText", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // search goes here !!

                if(newText==null || newText.isEmpty()){
                    List<Storefront.CollectionEdge> collection = new ArrayList<>();
                    searchItemRecyclerViewAdapter=new SearchItemRecyclerViewAdapter(getContext(), collection,R.layout.product_search_design);
                    recyclerView.setAdapter(searchItemRecyclerViewAdapter);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }else {
                    searchItemBykey(newText , new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
                        List<Storefront.CollectionEdge> collection = (List<Storefront.CollectionEdge>) success;
                        Log.w("length",collection.size()+"");
                        searchItemRecyclerViewAdapter=new SearchItemRecyclerViewAdapter(getContext(), collection,R.layout.product_search_design);
                        recyclerView.setAdapter(searchItemRecyclerViewAdapter);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }));
                }

                return false;
            }
        });

    }

    public void searchItemBykey(String item , VolleyResponse volleyResponse){
        Storefront.QueryRootQuery query =  Storefront.query(root -> root
                .shop(shop -> shop
                        .collections(
                                arg -> arg
                                        .first(10)
                                        .query(item),
                                connection -> connection

                                        .edges(edges -> edges
                                                .node(node -> node
                                                        .title()
                                                        .description()
                                                        .image(_queryBuilder -> _queryBuilder
                                                                .src())
                                                )
                                        )
                        )
                )
        );

        SampleApplication.graphClient().queryGraph(query).enqueue(new Handler(Looper.getMainLooper()), result -> {
            if (result instanceof GraphCallResult.Success){

                if (((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse() != null){
                    List<Storefront.CollectionEdge> collection= ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getShop().getCollections().getEdges();
                    volleyResponse.onSuccess(collection);
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