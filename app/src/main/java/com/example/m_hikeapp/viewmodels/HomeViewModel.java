package com.example.m_hikeapp.viewmodels;
import android.app.Activity;
import android.app.Application;
import android.database.Cursor;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.sqlite.DatabaseConnection;
import java.util.ArrayList;
import java.util.Collections;

public class HomeViewModel extends AndroidViewModel {

    private Hike hike;
    private ArrayList<Hike> hikes;
    private MutableLiveData<ArrayList<Hike>> hikeMutableLiveData;
    private DatabaseConnection db;

    public HomeViewModel(@NonNull Application application) {
        super(application);

//        Database instantiation
        db = new DatabaseConnection(application.getApplicationContext());

//        MutableLiveData and ArrayList instantiation
        hikeMutableLiveData = new MutableLiveData<>();
        hikes = new ArrayList<>();
    }

//    Read all hikes in the database.
    public void readAllHikes(Activity activity) {
//        instantiate a cursor object to get each individual data
        Cursor cursor = db.getAllHikes();

        if (cursor.getCount() == 0) {
//            Database is empty
            activity.findViewById(R.id.emptyState).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.emptyStateText).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.deleteAllHikesFAB).setVisibility(View.GONE);
        } else {
            while (cursor.moveToNext()) {
//                Get the data from each hike
                String id = cursor.getString(0);
                String hikeName = cursor.getString(1);
                String location = cursor.getString(2);
                String date = cursor.getString(3);
                String distance = cursor.getString(4);
                String purposeOfHike = cursor.getString(5);
                String description = cursor.getString(6);
                String numberOfPersons = cursor.getString(7);
                String parkingAvailable = cursor.getString(8);
                String camping = cursor.getString(9);
                String imageURL = cursor.getString(10);
                String difficultyLevel = cursor.getString(11);
                String hikeSaved = cursor.getString(12);

//                Set the hike details from the database to the hike model
                hike = new Hike(Integer.valueOf(id), hikeName, location, date, distance,
                        purposeOfHike, description, numberOfPersons, parkingAvailable, camping,
                        imageURL, difficultyLevel, Integer.valueOf(hikeSaved));

//                Add each Hike to the Hikes arraylist.
                hikes.add(hike);
            }
//            Reverse the order of data in the Hikes arraylist
            Collections.reverse(hikes);

//            Save the Hikes arraylist in a MutableLiveData object
            hikeMutableLiveData.postValue(hikes);
        }
    }

    //    Return the MutableLiveData object of the Hikes
    public MutableLiveData<ArrayList<Hike>> getHikeObserver() {
        return hikeMutableLiveData;
    }

    public void deleteAllHikes() {
        db.deleteAllHikes();
    }
}
