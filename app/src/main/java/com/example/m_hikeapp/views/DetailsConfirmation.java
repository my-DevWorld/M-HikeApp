package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.m_hikeapp.R;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.utils.Constants;
import com.example.m_hikeapp.viewmodels.DetailsConfirmationViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class DetailsConfirmation extends AppCompatActivity {

    private DetailsConfirmationViewModel viewModel;

    private TextInputEditText hikeName, location, date, distance, hikePurpose, description, numbOfPersons;
    private TextView editLabel, camping, parking, difficultLevelLabel;
    private Button submitBtn;

    private Hike hike;
    private String from;
    private Intent intent;
    private int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_confirmation);

        viewModel = new ViewModelProvider(this).get(DetailsConfirmationViewModel.class);

        intent = getIntent();
        from = intent.getStringExtra(Constants.HIKE_DETAIL_KEY_FROM);
        hike = intent.getParcelableExtra(Constants.HIKE_DETAILS);
        hikeId = hike.getId();
        init();
    }

    private void init() {
        hikeName = findViewById(R.id.hikeName);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        distance = findViewById(R.id.distance);
        hikePurpose = findViewById(R.id.hikePurpose);
        description = findViewById(R.id.description);
        numbOfPersons = findViewById(R.id.numbOfPersons);
        editLabel = findViewById(R.id.editLabel);
        camping = findViewById(R.id.camping);
        parking = findViewById(R.id.parking);
        submitBtn = findViewById(R.id.submitBtn);
        difficultLevelLabel = findViewById(R.id.difficultLevelLabel);

//      Setting hike details to the views
        hikeName.setText(hike.getHikeName());
        location.setText(hike.getLocation());
        date.setText(hike.getDate());
        distance.setText(hike.getDistance());
        hikePurpose.setText(hike.getPurposeOfHike());
        description.setText(hike.getDescription());
        numbOfPersons.setText(hike.getNumberOfPersons());
        camping.setText(hike.getCamping());
        parking.setText(hike.getParkingAvailable());
        difficultLevelLabel.setText(String.format("%s%s", getString(R.string.difficulty_level1), hike.getHikeDifficultyLevel()));

        editLabel.setOnClickListener(v -> {
            onBackPressed();
        });

        if (hikeId != 0){
            submitBtn.setText(getString(R.string.update));
        }

        submitBtn.setOnClickListener(view -> {
            if (submitBtn.getText().toString().trim().equals(getString(R.string.update))){
                viewModel.updateHike(this, hike);
            }
            else {
                viewModel.insertHike(this, hike);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AddHike.class);
        String editDetails = getString(R.string.yes_caps);
        intent.putExtra(Constants.HIKE_DETAIL_KEY, editDetails);
        intent.putExtra(Constants.HIKE_DETAILS, hike);
        startActivity(intent);
        finish();
    }
}