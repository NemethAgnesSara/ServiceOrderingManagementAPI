package com.example.serviceordering;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ServiceRefAdapter extends RecyclerView.Adapter<ServiceRefAdapter.ViewHolder>  implements Filterable {

    private ArrayList<ServiceRefOrValue> mData;
    private Context mContext;
    private int lastPosition = -1;

    ServiceRefAdapter(Context context, ArrayList<ServiceRefOrValue> itemsData) {
        this.mData = itemsData;
        this.mContext = context;
    }


    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ServiceRefAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceRefAdapter.ViewHolder holder, int position) {
        ServiceRefOrValue currentItem = mData.get(position);
        holder.bindTo(currentItem);
        if(holder.getAdapterPosition() > lastPosition) {
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameText;
        private TextView mCategoryText;

        ViewHolder(View itemView) {
            super(itemView);
            mNameText = itemView.findViewById(R.id.itemName);
            mCategoryText = itemView.findViewById(R.id.itemCategory);
           itemView.findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  String nameData= (String) mNameText.getText() ;
                  String descriptionData = (String) mCategoryText.getText();
                  order(nameData,descriptionData);
               }
           });
        }

        void bindTo(ServiceRefOrValue currentItem){
            mNameText.setText(currentItem.getName());
            mCategoryText.setText(currentItem.getDescription());
        }
    }
    public void order(String m, String d) {
        Intent intent = new Intent(mContext, ServiceOrderActivity.class);
        intent.putExtra("NAME", m);
        intent.putExtra("DESCRIPTION", d);
        mContext.startActivity(intent);
    }
}
