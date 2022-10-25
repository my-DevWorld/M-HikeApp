package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m_hikeapp.HikeAdapter;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.sqlite.DatabaseConnection;
import com.example.m_hikeapp.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fab;
    private ImageView emptyState;
    private TextView emptyStateText;
    private RecyclerView hikeRecyclerView;

    private DatabaseConnection db;
    private ArrayList<String> hikeID, hikeName, location, date, distance,
            purposeOfHike, description, numberOfPersons, parkingAvailable, camping, imageURL;
    private HikeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new DatabaseConnection(this);
        init();
    }

    private void init(){
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setItemActiveIndicatorColor(null);
        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, AddHike.class));
        });
        emptyState = findViewById(R.id.emptyState);
        emptyStateText = findViewById(R.id.emptyStateText);
        hikeRecyclerView = findViewById(R.id.hikeRecyclerView);

        hikeID = new ArrayList<>();
        hikeName = new ArrayList<>();
        location = new ArrayList<>();
        date = new ArrayList<>();
        distance = new ArrayList<>();
        purposeOfHike = new ArrayList<>();
        description = new ArrayList<>();
        numberOfPersons = new ArrayList<>();
        parkingAvailable = new ArrayList<>();
        camping = new ArrayList<>();
        imageURL = new ArrayList<>();

        readAllData();
        adapter = new HikeAdapter(this, hikeID, hikeName, location,
                date, distance, purposeOfHike, description, numberOfPersons, parkingAvailable, camping, imageURL);
        hikeRecyclerView.setAdapter(adapter);
        hikeRecyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
    }

    private void readAllData() {
        Cursor cursor = db.getAllData();
        if (cursor.getCount() == 0){
            emptyState.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.VISIBLE);
        }
        else {
            while (cursor.moveToNext()) {
                hikeID.add(cursor.getString(0));
                hikeName.add(cursor.getString(1));
                location.add(cursor.getString(2));
                date.add(cursor.getString(3));
                distance.add(cursor.getString(4));
                purposeOfHike.add(cursor.getString(5));
                description.add(cursor.getString(6));
                numberOfPersons.add(cursor.getString(7));
                parkingAvailable.add(cursor.getString(8));
                camping.add(cursor.getString(9));
//                imageURL.add(cursor.getString(10));
            }
            imageURL.add(Constants.HIKE_THUMBNAIL1);
            imageURL.add(Constants.HIKE_THUMBNAIL2);
            imageURL.add(Constants.HIKE_THUMBNAIL3);
            imageURL.add(Constants.HIKE_THUMBNAIL4);
            imageURL.add(Constants.HIKE_THUMBNAIL5);
        }
    }
}