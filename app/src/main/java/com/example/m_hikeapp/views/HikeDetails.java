package com.example.m_hikeapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.m_hikeapp.R;
import com.example.m_hikeapp.adapters.ObservationAdapter;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.models.HikeObservation;
import com.example.m_hikeapp.utils.Constants;
import com.example.m_hikeapp.viewmodels.HikeDetailsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HikeDetails extends AppCompatActivity implements ObservationAdapter.ItemClickListener {

    private HikeDetailsViewModel viewModel;

    private ImageView addToFav, share, hike_image1, delete;
    private TextView hikeName1, location1, hikeDate1, hikeDistance1, numbOfPersons1, camping1,
            parking1, addObservation, hikeDescription;
    private TextView hikeDetails, cancelBtn;
    private TextInputLayout observationTextOutline;
    private TextInputEditText observationText, additionalComment;
    private ImageView cameraIcon;
    private Button saveObservationBtn;
    private Image image = null;
    private Dialog dialog;
    private RecyclerView observationRecyclerView;

    private Hike hike;
    private Intent intent;
    private ObservationAdapter observationAdapter;
    private ArrayList<HikeObservation> hikeObservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_details);

        viewModel = new ViewModelProvider(this).get(HikeDetailsViewModel.class);
        intent = getIntent();
        init();

    }

    private void init() {
//        initialise view objects
        addToFav = findViewById(R.id.addToFav1);
        hike_image1 = findViewById(R.id.hike_image1);
        delete = findViewById(R.id.delete);
        hikeName1 = findViewById(R.id.hikeName1);
        location1 = findViewById(R.id.location1);
        hikeDate1 = findViewById(R.id.hikeDate1);
        hikeDistance1 = findViewById(R.id.hikeDistance1);
        numbOfPersons1 = findViewById(R.id.numbOfPersons1);
        camping1 = findViewById(R.id.camping1);
        parking1 = findViewById(R.id.parking1);
        hikeDescription = findViewById(R.id.hikeDescription);
        addObservation = findViewById(R.id.addObservation);
        observationRecyclerView = findViewById(R.id.observationRecyclerView);

        displayHike();

//        Recyclerview prep
        observationAdapter = new ObservationAdapter(this, hikeObservations, this);
        observationRecyclerView.setAdapter(observationAdapter);
        observationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getHikeObservationObserver().observe(this, hikeObservations -> {
            observationAdapter.setHikeObservationsArrayList(hikeObservations);
        });

        addObservation.setOnClickListener(v -> {
            openDialog();
        });

        delete.setOnClickListener(v -> {
            confirmDialog();
        });
    }

    private void displayHike() {
        String editDetails = intent.getStringExtra(Constants.HIKE_DETAIL_KEY);

        if (editDetails != null) {
            hike = intent.getParcelableExtra(Constants.HIKE_DETAILS);
            Glide.with(this)
                    .load(Constants.HIKE_THUMBNAIL1)
                    .placeholder(R.drawable.greyscale_landscape)
                    .error(R.drawable.greyscale_landscape)
                    .into(hike_image1);
            hikeName1.setText(hike.getHikeName());
            location1.setText(hike.getLocation());
            hikeDate1.setText(hike.getDate());
            hikeDistance1.setText(String.format("%s%s", hike.getDistance(), getString(R.string.km)));
            numbOfPersons1.setText(hike.getNumberOfPersons());
            camping1.setText(String.format("%s%s", getString(R.string.camping1), hike.getCamping()));
            parking1.setText(String.format("%s%s", getString(R.string.parking), hike.getParkingAvailable()));
            if (!TextUtils.isEmpty(hike.getDescription())){
                hikeDescription.setText(getString(R.string.description).concat(": ").concat(hike.getDescription()));
            }
            viewModel.readAllObservation(hike.getId());
        }
    }

    private void openDialog() {

        dialog = new Dialog(this, R.style.CustomDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View customDialog = inflater.inflate(R.layout.observation_details, null);

        observationTextOutline = customDialog.findViewById(R.id.observationTextOutline);
        observationText = customDialog.findViewById(R.id.observationText);
        additionalComment = customDialog.findViewById(R.id.additionalComment);
        cameraIcon = customDialog.findViewById(R.id.cameraIcon);
        saveObservationBtn = customDialog.findViewById(R.id.saveObservationBtn);
        cancelBtn = customDialog.findViewById(R.id.cancelBtn);

        saveObservationBtn.setOnClickListener(v -> {
            validateObservationDetails();

        });

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        cameraIcon.setOnClickListener(v -> {
//            TODO: Open the camera application
        });

        dialog.setContentView(customDialog);
        dialog.setTitle(getString(R.string.add_observation2));
        dialog.show();
    }

    private void validateObservationDetails() {
        if (TextUtils.isEmpty(observationText.getText())) {
            observationText.requestFocus();
            observationTextOutline.setErrorEnabled(true);
            observationTextOutline.setError(getResources().getString(R.string.required));
            observationTextOutline.setBoxStrokeColor(Color.RED);
            return;
        }

        if (saveObservation()) {
            viewModel.readAllObservation(hike.getId());
            viewModel.getHikeObservationObserver().observe(this, hikeObservations -> {
                if (hikeObservations.size()  == 1) {
                    observationAdapter.setHikeObservationsArrayList(hikeObservations);
                }
                else if (observationAdapter.getItemCount() > 1){
                    observationAdapter.refresh();
                    viewModel.readAllObservation(hike.getId());
                    observationAdapter.setHikeObservationsArrayList(hikeObservations);
                }
            });
            dialog.dismiss();
        }
    }

    private boolean saveObservation() {
        String observation = observationText.getText().toString().trim();
        Date currentTime = Calendar.getInstance().getTime();
        String time = String.valueOf(currentTime);
        String comment = additionalComment.getText().toString().trim();
        String imageURL = "";

        if (image != null) {
            imageURL = uploadImage(image);
        }

        HikeObservation hikeObservation = new HikeObservation(hike.getId(), observation, time, comment, imageURL);
        return viewModel.storeHikeObservationToDatabase(this, hikeObservation);
    }

    //    This method is going to upload the image to a cloud service that stores images
//    The url of the image will then be stored in the local sqlite database
    private String uploadImage(Image image) {
        String imageURL = "";
        if (image != null) {
//            TODO:Upload image to a cloud service and then get the url to the image
        }
        return imageURL;
    }

    @Override
    public void onHikeObservationClick(HikeObservation hikeObservation) {

    }

    private void deleteHike(Activity activity, String id) {
        viewModel.deleteHike(activity, id);
    }

    private void confirmDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Delete " + hike.getHikeName());
        builder.setMessage("Are you sure you want to delete " + hike.getHikeName() + "?");
        builder.setBackground(getDrawable(R.drawable.dialog_background));
        builder.setPositiveButton("Yes", (dialogInterface, i) -> deleteHike(HikeDetails.this, String.valueOf(hike.getId())));
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

        });
        builder.create().show();
    }
}


















