package com.example.serviceordering;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ServiceListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ServiceListActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private FirebaseUser user;


    private Button myOrdersBT;
    private RecyclerView mRecyclerView;
    private ArrayList<ServiceRefOrValue> mItemsData;
    private ServiceRefAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mServices;

    private boolean viewRow = true;
    private int number = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_list);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mRecyclerView = findViewById(R.id.recyclerView);
        myOrdersBT = findViewById(R.id.myorders);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, number));
        mItemsData = new ArrayList<>();
        mAdapter = new ServiceRefAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);
        mFirestore = FirebaseFirestore.getInstance();
        mServices = mFirestore.collection("Services");
        queryData();
    }

    private void initializeData() {
        String[] itemsName = getResources()
                .getStringArray(R.array.item_names);
        String[] itemsCategory = getResources()
                .getStringArray(R.array.item_categories);
        for (int i = 0; i < itemsName.length; i++) {
            mServices.add(new ServiceRefOrValue(itemsName[i],itemsCategory[i]));
        }

    }
    private void queryData() {
        mItemsData.clear();
        mServices.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ServiceRefOrValue item = document.toObject(ServiceRefOrValue.class);
                mItemsData.add(item);
            }
            if (mItemsData.size() == 0) {
                initializeData();
                queryData();
            }
            mAdapter.notifyDataSetChanged();
        });
    }
    public void listorders(View view){
        Intent intent = new Intent(this, OrderListActivity.class);
        startActivity(intent);
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "fadein-to-fadeout");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        myOrdersBT.setBackgroundColor(getResources().getColor(R.color.orangre));
    }

}
