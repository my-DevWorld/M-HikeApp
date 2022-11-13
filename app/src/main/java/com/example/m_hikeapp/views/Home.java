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
import android.widget.Toast;

import com.example.m_hikeapp.adapters.HikeAdapter;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.sqlite.DatabaseConnection;
import com.example.m_hikeapp.utils.Constants;
import com.example.m_hikeapp.viewmodels.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements HikeAdapter.ItemClickListener {

    private HomeViewModel viewModel;

    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fab;
    private ImageView emptyState;
    private TextView emptyStateText, deleteAllHikes;
    private RecyclerView hikeRecyclerView;
    private NestedScrollView nestedScrollView;

    private DatabaseConnection db;
    private ArrayList<Hike> hikeArrayList;
    private HikeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        ViewModel instantiation
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

//        Database instantiation
        db = new DatabaseConnection(this);
        init();
    }

    private void init() {
        //        initialise view objects
        nestedScrollView = findViewById(R.id.nestedScrollView);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setItemActiveIndicatorColor(null);
        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, AddHike.class));
            finish();
        });
        emptyState = findViewById(R.id.emptyState);
        deleteAllHikes = findViewById(R.id.deleteAllHikes);
        emptyStateText = findViewById(R.id.emptyStateText);
        hikeRecyclerView = findViewById(R.id.hikeRecyclerView);

//        Instantiate recyclerview adapter
        adapter = new HikeAdapter(this, hikeArrayList, this);
        hikeRecyclerView.setAdapter(adapter);
        hikeRecyclerView.setLayoutManager(new LinearLayoutManager(Home.this));

//        Observer for Hikes mutableData
        viewModel.getHikeObserver().observe(this, hikes -> {
            adapter.setHikeArrayList(hikes);
        });
        nestedScrollView.setNestedScrollingEnabled(false);

//        Get all Hikes from ViewModel
        viewModel.readAllHikes(this);

//        Onclick for delete all Hikes
        deleteAllHikes.setOnClickListener(v -> {
            confirmDialog();
        });
    }

    //    OnClick listener from HikeAdapter Class
    @Override
    public void onHikeClick(Hike hike, String viewClicked) {
        if (viewClicked == Constants.VIEW_CLICKED[0]){
            Intent intent = new Intent(this, HikeDetails.class);
            String editDetails = getString(R.string.yes_caps);
            intent.putExtra(Constants.HIKE_DETAIL_KEY, editDetails);
            intent.putExtra(Constants.HIKE_DETAILS, hike);
            startActivity(intent);
        }
        else if (viewClicked == Constants.VIEW_CLICKED[2]){
            Intent intent = new Intent(this, AddHike.class);
            String editDetails = getString(R.string.yes_caps);
            String from = getString(R.string.home);
            intent.putExtra(Constants.HIKE_DETAIL_KEY, editDetails);
            intent.putExtra(Constants.HIKE_DETAIL_KEY_FROM, from);
            intent.putExtra(Constants.HIKE_DETAILS, hike);
            startActivity(intent);
            finish();
        }
    }

    //    Call deleteAllHikes from ViewModel
    private void deleteAllHikes() {
        viewModel.deleteAllHikes();
        hikeRecyclerView.setVisibility(View.GONE);
    }

    private void confirmDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Delete All Hikes");
        builder.setMessage("Are you sure you want to delete all hikes?");
        builder.setBackground(getDrawable(R.drawable.dialog_background));
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            deleteAllHikes();
            viewModel.readAllHikes(this);
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        builder.create().show();
    }
}