package com.example.serviceordering;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {
    private static final String LOG_TAG = OrderListActivity.class.getName();

    private RecyclerView mRecyclerView;
    private ArrayList<ServiceOrder> mItemsData;
    private OrderAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mOrders;
    private FirebaseUser user;
    private String email;

    private boolean viewRow = true;
    private int number = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, number));
        mItemsData = new ArrayList<>();
        mAdapter = new OrderAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mOrders = mFirestore.collection("Orders");
        email = user.getEmail();
        queryData();
    }



        private void queryData() {
        mItemsData.clear();
        mOrders.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ServiceOrder item = document.toObject(ServiceOrder.class);
                mItemsData.add(item);
            }
            mAdapter.getFilter().filter(email);
            mAdapter.notifyDataSetChanged();
        });
        }



    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
