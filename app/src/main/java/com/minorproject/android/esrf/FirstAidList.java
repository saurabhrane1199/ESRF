package com.minorproject.android.esrf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minorproject.android.esrf.Fragment.FirstAid;
import com.minorproject.android.esrf.Models.firstAidLoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FirstAidList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private firstAidAdapter adapter;
    private ArrayList<firstAidLoc> firstAidLocArrayList;
    private DatabaseReference dbref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_list);

        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        firstAidLocArrayList = new ArrayList<>();
        setFirstAidList();

    }

    public void setFirstAidList(){
        dbref = FirebaseDatabase.getInstance().getReference().child("firstAid");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot temp: dataSnapshot.getChildren()) {
                        firstAidLoc floc = temp.getValue(firstAidLoc.class);
                        firstAidLocArrayList.add(temp.getValue(firstAidLoc.class));
                }
                setcustomAdapter();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void setcustomAdapter(){
        adapter = new firstAidAdapter(FirstAidList.this,firstAidLocArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(FirstAidList.this));
        recyclerView.setAdapter(adapter);
    }

}
