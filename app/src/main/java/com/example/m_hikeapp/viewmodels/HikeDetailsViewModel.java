package com.example.m_hikeapp.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.m_hikeapp.R;
import com.example.m_hikeapp.models.HikeObservation;
import com.example.m_hikeapp.sqlite.DatabaseConnection;
import com.example.m_hikeapp.views.Home;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class HikeDetailsViewModel extends AndroidViewModel {

    private DatabaseConnection dbConnection;

    private HikeObservation hikeObservation;
    private ArrayList<HikeObservation> hikeObservations;
    private MutableLiveData<ArrayList<HikeObservation>> hikeObservationsMutableLiveData;

    public HikeDetailsViewModel(@NonNull Application application) {
        super(application);

//        Database instantiation
        dbConnection = new DatabaseConnection(application.getApplicationContext());

//        MutableLiveData and ArrayList instantiation
        hikeObservations = new ArrayList<>();
        hikeObservationsMutableLiveData = new MutableLiveData<>();
    }

//    Save hike observation to database.
    public boolean storeHikeObservationToDatabase(Activity activity, HikeObservation hikeObservation) {
        if (dbConnection.addObservation(hikeObservation) == -1) {
            //TODO: spot progress bar
            Snackbar.make(activity.findViewById(R.id.saveObservationBtn), getApplication().getResources().getString(R.string.observation_not_created), Snackbar.LENGTH_SHORT).show();
            return false;
        } else {
            //TODO: spot progress bar
            return true;
        }
    }

//    Read all observation for a hike in the database.
    public void readAllObservation(int id) {
//        instantiate a cursor object to get each individual data
        Cursor cursor = dbConnection.getObservations(id);

        if (cursor.getCount() == 0) {
        } else {
            while (cursor.moveToNext()) {
//                Get the data from each observation
                String observationId = cursor.getString(0);
                String observation = cursor.getString(1);
                String time = cursor.getString(2);
                String additionalComment = cursor.getString(3);
                String hikeObservationImage = cursor.getString(4);

//                Store the data in the HikeObservation model class
                hikeObservation = new HikeObservation(Integer.valueOf(observationId), observation, time, additionalComment, hikeObservationImage);

//                Add each observation to the HikeObservation arraylist.
                hikeObservations.add(hikeObservation);
            }
//
//            Reverse the order of data in the HikeObservation arraylist
            Collections.reverse(hikeObservations);

//            Save the HikeObservation arraylist in a MutableLiveData object
            hikeObservationsMutableLiveData.postValue(hikeObservations);
        }
    }

//    Return the MutableLiveData object of the HikeObservation
    public MutableLiveData<ArrayList<HikeObservation>> getHikeObservationObserver() {
        return hikeObservationsMutableLiveData;
    }

//    Method to delete a Hike
    public void deleteHike(Activity activity, String id){
        if (dbConnection.deleteHike(id) == -1) {
//            On failed deletion, prompt user to try again.
            Snackbar.make(activity.findViewById(R.id.delete), R.string.delete_unsuccessful, Snackbar.LENGTH_SHORT).show();
        } else {
//            On successful deletion, navigate to Home activity.
            Toast.makeText(activity, activity.getString(R.string.hike_deleted), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}


























