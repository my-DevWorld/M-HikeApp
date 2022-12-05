package com.example.m_hikeapp.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private ArrayList<Hike> hikeArrayList, allHikes;
    private final ItemClickListener itemClickListener;

    public HikeAdapter(Context context, ArrayList<Hike> hikeArrayList, ItemClickListener itemClickListener) {
        this.context = context;
        this.hikeArrayList = hikeArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hike_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeAdapter.ViewHolder holder, int position) {
        Hike hike = hikeArrayList.get(position);
        holder.hikeName_location.setText(String.format("%s%s%s", hike.getHikeName(), context.getString(R.string.separator), hike.getLocation()));
        holder.hikeDetails.setText(hike.getDate() + " ∙ " + hike.getDistance() + " ∙ " + hike.getNumberOfPersons());

        holder.hikeImage.setOnClickListener(view -> itemClickListener.onHikeClick(hike, Constants.VIEW_CLICKED[0]));
        holder.share.setOnClickListener(view -> itemClickListener.onHikeClick(hike, Constants.VIEW_CLICKED[1]));
        holder.editHike.setOnClickListener(view -> itemClickListener.onHikeClick(hike, Constants.VIEW_CLICKED[2]));

            Glide.with(context)
                    .load(Constants.HIKE_THUMBNAIL1)
                    .placeholder(R.drawable.greyscale_landscape)
                    .error(R.drawable.greyscale_landscape)
                    .into(holder.hikeImage);
    }

    @Override
    public int getItemCount() {
        if(hikeArrayList != null){
            return hikeArrayList.size();
        }
        return 0;
    }

    public void setHikeArrayList(ArrayList<Hike> hikeArrayList){
        this.hikeArrayList = hikeArrayList;
        allHikes = new ArrayList<>(hikeArrayList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return hikeFilter;
    }

    private Filter hikeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Hike> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(allHikes);
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Hike hike : allHikes){
                    if(hike.getHikeName().toLowerCase().startsWith(filterPattern)
                            && hike.getHikeName().toLowerCase().contains(filterPattern)){
                        filteredList.add(hike);
                    }
                }
            }
            FilterResults result = new FilterResults();
            result.values = filteredList;
            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (hikeArrayList != null && !hikeArrayList.isEmpty()){
                hikeArrayList.clear();
            }
            hikeArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hikeImage, share, editHike;
        TextView hikeName_location;
        TextView hikeDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hikeImage = itemView.findViewById(R.id.hikeImage);
            hikeName_location = itemView.findViewById(R.id.hikeName_location);
            hikeDetails = itemView.findViewById(R.id.hikeDetails);
            share = itemView.findViewById(R.id.share);
            editHike = itemView.findViewById(R.id.editHike);
        }
    }
    public interface ItemClickListener{
        void onHikeClick(Hike hike, String viewClicked);
    }
}
