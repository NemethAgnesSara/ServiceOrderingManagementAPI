package com.example.serviceordering;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>  implements Filterable {

    private ArrayList<ServiceOrder> mData;
    private ArrayList<ServiceOrder> mDataF;

    private Context mContext;
    private int lastPosition = -1;
    private FirebaseFirestore mFirestore;

    OrderAdapter(Context context, ArrayList<ServiceOrder> itemsData) {
        this.mData = itemsData;
        this.mDataF = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        //most kiválasztott elem
        ServiceOrder currentItem = mDataF.get(position);
        // adat és felület összekötése
        holder.bindTo(currentItem);
        if(holder.getAdapterPosition() > lastPosition) {
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mDataF.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<ServiceOrder> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mData.size();
                results.values = mData;
            } else {
                if(mData.isEmpty()){
                }
                for(ServiceOrder item : mData) {
                    if(item.getNotificationContact().equals(charSequence)){
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mDataF = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNotificationContact;
        private TextView mDescription;
        private TextView mRequestedCompletionDate;
        private TextView mServiceName;
        private TextView mOrderDate;
        private TextView mCancellationDate;
        private TextView mCancellationReason;
        private TextView mId;
        private Button deleteB;

        ViewHolder(View itemView) {
            super(itemView);

            mNotificationContact = itemView.findViewById(R.id.notificationCantact);
            mDescription = itemView.findViewById(R.id.descrption);
            mRequestedCompletionDate = itemView.findViewById(R.id.requestedCompletionDate);
            mServiceName = itemView.findViewById(R.id.serviceName);
            mOrderDate = itemView.findViewById(R.id.orderDate);
            mCancellationDate = itemView.findViewById(R.id.cancellationDate);
            mCancellationReason = itemView.findViewById(R.id.cancellationReason);
            mId=itemView.findViewById(R.id.id);
            deleteB  =itemView.findViewById(R.id.delete);
            itemView.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   editOrder((String)mId.getText());
               }
            });
            deleteB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOrder((String)mId.getText());
                }
            });
        }

        private void deleteOrder(String i) {
            Intent intent = new Intent(mContext, DeleteOrderActivity.class);
            intent.putExtra("orderId",i);
            mContext.startActivity(intent);
            CustomIntent.customType(mContext, "left-to-right");
        }

        void bindTo(ServiceOrder currentItem){
            mNotificationContact.setText("Rendelő elérhetősége: "+currentItem.getNotificationContact());
            mDescription.setText("Leírás: "+currentItem.getDescription());
            mRequestedCompletionDate.setText("Kívánt befejezés: "+currentItem.getRequestedCompletionDate().toString());
            mOrderDate.setText("Rendelés időpontja: "+currentItem.getOrderDate().toString());
            mServiceName.setText("Kért szolgáltats: "+currentItem.getServiceOrderItem().get(0).getService().getName());
            mId.setText(currentItem.getId());

            //ha nem törölt az elem a cancell adattagok null értékűek, nem ezt iratom ki
            if(currentItem.getCancellationDate()==null){
                mCancellationDate.setText("Lemondás: A rendelés aktív");
                mCancellationReason.setText("");
                deleteB.setVisibility(View.VISIBLE);
            }else {
                mCancellationDate.setText("Leondás időpontja: "+currentItem.getCancellationDate().toString());
                mCancellationReason.setText("Lemondás oka: "+currentItem.getCancellationReason());
                deleteB.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void editOrder(String i) {
        Intent intent = new Intent(mContext, EditServiceOrderActivity.class);
        intent.putExtra("orderId",i);
        mContext.startActivity(intent);
        CustomIntent.customType(mContext, "left-to-right");

    }
}
