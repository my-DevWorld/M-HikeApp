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
        if (dbConnection.addHike(hike) == -1){
            Snackbar.make(activity.findViewById(R.id.submitBtn), getApplication().getResources().getString(R.string.hike_not_created), Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "Success", Toast.LENGTH_SHORT).show();
            getApplication().startActivity(new Intent(activity, Home.class));
            activity.finish();
        }
    }
}
