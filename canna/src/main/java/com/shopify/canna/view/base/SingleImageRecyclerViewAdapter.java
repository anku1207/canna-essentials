package com.shopify.canna.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.shopify.canna.R;
import com.shopify.canna.view.home.SubCategory;
import com.shopify.canna.view.products.ProductListActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SingleImageRecyclerViewAdapter extends  RecyclerView.Adapter<SingleImageRecyclerViewAdapter.ProdectViewHolder>{
        Context mctx ;
        JSONArray productslist;
        int Activityname;

        public SingleImageRecyclerViewAdapter(Context mctx, JSONArray productslist, int Activityname) {
            this.mctx = mctx;
            this.productslist = productslist;
            this.Activityname=Activityname;
        }

        @Override
        public ProdectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(mctx);
            View  view = layoutInflater.inflate(Activityname, parent, false);
            return new ProdectViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProdectViewHolder holder, int position) {
            try {
                JSONObject jsonObject = productslist.getJSONObject(position);

                if(jsonObject.has("image")){
                    Picasso.with(mctx)
                    .load(jsonObject.getString("image"))
                    .into(holder.imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            //do smth when picture is loaded successfully
                        }
                        @Override
                        public void onError() {

                        }
                    });
                }

                holder.mailmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if(jsonObject.has("sub_catg_data") && jsonObject.getJSONArray("sub_catg_data").length()>0){
                                ((Activity)mctx).startActivity(new Intent(mctx, SubCategory.class).putExtra("data",jsonObject.toString()).putExtra("title",jsonObject.getString("cb_name")));
                            }else if(jsonObject.has("cb_id") && jsonObject.has("image") && jsonObject.has("cb_name")){
                                Intent intent = new Intent(mctx, ProductListActivity.class);
                                intent.putExtra(ProductListActivity.EXTRAS_COLLECTION_ID,jsonObject.getString("encode_id"));
                                intent.putExtra(ProductListActivity.EXTRAS_COLLECTION_IMAGE_URL,jsonObject.getString("image"));
                                intent.putExtra(ProductListActivity.EXTRAS_COLLECTION_TITLE,jsonObject.getString("cb_name"));
                                ((Activity)mctx).startActivity(intent);

                            }else if(jsonObject.has("sub_ctg_id") && jsonObject.has("image") && jsonObject.has("sub_catg_name")){
                                Intent intent = new Intent(mctx, ProductListActivity.class);
                                intent.putExtra(ProductListActivity.EXTRAS_COLLECTION_ID,jsonObject.getString("sub_enc_id"));
                                intent.putExtra(ProductListActivity.EXTRAS_COLLECTION_IMAGE_URL,jsonObject.getString("image"));
                                intent.putExtra(ProductListActivity.EXTRAS_COLLECTION_TITLE,jsonObject.getString("sub_catg_name"));
                                ((Activity)mctx).startActivity(intent);

                            }
                        }catch (Exception e ){
                            Toast.makeText(mctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }catch (Exception e){
                Toast.makeText(mctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return productslist.length();
        }
        public  class ProdectViewHolder extends RecyclerView.ViewHolder {
            LinearLayout mailmenu;
            ImageView imageView;
            public ProdectViewHolder(View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.imageView);
                mailmenu=itemView.findViewById(R.id.mailmenu);
            }
        }
}
