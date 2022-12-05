package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.m_hikeapp.adapters.HikeAdapter;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.httpnetwork.MHikeRetrofitClient;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.models.HikePayload;
import com.example.m_hikeapp.sqlite.DatabaseConnection;
import com.example.m_hikeapp.utils.Constants;
import com.example.m_hikeapp.viewmodels.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements HikeAdapter.ItemClickListener {

    private HomeViewModel viewModel;

    private CardView search_bar;
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fab,deleteAllHikesFAB;
    private ImageView emptyState;
    private TextView emptyStateText, deleteAllHikes;
    private RecyclerView hikeRecyclerView;
    private NestedScrollView nestedScrollView;
    private SearchView searchView;
    private EditText searchHike;

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
        search_bar = findViewById(R.id.search_bar);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setItemActiveIndicatorColor(null);

        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    return true;
                case R.id.saved:
                    startActivity(new Intent(this, Favourite.class));
                    overridePendingTransition(0,0);
                    finish();
                    return true;
                case R.id.account:
                    startActivity(new Intent(this, Account.class));
                    overridePendingTransition(0,0);
                    finish();
                    return true;
            }
            return false;
        });

        deleteAllHikesFAB = findViewById(R.id.deleteAllHikesFAB);
        fab = findViewById(R.id.floating_action_button);

        fab.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, AddHike.class));
            finish();
        });

//        FAB to delete all hike from the database
        deleteAllHikesFAB.setOnClickListener(v -> {
            confirmDialog();
        });
        searchHike = findViewById(R.id.searchHike);
        searchView = findViewById(R.id.searchView);
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
            HikePayload hikePayload = new HikePayload(getString(R.string.user_id), hikes, 0);
            String gson = new Gson().toJson(hikePayload);
            uploadToCloud(hikePayload);
        });
        nestedScrollView.setNestedScrollingEnabled(false);

//        Get all Hikes from ViewModel
        viewModel.readAllHikes(this);

//        Onclick for delete all Hikes
        deleteAllHikes.setOnClickListener(v -> {
            confirmDialog();
        });

        search_bar.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchHike.class));
        });

//      Text Changed Listener for the search hike edit text
        searchHike.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable != null){
                    searchView.setQuery(searchHike.getText().toString(), false);
                }
            }
        });

//       SearchView Query Text Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    //    OnClick listener from HikeAdapter Class
    @Override
    public void onHikeClick(Hike hike, String viewClicked) {
        if (viewClicked == Constants.VIEW_CLICKED[0]) {
            Intent intent = new Intent(this, HikeDetails.class);
            String editDetails = getString(R.string.yes_caps);
            intent.putExtra(Constants.HIKE_DETAIL_KEY, editDetails);
            intent.putExtra(Constants.HIKE_DETAILS, hike);
            startActivity(intent);
            finish();
        } else if (viewClicked == Constants.VIEW_CLICKED[2]) {
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
        builder.setTitle(getString(R.string.delete_all_hikes));
        builder.setMessage(R.string.delete_all_message);
        builder.setBackground(getDrawable(R.drawable.dialog_background));
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            deleteAllHikes();
            viewModel.readAllHikes(this);
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
        });
        builder.create().show();
    }

    private void uploadToCloud(HikePayload hikePayload) {
        Call<ResponseBody> uploadHikes = MHikeRetrofitClient.getInstance()
                .getMHikeRestApi().uploadToCloud(hikePayload);
        uploadHikes.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody data = response.body();
                if (response.isSuccessful()) {
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}