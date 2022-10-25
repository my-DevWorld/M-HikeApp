package com.example.m_hikeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> hikeID, hikeName, location, date, distance,
            purposeOfHike, description, numberOfPersons, parkingAvailable, camping, imageURL;

    public HikeAdapter(Context context, ArrayList<String> hikeID, ArrayList<String> hikeName,
                       ArrayList<String> location, ArrayList<String> date, ArrayList<String> distance,
                       ArrayList<String> purposeOfHike, ArrayList<String> description, ArrayList<String> numberOfPersons,
                       ArrayList<String> parkingAvailable, ArrayList<String> camping, ArrayList<String> imageURL) {
        this.context = context;
        this.hikeID = hikeID;
        this.hikeName = hikeName;
        this.location = location;
        this.date = date;
        this.distance = distance;
        this.purposeOfHike = purposeOfHike;
        this.description = description;
        this.numberOfPersons = numberOfPersons;
        this.parkingAvailable = parkingAvailable;
        this.camping = camping;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public HikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hike_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeAdapter.ViewHolder holder, int position) {
        holder.hikeName_location.setText(String.format("%s%s%s", String.valueOf(hikeName.get(position)), context.getString(R.string.separator), location.get(position)));
        holder.hikeDetails.setText(String.valueOf(date.get(position)) + " ∙ " + distance.get(position) + " ∙ " + numberOfPersons.get(position));
            Glide.with(context)
                    .load(imageURL.get(position))
                    .placeholder(R.drawable.greyscale_landscape)
                    .error(R.drawable.greyscale_landscape)
                    .into(holder.hikeImage);
    }

    @Override
    public int getItemCount() {
        return hikeID.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView addToFav, share;
        ImageView hikeImage;
        TextView hikeName_location;
        TextView hikeDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addToFav = itemView.findViewById(R.id.addToFav);
            hikeImage = itemView.findViewById(R.id.hikeImage);
            hikeName_location = itemView.findViewById(R.id.hikeName_location);
            hikeDetails = itemView.findViewById(R.id.hikeDetails);
            share = itemView.findViewById(R.id.share);
        }
    }
}
