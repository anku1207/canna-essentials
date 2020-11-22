package com.shopify.canna.view.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.shopify.buy3.GraphCallResult;
import com.shopify.buy3.Storefront;
import com.shopify.canna.R;
import com.shopify.canna.SampleApplication;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utils;
import com.shopify.canna.view.home.AddressFragment;
import com.shopify.canna.view.home.ShippingAddressFragment;
import com.shopify.graphql.support.ID;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import kotlin.Unit;

public class ShippingAddressRecyclerViewAdapter extends  RecyclerView.Adapter<ShippingAddressRecyclerViewAdapter.ProdectViewHolder>{
        Context mctx ;
        List<Storefront.MailingAddressEdge> customerAddress ;
        int Activityname;
        Fragment fragment;

        public ShippingAddressRecyclerViewAdapter(Context mctx,  List<Storefront.MailingAddressEdge> customerAddress, int Activityname , Fragment fragment) {
            this.mctx = mctx;
            this.customerAddress = customerAddress;
            this.Activityname=Activityname;
            this.fragment=fragment;
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

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddressFragment addressFragment = new AddressFragment();
                        Bundle args = new Bundle();
                        args.putSerializable(AddressFragment.EXTRAS_ADDRESS_DETAILS, collectionEdge);
                        args.putString(AddressFragment.EXTRAS_TITLE, "Update Address");
                        addressFragment.setArguments(args);
                        ((FragmentActivity)mctx).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, addressFragment)
                                .commit();
                    }
                });

                holder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ShippingAddressFragment)fragment).myDialogDoubleButton(mctx,"Alert","Are you sure remove this Address ?",Prefs.INSTANCE.getAccessToken(),collectionEdge.getId());

                    }
                });
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

            TextView name,address,edit,remove;

            public ProdectViewHolder(View itemView) {
                super(itemView);
                mainLayout=itemView.findViewById(R.id.mainLayout);
                name=itemView.findViewById(R.id.name);
                address=itemView.findViewById(R.id.address);
                edit=itemView.findViewById(R.id.edit);
                remove=itemView.findViewById(R.id.remove);
            }
        }


}
