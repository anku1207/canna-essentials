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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderListRecyclerViewAdapter extends  RecyclerView.Adapter<OrderListRecyclerViewAdapter.ProdectViewHolder>{
        Context mctx ;
        List<Storefront.OrderEdge> orderEdges;
        int Activityname;

        public OrderListRecyclerViewAdapter(Context mctx,List<Storefront.OrderEdge> orderEdges, int Activityname) {
            this.mctx = mctx;
            this.orderEdges = orderEdges;
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
                Storefront.OrderEdge orderEdge = orderEdges.get(position);

                holder.id.setText("#"+orderEdge.getNode().getOrderNumber());
                holder.status.setText(orderEdge.getNode().getFinancialStatus().toString());


                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(orderEdge.getNode().getProcessedAt().getMillis());
                SimpleDateFormat df1 = new SimpleDateFormat("MMM dd,yyyy");

                holder.desc.setText(df1.format(c.getTime()));
                holder.price.setText(mctx.getString(R.string.Rs)+orderEdge.getNode().getTotalPrice()+"");
            }catch (Exception e){
                Toast.makeText(mctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return orderEdges.size();
        }
        public  class ProdectViewHolder extends RecyclerView.ViewHolder {
            LinearLayout mainLayout;
            TextView desc,price,status,id;

            public ProdectViewHolder(View itemView) {
                super(itemView);
                mainLayout=itemView.findViewById(R.id.mainLayout);
                desc=itemView.findViewById(R.id.desc);
                price=itemView.findViewById(R.id.price);
                status=itemView.findViewById(R.id.status);
                id=itemView.findViewById(R.id.id);
            }
        }
}
