package com.example.serviceordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class RegistrationActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText phoneEditText;
    Button saveBT;
    Button cancelBT;
    TextView titleTV;
    RadioGroup phoneTypeGroup;
    Spinner spinner;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mUsers;

    private String interest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        phoneTypeGroup = findViewById(R.id.phoneTypeGroup);
        saveBT= findViewById(R.id.buttonSave);
        cancelBT=findViewById(R.id.buttonCancel);
        titleTV=findViewById(R.id.registerTextView);
        phoneTypeGroup.check(R.id.home);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        emailEditText.setText(userName);
        passwordEditText.setText(password);

        spinner = findViewById(R.id.serviceSpinner);
        spinner.setOnItemSelectedListener( this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.item_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mFirestore = FirebaseFirestore.getInstance();
        mUsers = mFirestore.collection("Users");


    }

    public void cancel(View view) {
        finish();
    }

    public void registration(View view) {

        String userName = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        int phoneTypeId = phoneTypeGroup.getCheckedRadioButtonId();
        View radioButton = phoneTypeGroup.findViewById(phoneTypeId);
        int id = phoneTypeGroup.indexOfChild(radioButton);
        String phoneType =  ((RadioButton)phoneTypeGroup.getChildAt(id)).getText().toString();
        
        mUsers.document().set(new User(userName,email,phone,phoneType, interest));

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created successfully");
                    bactToMain();

                } else {
                    Log.d(LOG_TAG, "User was't created successfully:", task.getException());
                }
            }
        });

    }

    private void bactToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());
        editor.apply();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        saveBT.setBackgroundColor(getResources().getColor(R.color.orangre));
        cancelBT.setBackgroundColor(getResources().getColor(R.color.orangre));
        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .repeat(1)
                .playOn(titleTV);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        interest =selectedItem;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}