package com.minorproject.android.esrf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelpActivity extends AppCompatActivity {

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAaG8hVck:APA91bEovU1VFr4U0CfGywVMLIyc6-eTNLizPGClb56yF7E_p2y0f2D4_8Nnv_TBXlOCSYINc-cycXMjcTOa9J8AjyF_GgZlhQWY-DtLYrcKxfjTGeFv0of7VDh2NXw-flWRO4_zBobb";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    User currUser;
    String uid;
    DatabaseReference dbref;


    String tokentemp;

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Button btnSend = findViewById(R.id.button);
        //tokenList = new ArrayList<>();
        userInit();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TOPIC = "dpC9B8qww1s:APA91bEFsDJqEZgZ1K14nsLScBhOzdyp0BdUA-DJy8ZPlmDOd0Ml3dkGurxoKOSdfJjdddp6DEV8I_fen0mF7v4TlfAOcNnBSjoS30UBCWXvtQYsAOzxjeo0mElKeD7S90-rjqsl81kT"; //topic must match with what the receiver subscribed to
                TOPIC = getTokenFormat();
                //TOPIC = "cn8WzUWkiH0:APA91bGr2Q6bsO_IiuWIKpv5ZZSs7zIGrHziGEkdmmL5xjCJoSBArp5-Rnl8Rb9_DFv-Pr4ng7t-E9rjbdAhDT6h86RES5PPCxktQd-irrHZjpcwBR_udBP5UuuwRlXwEBngjOqYLcMi";
                NOTIFICATION_TITLE = "Help";
                NOTIFICATION_MESSAGE = "Trial";

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", TOPIC);
                    notification.put("data", notifcationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage() );
                }
                sendNotification(notification);

            }
        });
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
        new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());;
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HelpActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    public void userInit()
    {
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbref = FirebaseDatabase.getInstance().getReference("users/"+uid);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getTokenFormat(){
        String token,nameToken1,nameToken2;
        nameToken1=tokenByName(currUser.er.name1);
        //nameToken2=tokenByName(currUser.er.name2);
        token = nameToken1;
        //token="Trial";
        Log.d("Final Token",token);
        return token;
    }

    public String tokenByName(final String name){

        //final String[] nameToken={""};

        dbref = FirebaseDatabase.getInstance().getReference("users");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot dataValue : dataSnapshot.getChildren()){
                    User temp = dataValue.getValue(User.class);
                    if(temp.name.equals(name))
                    {
                        //tokenList.add(temp.token);
                        tokentemp = temp.token;
                        Log.d("Inside tokenByName",temp.token);
                        Log.d("Inside tokenByName","Value of name is"+tokentemp);

                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }

        });

        return tokentemp;

    }



    }

