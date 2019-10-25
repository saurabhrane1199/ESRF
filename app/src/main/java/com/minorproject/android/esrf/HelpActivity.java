package com.minorproject.android.esrf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minorproject.android.esrf.Helping_Classes.statics;
import com.minorproject.android.esrf.Models.User;

import org.json.JSONArray;
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
    private User currUser;
    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_MESSAGE;
    private ArrayList<String> tokenList;
    private Location userLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //userLoc = new Location("gps");
        currUser =(User)getIntent().getSerializableExtra("currUser");
        Button btnSend = findViewById(R.id.button);
        tokenList = new ArrayList<>();
        //userLoc.setLatitude(currUser.latitude);
        //userLoc.setLongitude(currUser.longitude);

        Log.d("Intent Value",currUser.name);
        populateTokenList();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NOTIFICATION_TITLE = "Help";
                NOTIFICATION_MESSAGE = statics.currentLoc;
                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                JSONArray  jsonArray = new JSONArray(tokenList);
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);
                    notification.put("registration_ids", jsonArray);
                    notification.put("data", notifcationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage() );
                }
                Log.d("TOKEN SIZE",Integer.toString(tokenList.size()));
                sendNotification(notification);
            }
        });

    }



    public void populateTokenList(){

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User temp;
                for(DataSnapshot dataValue : dataSnapshot.getChildren()){
                   temp = dataValue.getValue(User.class);
                   /*if(temp.name.equals(currUser.er.name1) || temp.name.equals(currUser.er.name2) ){
                        //tokenByName(temp);
                   }*/
                   if(ifLocation(temp)){
                       tokenByLocation(temp);

                   }



                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
        new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
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

    private boolean ifLocation(User user){
        float[] distances = new float[1];
        boolean flag = true;
        Location.distanceBetween(currUser.latitude,
                currUser.longitude,
                user.latitude,
                user.longitude, distances);
        //flag = distances[0]<1000.0;

        return flag;
    }

    public void tokenByLocation(User temp){
        tokenList.add(temp.token);

    }

    public void tokenByName(User temp){
            tokenList.add(temp.token);
        }




}

