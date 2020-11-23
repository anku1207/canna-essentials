package com.shopify.canna.view.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utils;
import com.shopify.canna.util.VolleyResponse;
import com.shopify.canna.view.base.EndlessRecyclerViewScrollListener;
import com.shopify.canna.view.base.SearchItemRecyclerViewAdapter;
import com.shopify.canna.view.base.SingleImageRecyclerViewAdapter;
import com.shopify.canna.view.login.User_Login;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.reflect.jvm.internal.impl.renderer.ClassifierNamePolicy;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SearchItemRecyclerViewAdapter.OnItemClick{

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
    ProgressBar progressBar;
    AppCompatTextView textViewNoData;
    String pageCursor = "";
    LinearLayoutManager verticalLayoutManager;
    String searchString = "";

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
        progressBar=view.findViewById(R.id.progress);
        textViewNoData = view.findViewById(R.id.text_no_data);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        verticalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);

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

                if(newText==null || newText.isEmpty() || newText.length() < 3){
                    List<Storefront.Product> collection = new ArrayList<>();
                    searchItemRecyclerViewAdapter=new SearchItemRecyclerViewAdapter(getContext(), collection,R.layout.product_search_design, SearchFragment.this);
                    recyclerView.setAdapter(searchItemRecyclerViewAdapter);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }else {
                    searchString = newText;
                    pageCursor = "";
                    searchItemBykey(searchString , new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
                        List<Storefront.Product> collection = (List<Storefront.Product>) success;
                        Log.w("length",collection.size()+"");
                        if (collection.isEmpty()){
                            textViewNoData.setVisibility(View.VISIBLE);
                        }else {
                            textViewNoData.setVisibility(View.GONE);
                        }
                        searchItemRecyclerViewAdapter=new SearchItemRecyclerViewAdapter(getContext(), collection,R.layout.product_search_design, SearchFragment.this);
                        recyclerView.setAdapter(searchItemRecyclerViewAdapter);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }));
                }

                return false;
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(verticalLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("PAGINNATION",""+pageCursor+"\n"+totalItemsCount);
                if (!searchString.isEmpty() && !pageCursor.isEmpty()){
                    searchItemBykey(searchString , new VolleyResponse((VolleyResponse.OnSuccess)(success)->{
                        List<Storefront.Product> collection = (List<Storefront.Product>) success;
                        Log.w("length",collection.size()+"");
                        if (collection.isEmpty()){
                            textViewNoData.setVisibility(View.VISIBLE);
                        }else {
                            Log.d("PAGINATION",""+collection.size());
                            textViewNoData.setVisibility(View.GONE);
                            searchItemRecyclerViewAdapter.updateList(collection);
                        }
                    }), pageCursor);
                }
            }
        });
    }

    public void searchItemBykey(String item , VolleyResponse volleyResponse){
        progressBar.setVisibility(View.VISIBLE);

        Storefront.QueryRootQuery query = Storefront.query(root -> root.products(args2 -> args2.query(item).first(10),
                args -> args.edges(productQuery -> productQuery.cursor().node(_queryBuilder -> _queryBuilder.title().availableForSale().priceRange(range -> range
                .minVariantPrice(Storefront.MoneyV2Query::amount)).images(args1 ->  args1.first(1), imageConnection -> imageConnection
                        .pageInfo(Storefront.PageInfoQuery::hasNextPage)
                        .edges(imageEdge -> imageEdge
                                .node(Storefront.ImageQuery::src)))))
        ));

        SampleApplication.graphClient().queryGraph(query).enqueue(new Handler(Looper.getMainLooper()), result -> {
            if (result instanceof GraphCallResult.Success){
                List<Storefront.Product> products = new ArrayList<>();
                if (((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse() != null &&
                        ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData() != null &&
                        ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getProducts() != null){
                    for (Storefront.ProductEdge collectionEdge : ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getProducts().getEdges()) {
                        if (collectionEdge.getCursor() != null){
                            Log.d("CURSOR_PAGE",""+collectionEdge.getCursor());
                            pageCursor = collectionEdge.getCursor();
                        }
                        products.add(collectionEdge.getNode());
                    }

                    volleyResponse.onSuccess(products);
                }else {
                    Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
                }
                Utils.INSTANCE.showHideView(progressBar, View.GONE);
            }else {
                Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
                Utils.INSTANCE.showHideView(progressBar, View.GONE);
            }
            return Unit.INSTANCE;
        });
    }

    public void searchItemBykey(String item , VolleyResponse volleyResponse, String pageCursor){
        progressBar.setVisibility(View.VISIBLE);
        Storefront.QueryRootQuery query = Storefront.query(root -> root.products(args2 -> args2.query(item).first(20).after(pageCursor),
                args -> args.edges(productQuery -> productQuery.cursor().node(_queryBuilder -> _queryBuilder.title().availableForSale().priceRange(range -> range
                        .minVariantPrice(Storefront.MoneyV2Query::amount)).images(args1 ->  args1.first(1), imageConnection -> imageConnection
                        .edges(imageEdge -> imageEdge
                                .node(Storefront.ImageQuery::src)))))
        ));

        SampleApplication.graphClient().queryGraph(query).enqueue(new Handler(Looper.getMainLooper()), result -> {
            if (result instanceof GraphCallResult.Success){
                List<Storefront.Product> products = new ArrayList<>();

                if (((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse() != null &&
                        ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData() != null &&
                        ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getProducts() != null){
                    for (Storefront.ProductEdge collectionEdge : ((GraphCallResult.Success<Storefront.QueryRoot>) result).getResponse().getData().getProducts().getEdges()) {
                        products.add(collectionEdge.getNode());
                    }

                    volleyResponse.onSuccess(products);
                }else {
                    Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
                }
                Utils.INSTANCE.showHideView(progressBar, View.GONE);
            }else {
                Utils.INSTANCE.showToast(getContext(), getString(R.string.something_wrong));
                Utils.INSTANCE.showHideView(progressBar, View.GONE);
            }
            return Unit.INSTANCE;
        });
    }

    @Override
    public void onSearchItemClick() {
        searchView.clearFocus();
        searchView.invalidate();
    }
}