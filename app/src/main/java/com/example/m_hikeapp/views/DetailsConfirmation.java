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

import org.checkerframework.common.subtyping.qual.Bottom;

public class DetailsConfirmation extends AppCompatActivity {

    private DetailsConfirmationViewModel viewModel;

    private TextInputEditText hikeName, location, date, distance, hikePurpose, description, numbOfPersons;
    private TextView editLabel, camping, parking;
    private Button submitBtn;

    private Hike hike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_confirmation);

        viewModel = new ViewModelProvider(this).get(DetailsConfirmationViewModel.class);

        Intent intent = getIntent();
        hike = intent.getParcelableExtra(Constants.HIKE_DETAILS);
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


        hikeName.setText(hike.getHikeName());
        location.setText(hike.getLocation());
        date.setText(hike.getDate());
        distance.setText(hike.getDistance());
        hikePurpose.setText(hike.getPurposeOfHike());
        description.setText(hike.getDescription());
        numbOfPersons.setText(hike.getNumberOfPersons());
        camping.setText(hike.getCamping());
        parking.setText(hike.getParkingAvailable());

        editLabel.setOnClickListener(v -> {
            onBackPressed();
        });

        submitBtn.setOnClickListener(view -> {
            viewModel.insertHike(this, hike);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AddHike.class);
        String editDetails = getString(R.string.yes_caps);
        intent.putExtra(Constants.EDIT_DETAILS_KEY, editDetails);
        intent.putExtra(Constants.HIKE_DETAILS, hike);
        startActivity(intent);
        finish();
    }
}