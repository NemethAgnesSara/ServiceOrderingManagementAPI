package com.example.serviceordering;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import maes.tech.intentanim.CustomIntent;


public class EditServiceOrderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String LOG_TAG = EditServiceOrderActivity.class.getName();

    private TextView dateText;
    private TextView titleText;
    private TextView orderDateText;
    private TextView serviceNameText;
    private EditText descriptionET;
    private EditText notifyET;
    private Button saveBT;
    private Button cancelBT;


    private FirebaseFirestore mFirestore;
    private CollectionReference mOrders;
    private FirebaseUser user;
    private int myear;
    private int mmonth;
    private int mday;
    private String email;
    private String orderId;
    private ServiceOrder serviceOrder;
    private int tmp=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_service_order);

        dateText = findViewById(R.id.dateText);
        titleText = findViewById(R.id.tite);
        orderDateText = findViewById(R.id.orderDate);
        descriptionET = findViewById(R.id.descrption);
        notifyET = findViewById(R.id.notifi);
        serviceNameText=findViewById(R.id.serviceName);
        saveBT=findViewById(R.id.saveButton);
        cancelBT=findViewById(R.id.cancel);


        findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        mFirestore = FirebaseFirestore.getInstance();
        mOrders = mFirestore.collection("Orders");
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        String s =" Rendelés módosítása";

        //felhasználói felület
        dateText.setText("Kívánt befejezés dátuma");
        titleText.setText(s);
        orderDateText.setText("Rendelés időpontja: "+ new Date().toString() );

        mOrders.whereEqualTo("id", orderId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                             serviceOrder = documentSnapshot.toObject(ServiceOrder.class);
                            dateText.setText(serviceOrder.getRequestedCompletionDate().toString());
                            orderDateText.setText(serviceOrder.getOrderDate().toString());
                            descriptionET.setText(serviceOrder.getDescription());
                            notifyET.setText(serviceOrder.getNotificationContact());
                            serviceNameText.setText("Szolgáltatás: "+ serviceOrder.getServiceOrderItem().get(0).getService().getName());
                            System.out.println(documentSnapshot.getData());
                        }
                    }

                });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "/" + month + "/" + dayOfMonth ;
        myear=year;
        mmonth=month;
        mday=dayOfMonth;
        tmp=1;
        dateText.setText(date);
    }

    public void saveOrder(View view){
        //attól függően, hogy megváltoztattuk-e a dátumot, máshogy kell módosítani
        Date d;
        if(tmp==1){
            d=new Date((myear-1900),mmonth,mday);
        }else{
            d=serviceOrder.getRequestedCompletionDate();
        }
        mFirestore.collection("Orders")
                .document(serviceOrder.getId()).
                update("notificationContact" , notifyET.getText().toString() , "description" , descriptionET.getText().toString(),"requestedCompletionDate", d)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i(LOG_TAG, "sikeres módosítás");
                        }else{
                            Log.i(LOG_TAG, task.getException().getMessage());

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(LOG_TAG, e.getMessage());
            }
        });

        cancel(view);

    }
    public void cancel(View view){
        Intent intent = new Intent(this, OrderListActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "right-to-left");

    }

    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        saveBT.setBackgroundColor(getResources().getColor(R.color.orangre));
        cancelBT.setBackgroundColor(getResources().getColor(R.color.orangre));
        YoYo.with(Techniques.FlipInX)
                .duration(700)
                .repeat(1)
                .playOn(titleText);
    }
}
