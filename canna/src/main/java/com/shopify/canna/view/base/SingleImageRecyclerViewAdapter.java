package com.shopify.canna.view.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.shopify.canna.R;
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
