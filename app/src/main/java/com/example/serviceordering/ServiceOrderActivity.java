package com.example.serviceordering;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class ServiceOrderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView dateText;
    private TextView titleText;
    private TextView orderDateText;
    private EditText descriptionET;
    private EditText notifyET;
    private Button saveBT;
    private Button cancelBT;

    private FirebaseFirestore mFirestore;
    private CollectionReference mOrders;
    private String email;
    private FirebaseUser user;
    private String serviceName;
    private String serviceDescription;

    private int myear;
    private int mmonth;
    private int mday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_order);

        dateText = findViewById(R.id.dateText);
        titleText = findViewById(R.id.tite);
        orderDateText = findViewById(R.id.orderDate);
        descriptionET = findViewById(R.id.descrption);
        notifyET = findViewById(R.id.notifi);
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
        serviceName = intent.getStringExtra("NAME");
        serviceDescription = intent.getStringExtra("DESCRIPTION");

        String s =serviceName +" Rendelése";
        dateText.setText("Kívánt befejezés dátuma");
        titleText.setText(s);
        orderDateText.setText("Rendelés időpontja: "+ new Date().toString() );
        notifyET.setText(email);
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
        dateText.setText(date);
    }

    public void saveOrder(View view){
        List<ServiceOrderItem> list = new ArrayList<>();
        list.add(new ServiceOrderItem(1, new ServiceRefOrValue(serviceName,serviceDescription)));

        ServiceOrderItem[] array = new ServiceOrderItem[1];
        array[0]= new ServiceOrderItem(1, new ServiceRefOrValue(serviceName,serviceDescription));

        String id=String.valueOf(new Random().nextInt(1000));
        mOrders.document(id).set(new ServiceOrder( id,
                descriptionET.getText().toString(),
                 new Date((myear-1900),mmonth,mday), new Date(),
                notifyET.getText().toString(),
                null,
                null,
                list));
        cancel(view);

    }
    public void cancel(View view){
        Intent intent = new Intent(this, ServiceListActivity.class);
        startActivity(intent);
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
