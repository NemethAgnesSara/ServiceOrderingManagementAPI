package com.example.serviceordering;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import maes.tech.intentanim.CustomIntent;


public class DeleteOrderActivity extends AppCompatActivity{
    private static final String LOG_TAG = DeleteOrderActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static SharedPreferences preferences;
    private Bundle savedInstanceState;

    private TextView titleText;
    private TextView reasonText;
    private TextView serviceNameText;
    private TextView cancellationDateText;
    private EditText reasonET;
    private Button saveBT;
    private Button cancelBT;

    private static Context mContext;
    private static FirebaseFirestore mFirestore;
    private static CollectionReference mOrders;
    private  FirebaseUser user;

    private String email;
    private String orderId;
    private ServiceOrder serviceOrder;

    private AlarmManager mAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_order);
        this.savedInstanceState =savedInstanceState;

        titleText = findViewById(R.id.tite);
        serviceNameText=findViewById(R.id.serviceName);
        cancellationDateText=findViewById(R.id.cancellationDate);
        reasonET=findViewById(R.id.reason);
        reasonText=findViewById(R.id.reason);
        saveBT=findViewById(R.id.saveButton);
        cancelBT=findViewById(R.id.cancel);

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mFirestore = FirebaseFirestore.getInstance();
        mOrders = mFirestore.collection("Orders");
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");

        //felhasználói felület beállítása, lekérdezés a szolgáltatás nevéért
        String s =" Rendelés törlése";
        titleText.setText(s);
        cancellationDateText.setText("Lemondás időpontja: "+ new Date().toString());
        mContext=this;
        mOrders.whereEqualTo("id", orderId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                             serviceOrder = documentSnapshot.toObject(ServiceOrder.class);
                            serviceNameText.setText("Szolgáltatás: "+ serviceOrder.getServiceOrderItem().get(0).getService().getName());
                            System.out.println(documentSnapshot.getData());
                        }
                    }

                });


    }

    public static void deleteOrder(){
        //alarmmanager hivja meg, nem fut le onCreate
        mFirestore = FirebaseFirestore.getInstance();
        mOrders = mFirestore.collection("Orders");
        String oid = preferences.getString("id", "");
        System.out.println(oid);

        DocumentReference ref = mOrders.document(oid);
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + oid);
                })
                .addOnFailureListener(fail -> {
                    Log.d(LOG_TAG, "Item is NOT deleted: " + oid);
                });

        cancel();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateOrder(View view){
        mFirestore.collection("Orders")
                .document(serviceOrder.getId()).
                update("cancellationDate" , new Date() , "cancellationReason" , reasonET.getText().toString())
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
        setAlarmManager();
        cancel(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setAlarmManager() {
        //a várakozás kicst, hogy ne kelljen sokáig futtatni az emulátort
        long repeatInterval = 30000;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("id",orderId);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                pendingIntent);

    }

    public void cancel(View view){
        Intent intent = new Intent(this, OrderListActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "right-to-left");

    }
    public static void cancel(){
        Intent intent = new Intent(mContext, OrderListActivity.class);
        mContext.startActivity(intent);
    }

    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", orderId);
        editor.apply();
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
