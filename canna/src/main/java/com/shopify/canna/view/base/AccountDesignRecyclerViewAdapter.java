package com.shopify.canna.view.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shopify.canna.R;
import com.shopify.canna.util.Utility;
import com.shopify.canna.view.home.HelpCenterFragment;
import com.shopify.canna.view.home.OrderFragment;
import com.shopify.canna.view.home.WebView;
import com.shopify.canna.view.home.ShippingAddressFragment;

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
                                        .addToBackStack(null)
                                        .commit();
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Saved Address")){
                                ((FragmentActivity)mctx).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new ShippingAddressFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("My Order")){
                                ((FragmentActivity)mctx).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, new OrderFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Privacy Policy")){
                                Intent intent = new Intent(mctx, WebView.class);
                                intent.putExtra("url","file:///android_asset/privacy_policy.html");
                                intent.putExtra("title","Privacy Policy");

                                ((Activity)mctx).startActivity(intent);
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("About Us")){
                                Intent intent = new Intent(mctx, WebView.class);
                                intent.putExtra("url","file:///android_asset/about.html");
                                intent.putExtra("title","About Us");

                                ((Activity)mctx).startActivity(intent);
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Terms And Condition")){
                                Intent intent = new Intent(mctx, WebView.class);
                                intent.putExtra("url","file:///android_asset/terms_of_service.html");
                                intent.putExtra("title","Terms And Condition");

                                ((Activity)mctx).startActivity(intent);
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Shipping")){
                                Intent intent = new Intent(mctx, WebView.class);
                                intent.putExtra("url","file:///android_asset/shipping_policy.html");
                                intent.putExtra("title","Shipping Policy");

                                ((Activity)mctx).startActivity(intent);
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Refund")){
                                Intent intent = new Intent(mctx, WebView.class);
                                intent.putExtra("url","file:///android_asset/refund_policy.html");
                                intent.putExtra("title","Refund Policy");

                                ((Activity)mctx).startActivity(intent);
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Frequently Asked Questions")){
                                Intent intent = new Intent(mctx, WebView.class);
                                intent.putExtra("url","file:///android_asset/faqs.html");
                                intent.putExtra("title","FAQ");

                                ((Activity)mctx).startActivity(intent);
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Call Us")){
                                ((FragmentActivity)mctx).startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", jsonObject.getString("desc"), null)));
                            }
                        }catch (Exception e){
                            Log.w("error",e.getMessage());
                            Toast.makeText(mctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }catch (Exception e){
                Toast.makeText(mctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private static boolean hasPermissions(Context context, String... permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
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
