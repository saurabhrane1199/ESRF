package com.minorproject.android.esrf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.minorproject.android.esrf.Helping_Classes.statics;
import com.minorproject.android.esrf.Models.User;

public class register extends AppCompatActivity {
    EditText number,er1name,er2name,er1no,er2no;
    TextView warning;
    Spinner bgSpinner1;
    Button submit;
    User user;
    DatabaseReference dbref;
    FirebaseUser mAuth;
    String uid,userName,userEmail,userBG;
    ScrollView scrollView;
    GoogleSignInAccount acct;
    public static final String TAG = "register.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDataToFb();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("tokens", 0); // 0 - for private mode
        statics.token = pref.getString("token_value",null);
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("users").child(uid).child("token").setValue(statics.token);
    }

    void init(){
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            userName = acct.getDisplayName();
            userEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            TextView welcomeMsg = findViewById(R.id.welcomeMsg);
            welcomeMsg.setText("Hey"+userName);
        }
        //bg = (EditText)findViewById(R.id.bg);
        bgSpinner1 = findViewById(R.id.bGSpinner);
        bgSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userBG = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        number = (EditText)findViewById(R.id.number);
        er1name = (EditText)findViewById(R.id.er1name);
        er2name = (EditText)findViewById(R.id.er2name);
        er1no = (EditText)findViewById(R.id.er1no);
        er2no = (EditText)findViewById(R.id.er2no);
        submit = (Button)findViewById(R.id.submit);
        warning =(TextView)findViewById(R.id.warning);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    User bindUser(){
        String num = number.getText().toString();
        String e1name = er1name.getText().toString();
        String e2name = er2name.getText().toString();
        String e1no = er1no.getText().toString();
        String e2no = er2no.getText().toString();
        User user1 = new User(userName,userEmail,0.0,0.0,25.0,num,e1name,e2name,e1no,e2no,userBG);
        return user1;
    }

    void submitDataToFb(){
        if(isFilled())
        {
            user = bindUser();
            dbref = FirebaseDatabase.getInstance().getReference();
            dbref.child("users").child(uid).setValue(user);
            Log.d(TAG, "Entered submitDataToFb");
            Intent intent = new Intent(register.this, MainActivity.class);
            intent.putExtra("curruser", user);
            startActivity(intent);
        }
        else{

            warning.setVisibility(View.VISIBLE);
            scrollView.smoothScrollTo(0,0);

        }
    }

    boolean isFilled(){
        if(TextUtils.isEmpty(number.getText()) || TextUtils.isEmpty(er1name.getText()) || TextUtils.isEmpty(er2name.getText()) ||
                TextUtils.isEmpty(er1no.getText()) || TextUtils.isEmpty(er2no.getText()))
            return false;
        else
            return true;

    }





}
