package com.minorproject.android.esrf;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.minorproject.android.esrf.Helping_Classes.statics;
import com.minorproject.android.esrf.Models.firstAidLoc;
import java.util.ArrayList;

public class firstAidAdapter extends RecyclerView.Adapter<firstAidAdapter.MyViewHolder> {
    public ArrayList<firstAidLoc> firstAidLocArrayList;
    public Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name,distance,type;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.name);
            distance = (TextView)view.findViewById(R.id.distance);
            type = (TextView)view.findViewById(R.id.type);
        }

    }

    public firstAidAdapter(Context mContext, ArrayList<firstAidLoc> firstAidLocArrayList) {
        this.mContext = mContext;
        this.firstAidLocArrayList = firstAidLocArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.firstaid_card, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        firstAidLoc floc = firstAidLocArrayList.get(position);
        holder.name.setText(floc.name);
        holder.type.setText(floc.type.toUpperCase());
        holder.distance.setText(Float.toString(floc.distance)+"m");
    }




    /*public String calcDistance(Double lat,Double lon){
        String distance;
        float distances[] = new float[1];
        Location.distanceBetween(statics.currLat,
                statics.currLong,
                lat,
                lon, distances);
        distance = Float.toString(distances[0]);
        return distance;

    }*/

    @Override
    public int getItemCount() {
        return firstAidLocArrayList.size();
    }


}
