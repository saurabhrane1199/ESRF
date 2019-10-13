package com.minorproject.android.esrf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolBar;
    public Intent serviceIntent;
    public Intent intent;
    public User currUser;
    public Bundle b;
    public static final String TAG ="Main Activity";
    public String uid;
    public DatabaseReference dbref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(MainActivity.this,LocationBgService.class);
        handlePermissions();
        startService(serviceIntent);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbref = FirebaseDatabase.getInstance().getReference("/users/"+uid);


        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
                Log.d(TAG,"Heyy"+currUser.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,"Error ocurred"+databaseError.toException());
            }
        });


        toolBar = getSupportActionBar();
        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolBar.setTitle("Shop");
        loadFragment(new HomeFragment());
    }

    private void handlePermissions() {
        int locationRequestCode = 1000;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        else{
            startService(serviceIntent);
            //startActivity(new Intent(helpScreen.this,LocationMapTrial.class));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startService(serviceIntent);
                    //startActivity(new Intent(helpScreen.this,LocationMapTrial.class));
                }
                else
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_user:
                    toolBar.setTitle("Shop");
                    fragment = new UserFragment();
                    fragment.setArguments(b);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_home:
                    toolBar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_firstAid:
                    toolBar.setTitle("First Aid");
                    fragment = new FirstAid();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }






}
