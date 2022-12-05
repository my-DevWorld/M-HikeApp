package com.example.m_hikeapp.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.sqlite.DatabaseConnection;
import com.example.m_hikeapp.views.Home;
import com.google.android.material.snackbar.Snackbar;

public class DetailsConfirmationViewModel extends AndroidViewModel {

    private DatabaseConnection dbConnection;

    public DetailsConfirmationViewModel(@NonNull Application application) {
        super(application);
        dbConnection = new DatabaseConnection(application.getApplicationContext());
    }

    public void insertHike(Activity activity, Hike hike) {
        //TODO: Load progress bar
        if (dbConnection.addHike(hike) == -1){
            //TODO: spot progress bar
            Snackbar.make(activity.findViewById(R.id.submitBtn), getApplication().getResources().getString(R.string.hike_not_created), Snackbar.LENGTH_SHORT).show();
        } else {
            //TODO: spot progress bar
            Toast.makeText(getApplication(), R.string.hike_created_successfully, Toast.LENGTH_SHORT).show();
            activity.startActivity(new Intent(activity, Home.class));
            activity.finish();
        }
    }

    public void updateHike(Activity activity, Hike hike) {
        if (dbConnection.updateHike(hike) == -1){
            //TODO: spot progress bar
            Snackbar.make(activity.findViewById(R.id.submitBtn), getApplication().getResources().getString(R.string.hike_not_updated), Snackbar.LENGTH_SHORT).show();
        } else {
            //TODO: spot progress bar
            Toast.makeText(getApplication(), R.string.hike_update_successfully, Toast.LENGTH_SHORT).show();
            activity.startActivity(new Intent(activity, Home.class));
            activity.finish();
        }
    }
}
