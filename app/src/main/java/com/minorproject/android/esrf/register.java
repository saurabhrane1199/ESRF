package com.minorproject.android.esrf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    EditText name,email,number,er1name,er2name,er1no,er2no,bg;
    Button submit;
    User user;
    DatabaseReference dbref;
    FirebaseUser mAuth;
    public static final String TAG = "register.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = bindUser();
                submitDataToFb(user);
            }
        });
    }

    void init(){
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        number = (EditText)findViewById(R.id.number);
        er1name = (EditText)findViewById(R.id.er1name);
        er2name = (EditText)findViewById(R.id.er2name);
        er1no = (EditText)findViewById(R.id.er1no);
        er2no = (EditText)findViewById(R.id.er2no);
        bg = (EditText)findViewById(R.id.bg);
        submit = (Button)findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
    }

    User bindUser(){
        String n = name.getText().toString();
        String e = email.getText().toString();
        String num = number.getText().toString();
        String e1name = er1name.getText().toString();
        String e2name = er2name.getText().toString();
        String e1no = er1no.getText().toString();
        String e2no = er2no.getText().toString();
        String b = bg.getText().toString();

        User user1 = new User(n,e,0.0,0.0,25.0,num,e1name,e2name,e1no,e2no,b);

        return user1;
    }

    void submitDataToFb(User u){
        String uid = mAuth.getUid();
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("users").child(uid).setValue(u);
        Log.d(TAG,"Entered submitDataToFb");
        Intent intent = new Intent(register.this,MainActivity.class);
        intent.putExtra("curruser",u);
        startActivity(intent);
    }





}
