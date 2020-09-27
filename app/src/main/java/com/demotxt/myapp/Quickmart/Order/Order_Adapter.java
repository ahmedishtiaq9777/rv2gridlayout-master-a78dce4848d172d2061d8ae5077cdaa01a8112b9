package com.demotxt.myapp.Quickmart.Order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.demotxt.myapp.Quickmart.R;

import java.util.List;
import java.util.Set;

public class Order_Adapter extends RecyclerView.Adapter<Order_Adapter.OrderViewHolder>{

    private final Context mContext;
    private final List<Order> mData;

    //  private List<Integer> Ids;
    public Set<String> ids;

    public Order_Adapter(Context mContext, List<Order> mdata){
        this.mContext=mContext;
        this.mData = mdata;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_order, parent, false);
        return new Order_Adapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {

        holder.Order_Id.setText(mData.get(position).getOrderId());
        holder.Order_Price.setText(mData.get(position).getPrice());
        holder.Order_Status.setText(mData.get(position).getOrderStatus());
        holder.Order_Date.setText(mData.get(position).getOrderDate().substring(0,10));

        //Card click Listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext,Order_Detail_Activity.class);

                //passing data to order detail activity
                i.putExtra("ID",mData.get(position).getOrderId());
                i.putExtra("Price",mData.get(position).getPrice());
                i.putExtra("Status",mData.get(position).getOrderStatus());
                i.putExtra("Date",mData.get(position).getOrderDate());

                mContext.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder   {

        final TextView Order_Id;
        final TextView Order_Price;
        final TextView Order_Status;
        final TextView Order_Date;
        final CardView cardView;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            Order_Id = itemView.findViewById(R.id.txt_orderId);
            Order_Price = itemView.findViewById(R.id.txt_price);
            Order_Status = itemView.findViewById(R.id.txt_status);
            Order_Date = itemView.findViewById(R.id.txt_date);
            cardView = itemView.findViewById(R.id.order_cardview);
        }
    }


}
