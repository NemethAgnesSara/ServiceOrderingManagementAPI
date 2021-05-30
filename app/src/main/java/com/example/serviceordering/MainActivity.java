package com.example.serviceordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.collection.LLRBNode.Color;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    EditText emailET;
    EditText passwordET;
    TextView titleTV;
    Button registerBT;
    Button loginBT;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        //adattag összekapcsolása a bvitelimezővel
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        titleTV=findViewById(R.id.tite);
        registerBT=findViewById(R.id.registerButton);
        loginBT=findViewById(R.id.loginButton);

        //animáció
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(1)
                .playOn(titleTV);

        //ha valaki regisztáció után kerül vissza
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        emailET.setText(userName);
        passwordET.setText(password);
    }

    public void login(View view){
        String userName = emailET.getText().toString();
        String password = passwordET.getText().toString();
        mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "Login done!");
                    loginCompleted();
                } else {
                    Log.d(LOG_TAG, "Login unsuccessful!");
                }
            }
        });
    }

    public void registration(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void loginCompleted() {
        Intent intent = new Intent(this, ServiceListActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "fadein-to-fadeout");
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", emailET.getText().toString());
        editor.putString("password", passwordET.getText().toString());
        editor.apply();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        registerBT.setBackgroundColor(getResources().getColor(R.color.orangre));
        loginBT.setBackgroundColor(getResources().getColor(R.color.orangre));
    }
}