package com.example.m_hikeapp.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.models.HikeObservation;
import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HikeObservation> hikeObservations;
    private ItemClickListener itemClickListener;

    public ObservationAdapter(Context context, ArrayList<HikeObservation> hikeObservations, ItemClickListener itemClickListener) {
        this.context = context;
        this.hikeObservations = hikeObservations;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ObservationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.observation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationAdapter.ViewHolder holder, int position) {
        HikeObservation hikeObservation = hikeObservations.get(position);
        holder.observationName.setText(hikeObservation.getObservation());
        holder.observationTime.setText(hikeObservation.getTime());
    }

    @Override
    public int getItemCount() {
        if (hikeObservations != null) {
            return hikeObservations.size();
        }
        return 0;
    }

    public void setHikeObservationsArrayList(ArrayList<HikeObservation> hikeObservations) {
        this.hikeObservations = hikeObservations;
        notifyDataSetChanged();
    }

    public void refresh() {
        hikeObservations.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView observationName, observationTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            observationName = itemView.findViewById(R.id.observationName);
            observationTime = itemView.findViewById(R.id.observationTime);
        }
    }

    public interface ItemClickListener {
        void onHikeObservationClick(HikeObservation hikeObservation);
    }
}
