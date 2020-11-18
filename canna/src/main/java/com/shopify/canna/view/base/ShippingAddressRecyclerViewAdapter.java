package com.shopify.canna.view.base;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShippingAddressRecyclerViewAdapter extends  RecyclerView.Adapter<ShippingAddressRecyclerViewAdapter.ProdectViewHolder>{
        Context mctx ;
        List<Storefront.MailingAddressEdge> customerAddress ;
        int Activityname;

        public ShippingAddressRecyclerViewAdapter(Context mctx,  List<Storefront.MailingAddressEdge> customerAddress, int Activityname) {
            this.mctx = mctx;
            this.customerAddress = customerAddress;
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
                Storefront.MailingAddress collectionEdge = customerAddress.get(position).getNode();

                holder.name.setText(collectionEdge.getFirstName()+" "+collectionEdge.getLastName());
                holder.address.setText(collectionEdge.getAddress1()+"\n"+collectionEdge.getAddress2()+"\n"+collectionEdge.getCity()+"\n"+collectionEdge.getCountry()+"\n"
                        +collectionEdge.getProvince()+ ","+collectionEdge.getZip()+"\n"+collectionEdge.getPhone() );
            }catch (Exception e){
                Toast.makeText(mctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return customerAddress.size();
        }
        public  class ProdectViewHolder extends RecyclerView.ViewHolder {
            LinearLayout mainLayout;

            TextView name,address;

            public ProdectViewHolder(View itemView) {
                super(itemView);
                mainLayout=itemView.findViewById(R.id.mainLayout);
                name=itemView.findViewById(R.id.name);
                address=itemView.findViewById(R.id.address);
            }
        }
}
