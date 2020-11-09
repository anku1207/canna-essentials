package com.shopify.canna.view.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.shopify.canna.Interface.VolleyResponse;
import com.shopify.canna.R;
import com.shopify.canna.constant.Content_Message;
import com.shopify.canna.view.base.SingleImageRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class SubCategory extends AppCompatActivity implements View.OnClickListener , SwipeRefreshLayout.OnRefreshListener{
    ImageView back_activity_button;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SingleImageRecyclerViewAdapter singleImageRecyclerViewAdapter;
    JSONObject jsonObjectData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        if(getSupportActionBar()!=null) getSupportActionBar().hide();
        back_activity_button=findViewById(R.id.back_activity_button);
        recyclerView=findViewById(R.id.recyclerView);

        back_activity_button.setOnClickListener(this);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
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
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        try {
             jsonObjectData = new JSONObject( getIntent().getStringExtra("data"));
        }catch (Exception e){
            Toast.makeText(this, Content_Message.error_message, Toast.LENGTH_SHORT).show();
        }
    }


    private void loadRecyclerViewData(){
        mSwipeRefreshLayout.setRefreshing(true);
        try {
            JSONArray jsonArray = jsonObjectData.getJSONArray("sub_catg_data");
            singleImageRecyclerViewAdapter=new SingleImageRecyclerViewAdapter(SubCategory.this, jsonArray,R.layout.brandsimagerecyclerviewdesign);
            recyclerView.setAdapter(singleImageRecyclerViewAdapter);
            recyclerView.getAdapter().notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }catch (Exception e ){
            Toast.makeText(this, Content_Message.error_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_activity_button:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }
}