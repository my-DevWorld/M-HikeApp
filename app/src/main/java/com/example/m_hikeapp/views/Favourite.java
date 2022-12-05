package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.adapters.HikeAdapter;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.viewmodels.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Favourite extends AppCompatActivity implements HikeAdapter.ItemClickListener {

    private HomeViewModel viewModel;

    private BottomNavigationView bottomNavigation;
    private RecyclerView hikeRecyclerView;
    private NestedScrollView nestedScrollView;
    private ImageView emptyState;
    private TextView emptyStateText;

    private HikeAdapter adapter;
    private ArrayList<Hike> hikeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        //        ViewModel instantiation
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        init();
    }

    private void init() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setItemActiveIndicatorColor(null);
        bottomNavigation.setSelectedItemId(R.id.saved);
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(this, Home.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.saved:
                    return true;
                case R.id.account:
                    startActivity(new Intent(this, Account.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        });

        emptyState = findViewById(R.id.emptyState);
        emptyStateText = findViewById(R.id.emptyStateText);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        hikeRecyclerView = findViewById(R.id.hikeRecyclerView);
        adapter = new HikeAdapter(this, hikeArrayList, this);
        hikeRecyclerView.setAdapter(adapter);
        hikeRecyclerView.setLayoutManager(new LinearLayoutManager(Favourite.this));

        //        Observer for Hikes mutableData
        viewModel.getHikeObserver().observe(this, hikes -> {
            hikeArrayList = new ArrayList<>();
            for (Hike hike : hikes) {
                if (hike.getHikeSaved() == 1) {
                    hikeArrayList.add(hike);
                }
            }
            if (hikeArrayList.isEmpty()){
                hikeRecyclerView.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
                emptyStateText.setVisibility(View.VISIBLE);
            }
            adapter.setHikeArrayList(hikeArrayList);
        });
        nestedScrollView.setNestedScrollingEnabled(false);

//        Get all Hikes from ViewModel
        viewModel.readAllHikes(this);
    }

    @Override
    public void onHikeClick(Hike hike, String viewClicked) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
        overridePendingTransition(0, 0);
        finish();
        super.onBackPressed();
    }
}