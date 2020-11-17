package com.shopify.canna.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shopify.canna.R;
import com.shopify.canna.util.Utility;
import com.shopify.canna.view.home.HelpCenterFragment;
import com.shopify.canna.view.home.HomeActivity;
import com.shopify.canna.view.home.ShippingAddressFragment;
import com.shopify.canna.view.home.SubCategory;
import com.shopify.canna.view.products.ProductListActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class AccountDesignRecyclerViewAdapter extends  RecyclerView.Adapter<AccountDesignRecyclerViewAdapter.ProdectViewHolder>{
        Context mctx ;
        JSONArray productslist;
        int Activityname;

        public AccountDesignRecyclerViewAdapter(Context mctx, JSONArray productslist, int Activityname) {
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

                holder.imageView.setImageDrawable(Utility.GetImage(mctx,jsonObject.getString("image")));
                holder.heading.setText(jsonObject.getString("heading"));
                holder.desc.setText(jsonObject.getString("desc"));

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if(jsonObject.getString("heading").equalsIgnoreCase("Help Center")){
                                ((FragmentActivity)mctx).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new HelpCenterFragment())
                                        .commit();
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Saved Address")){
                                ((FragmentActivity)mctx).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new ShippingAddressFragment())
                                        .commit();
                            }
                        }catch (Exception e){
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
            LinearLayout mainLayout;
            ImageView imageView,goArrow;
            TextView heading,desc;
            public ProdectViewHolder(View itemView) {
                super(itemView);
                mainLayout=itemView.findViewById(R.id.mainLayout);
                imageView=itemView.findViewById(R.id.imageView);
                goArrow=itemView.findViewById(R.id.goArrow);
                heading=itemView.findViewById(R.id.heading);
                desc=itemView.findViewById(R.id.desc);
            }
        }
}
