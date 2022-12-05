package com.example.m_hikeapp.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
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

    private ImageView save, hikeSaved, share, hike_image1, delete, previousPage;
    private TextView hikeName1, location1, hikeDate1, hikeDistance1, numbOfPersons1, camping1,
            parking1, addObservation, hikeDescription;
    private TextView hikeDetails, cancelBtn, LevelLabel;
    private TextInputLayout observationTextOutline;
    private TextInputEditText observationText, additionalComment;
    private ImageView cameraIcon;
    private Button saveObservationBtn;
    private Bitmap image = null;
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
        save = findViewById(R.id.fav1);
        hike_image1 = findViewById(R.id.hike_image1);
        delete = findViewById(R.id.delete);
        previousPage = findViewById(R.id.previousPage);
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
        LevelLabel = findViewById(R.id.LevelLabel);

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
            deleteHikeDialog();
        });

        previousPage.setOnClickListener(v -> {onBackPressed();});
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

            if (hike.getHikeDifficultyLevel().equals(Constants.DIFFICULTY_LEVEL3)){
                LevelLabel.setText(Constants.DIFFICULTY_LEVEL3);
                LevelLabel.setTextColor(getColor(R.color.high_level));
            }else if (hike.getHikeDifficultyLevel().equals(Constants.DIFFICULTY_LEVEL2)){
                LevelLabel.setText(Constants.DIFFICULTY_LEVEL2);
                LevelLabel.setTextColor(getColor(R.color.medium_level));
            }else {
                LevelLabel.setText(Constants.DIFFICULTY_LEVEL1);
                LevelLabel.setTextColor(getColor(R.color.low_level));
            }

            if (hike.getHikeSaved() == 1){
                save.setImageDrawable(getDrawable(R.drawable.ic_hike_saved));
            }

            save.setOnClickListener(v -> {
                if (hike.getHikeSaved() == 0){
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(getString(R.string.add));
                    strings.add(getString(R.string.favourite1));
                    strings.add(getString(R.string.add_hike_message));
                    strings.add(getString(R.string.yes));
                    strings.add(getString(R.string.cancel));
                    hike.setHikeSaved(1);
                    favouriteHike(this, hike, strings);
                }else {
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(getString(R.string.remove_hike_from_favourite));
                    strings.add(getString(R.string.favourite2));
                    strings.add(getString(R.string.remove_hike_message));
                    strings.add(getString(R.string.yes));
                    strings.add(getString(R.string.cancel));
                    hike.setHikeSaved(0);
                    favouriteHike(this, hike, strings);
                }
            });
        }
    }

//    Add Observation to database dialog
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
            checkPermissionAndOpenCamera();
        });

        dialog.setContentView(customDialog);
        dialog.setTitle(getString(R.string.add_observation2));
        dialog.show();
    }

//    Open camera app
    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST);
    }

//    Validate observation input
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

//    Save observation to database
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
    private String uploadImage(Bitmap image) {
        String imageURL = "";
        if (image != null) {
//            TODO:Upload image to a cloud service and then get the url to the image
        }
        return imageURL;
    }

    @Override
    public void onHikeObservationClick(HikeObservation hikeObservation) {

    }

//    Delete hike from database
    private void deleteHike(Activity activity, String id) {
        viewModel.deleteHike(activity, id);
    }

//    Delete hike dialog
    private void deleteHikeDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(getString(R.string.delete) + hike.getHikeName());
        builder.setMessage(getString(R.string.delete_hike_message) + hike.getHikeName() + "?");
        builder.setBackground(getDrawable(R.drawable.dialog_background));
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            deleteHike(HikeDetails.this, String.valueOf(hike.getId()));
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {

        });
        builder.create().show();
    }

//    Add hike to  favourite list dialog
    private void favouriteHike(Activity activity, Hike hike, ArrayList<String> strings){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(String.format("%s%s%s", strings.get(0), hike.getHikeName().concat(" "), strings.get(1)));
        builder.setMessage(strings.get(2));
        builder.setBackground(getDrawable(R.drawable.dialog_background));
        builder.setPositiveButton(strings.get(3), ((dialogInterface, i) -> {
            viewModel.addToFavourite(activity, hike);
        }));
        builder.setNegativeButton(strings.get(4), (dialogInterface, i) -> {

        });
        builder.create().show();
    }

// Result for camera intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST) {
            if (data != null){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                uploadImage(photo);
            }
        }
    }

//    Check camera permission
    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, Constants.CHECK_CAMERA_REQUEST);
        } else {
            openCamera();
        }
    }

//    Get result of camera permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.CHECK_CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
        finish();
        super.onBackPressed();
    }
}


















