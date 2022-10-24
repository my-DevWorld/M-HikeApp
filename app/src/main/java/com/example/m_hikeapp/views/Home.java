package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.m_hikeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    private void init(){
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setItemActiveIndicatorColor(null);
        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, AddHike.class));
        });
    }
}