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

import androidx.recyclerview.widget.RecyclerView;

import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.view.ScreenActionEvent;
import com.shopify.canna.view.home.SubCategory;
import com.shopify.canna.view.product.ProductDetailsActivity;
import com.shopify.canna.view.products.ProductListActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SearchItemRecyclerViewAdapter extends  RecyclerView.Adapter<SearchItemRecyclerViewAdapter.ProdectViewHolder>{
        Context mctx ;
        List<Storefront.Product>  productslist;
        int Activityname;

        public SearchItemRecyclerViewAdapter(Context mctx, List<Storefront.Product> productslist, int Activityname) {
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
                Storefront.Product collectionEdge = productslist.get(position);

                if(collectionEdge.getImages() !=null &&  !collectionEdge.getImages().getEdges().isEmpty()){
                    Picasso.with(mctx)
                    .load(collectionEdge.getImages().getEdges().get(0).getNode().getSrc())
                    .into(holder.image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            //do smth when picture is loaded successfully
                        }
                        @Override
                        public void onError() {
                        }
                    });
                }
                holder.desc.setText(collectionEdge.getTitle());
                if (collectionEdge.getPriceRange().getMinVariantPrice() != null &&collectionEdge.getPriceRange().getMinVariantPrice() != null)
                holder.price.setText(collectionEdge.getPriceRange().getMinVariantPrice().getAmount());

                holder.itemView.setOnClickListener(v -> {
                    if (collectionEdge.getId() != null &&
                            collectionEdge.getPriceRange().getMinVariantPrice() != null &&collectionEdge.getPriceRange().getMinVariantPrice() != null &&
                            collectionEdge.getImages() !=null &&  !collectionEdge.getImages().getEdges().isEmpty()){
                        Intent intent = new Intent(mctx, ProductDetailsActivity.class);
                        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_ID, collectionEdge.getId().toString());
                        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_IMAGE_URL, collectionEdge.getImages().getEdges().get(0).getNode().getSrc());
                        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_TITLE, collectionEdge.getTitle());
                        intent.putExtra(ProductDetailsActivity.EXTRAS_PRODUCT_PRICE, Double.valueOf(collectionEdge.getPriceRange().getMinVariantPrice().getAmount()));
                        mctx.startActivity(intent);
                    }
                });
            }catch (Exception e){
                Toast.makeText(mctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return productslist.size();
        }
        public  class ProdectViewHolder extends RecyclerView.ViewHolder {
            LinearLayout mainLayout;
            ImageView image;
            TextView desc,price;

            public ProdectViewHolder(View itemView) {
                super(itemView);
                image=itemView.findViewById(R.id.image);
                mainLayout=itemView.findViewById(R.id.mainLayout);
                desc=itemView.findViewById(R.id.desc);
                price=itemView.findViewById(R.id.price);
            }
        }
}
