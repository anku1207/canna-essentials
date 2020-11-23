package com.shopify.canna.view.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shopify.canna.BuildConfig;
import com.shopify.canna.R;
import com.shopify.canna.util.Prefs;
import com.shopify.canna.util.Utility;
import com.shopify.canna.view.home.HelpCenterFragment;
import com.shopify.canna.view.home.HomeActivity;
import com.shopify.canna.view.home.OrderFragment;
import com.shopify.canna.view.home.WebView;
import com.shopify.canna.view.home.ShippingAddressFragment;
import com.shopify.canna.view.login.User_Login;
import com.shopify.graphql.support.ID;

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
                                        .setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_left, R.anim.exit_right)
                                        .replace(R.id.fragment_container, new HelpCenterFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Saved Address")){
                                ((FragmentActivity)mctx).getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_left, R.anim.exit_right)
                                        .replace(R.id.fragment_container, new ShippingAddressFragment())
                                        .addToBackStack(null)
                                        .commit();
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("My Order")){
                                ((FragmentActivity)mctx).getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.enter_left, R.anim.exit_right, R.anim.enter_left, R.anim.exit_right)
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
                            }else if(jsonObject.getString("heading").equalsIgnoreCase("Share App")){
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, mctx.getResources().getString(R.string.app_name));
                                String shareMessage= "\nTry out this cool app\n\n";
                                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                ((FragmentActivity)mctx).startActivity(Intent.createChooser(shareIntent, "Share app"));
                            }else if (jsonObject.getString("heading").equalsIgnoreCase("Share Feedback")){
                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@cannaessentials.in"));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Canna Essentials android app feedback");
                                ((FragmentActivity)mctx).startActivity(Intent.createChooser(emailIntent, ""));
                            }else if (jsonObject.getString("heading").equalsIgnoreCase("Logout")){
                                myDialogDoubleButton(mctx,"Logout","myDialogDoubleButton");
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


    public void  myDialogDoubleButton(Context context, String title, String msg){
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Prefs.INSTANCE.clear();
                        User_Login.launchActivity(mctx);
                        ((Activity)mctx).finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        if(!((Activity)context).isFinishing() && !alert.isShowing())  alert.show();


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
