package com.example.m_hikeapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_hikeapp.R;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.utils.Constants;
import com.example.m_hikeapp.utils.UISupport;
import com.example.m_hikeapp.viewmodels.AddHikeViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class AddHike extends AppCompatActivity {

    private AddHikeViewModel viewModel;

    private TextInputLayout hikeNameOutline, locationOutline, dateOutline, distanceOutline, hikePurposeOutline;
    private TextInputEditText hikeName, location, date, distance, description, numbOfPersons;
    private TextView difficultLevelLabel;
    private AutoCompleteTextView hikePurpose;
    private Button submitBtn;
    private RadioGroup campingRadioGroup, parkingAvalRadioGroup;
    private RadioButton isCamping, parkingAvalSelectedRadioButton, parkingAvalYes, parkingAvalNo, campingNo, campingYes;
    private ImageView close;
    private View levelIndicator1, levelIndicator2, levelIndicator3;

    private MaterialDatePicker materialDatePicker;
    private CalendarConstraints.Builder calendarConstraints;

    private String purposeOfHike, from, difficultyLevel;
    private Hike hike;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

//        Initialising viewModel
        viewModel = new ViewModelProvider(this).get(AddHikeViewModel.class);

//        Get Data back from confirmation activity
        intent = getIntent();
        String editDetails = intent.getStringExtra(Constants.HIKE_DETAIL_KEY);
        from = intent.getStringExtra(Constants.HIKE_DETAIL_KEY_FROM);

        init();

//        Setting data from confirmation activity to view elements
        if (editDetails != null) {
            hike = intent.getParcelableExtra(Constants.HIKE_DETAILS);
            hikeName.setText(hike.getHikeName());
            location.setText(hike.getLocation());
            date.setText(hike.getDate());
            distance.setText(hike.getDistance());
            hikePurpose.setText(hike.getPurposeOfHike());
            description.setText(hike.getDescription());
            numbOfPersons.setText(hike.getNumberOfPersons());
            if (parkingAvalYes.getText().toString().equals(hike.getParkingAvailable())) {
                parkingAvalYes.setChecked(true);
            } else {
                parkingAvalNo.setChecked(true);
            }

            if (campingYes.getText().toString().equals(hike.getCamping())) {
                campingYes.setChecked(true);
            } else if (campingNo.getText().toString().equals(hike.getCamping())) {
                campingNo.setChecked(true);
            }
        }
    }

    //        Initialise view objects
    private void init() {

        close = findViewById(R.id.close);
        hikeNameOutline = findViewById(R.id.hikeNameOutline);
        locationOutline = findViewById(R.id.locationOutline);
        dateOutline = findViewById(R.id.dateOutline);
        distanceOutline = findViewById(R.id.distanceOutline);
        hikePurposeOutline = findViewById(R.id.hikePurposeOutline);
        campingRadioGroup = findViewById(R.id.campingRadioGroup);
        parkingAvalRadioGroup = findViewById(R.id.parkingAvalRadioGroup);
        parkingAvalYes = findViewById(R.id.parkingAvalYes);
        parkingAvalNo = findViewById(R.id.parkingAvalNo);
        campingNo = findViewById(R.id.campingNo);
        campingYes = findViewById(R.id.campingYes);
        difficultLevelLabel = findViewById(R.id.difficultLevelLabel);
        levelIndicator1 = findViewById(R.id.levelIndicator1);
        levelIndicator2 = findViewById(R.id.levelIndicator2);
        levelIndicator3 = findViewById(R.id.levelIndicator3);

        hikeName = findViewById(R.id.hikeName);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        distance = findViewById(R.id.distance);
        hikePurpose = findViewById(R.id.hikePurpose);
        description = findViewById(R.id.description);
        numbOfPersons = findViewById(R.id.numbOfPersons);
        submitBtn = findViewById(R.id.submitBtn);

        hikeName.addTextChangedListener(hikeNameEditTextWatcher);
        location.addTextChangedListener(locationEditTextWatcher);
        date.addTextChangedListener(dateEditTextWatcher);
        distance.addTextChangedListener(distanceEditTextWatcher);
        hikePurpose.addTextChangedListener(hikePurposeEditTextWatcher);

        date.setInputType(InputType.TYPE_NULL);
        locationOutline.setEndIconOnClickListener(v -> {
            checkLocationPermission();
        });

        ArrayList<String> list = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.purpose_of_hike)));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, list);
        hikePurpose.setAdapter(arrayAdapter);


        datePicker();
        onClickEvents();
    }

    //        Method for onClick events.
    public void onClickEvents() {
        close.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });

        date.setOnClickListener(v -> {
            UISupport.hideSoftKeyboard(this, date);
            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        date.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                UISupport.hideSoftKeyboard(this, date);
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        hikePurpose.setOnItemClickListener((adapterView, view, i, l) -> purposeOfHike = adapterView.getItemAtPosition(i).toString());

        submitBtn.setOnClickListener(v -> {
            formValidation();
        });
    }

    private void formValidation() {
        String camping;
        if (TextUtils.isEmpty(hikeName.getText())) {
            hikeName.requestFocus();
            hikeNameOutline.setErrorEnabled(true);
            hikeNameOutline.setError(getResources().getString(R.string.required));
            hikeNameOutline.setBoxStrokeColor(Color.RED);
        }

        if (TextUtils.isEmpty(location.getText())) {
            location.requestFocus();
            locationOutline.setErrorEnabled(true);
            locationOutline.setError(getResources().getString(R.string.required));
            locationOutline.setBoxStrokeColor(Color.RED);
        }

        if (TextUtils.isEmpty(date.getText())) {
            dateOutline.setErrorEnabled(true);
            dateOutline.setError(getResources().getString(R.string.required));
            dateOutline.setBoxStrokeColor(Color.RED);
        }

        if (TextUtils.isEmpty(distance.getText())) {
            distance.requestFocus();
            distanceOutline.setErrorEnabled(true);
            distanceOutline.setError(getResources().getString(R.string.required));
            distanceOutline.setBoxStrokeColor(Color.RED);
        }

        if (TextUtils.isEmpty(hikePurpose.getText())) {
            hikePurpose.requestFocus();
            hikePurposeOutline.setErrorEnabled(true);
            hikePurposeOutline.setError(getResources().getString(R.string.required));
            hikePurposeOutline.setBoxStrokeColor(Color.RED);
        }

        if (parkingAvalRadioGroup.getCheckedRadioButtonId() == -1) {
            Snackbar.make(this, parkingAvalRadioGroup, getString(R.string.parking_availability), Snackbar.LENGTH_SHORT).show();
            Constants.IS_FORM_VALID = false;
        } else {
            // get selected radio button from radioGroup
            int selectedId = parkingAvalRadioGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            parkingAvalSelectedRadioButton = findViewById(selectedId);
            Constants.IS_FORM_VALID = true;
        }

        if (campingRadioGroup.getCheckedRadioButtonId() == -1) {
            int selectedId = campingRadioGroup.getCheckedRadioButtonId();
            isCamping = findViewById(selectedId);
            camping = "";
        } else {
            // get selected radio button from radioGroup
            int selectedId = campingRadioGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            isCamping = findViewById(selectedId);
            camping = isCamping.getText().toString();
        }

        if (!TextUtils.isEmpty(hikeName.getText()) && !TextUtils.isEmpty(location.getText())
                && !TextUtils.isEmpty(date.getText()) && !TextUtils.isEmpty(distance.getText())
                && !TextUtils.isEmpty(hikePurpose.getText()) && Constants.IS_FORM_VALID) {

            if (Integer.valueOf(distance.getText().toString().trim()) <= Constants.LOW_DIFFICULTY_LEVEL){
                difficultyLevel = Constants.DIFFICULTY_LEVEL1;
//                levelIndicator1.setBackgroundColor(getColor(R.color.low_level));
            }
            else if (Integer.valueOf(distance.getText().toString().trim()) <= Constants.MEDIUM_DIFFICULTY_LEVEL){
                difficultyLevel = Constants.DIFFICULTY_LEVEL2;
//                levelIndicator1.setBackgroundColor(getColor(R.color.low_level));
//                levelIndicator2.setBackgroundColor(getColor(R.color.medium_level));
            }
            else {
                difficultyLevel = Constants.DIFFICULTY_LEVEL3;
//                levelIndicator1.setBackgroundColor(getColor(R.color.low_level));
//                levelIndicator2.setBackgroundColor(getColor(R.color.medium_level));
//                levelIndicator3.setBackgroundColor(getColor(R.color.high_level));
            }

            hike = new Hike(hikeName.getText().toString(), location.getText().toString(),
                    date.getText().toString(), distance.getText().toString(),
                    hikePurpose.getText().toString(), description.getText().toString(),
                    numbOfPersons.getText().toString(),
                    parkingAvalSelectedRadioButton.getText().toString(), camping, Constants.HIKE_THUMBNAIL1, difficultyLevel, 0);

            if (from != null){
                hike = intent.getParcelableExtra(Constants.HIKE_DETAILS);
                int id = Integer.valueOf(hike.getId());
                String name = hikeName.getText().toString().trim();
                String hikeLocation = location.getText().toString().trim();
                String hikeDate = date.getText().toString().trim();
                String hikeDistance = distance.getText().toString().trim();
                String purpose = hikePurpose.getText().toString().trim();
                String hikeDescription = description.getText().toString().trim();
                String hikeNumbOfPersons = numbOfPersons.getText().toString().trim();
                String hikeParking = parkingAvalSelectedRadioButton.getText().toString().trim();
                String hikeDifficultyLevel = difficultyLevel;

                hike = new Hike(id, name, hikeLocation, hikeDate, hikeDistance,
                        purpose, hikeDescription, hikeNumbOfPersons, hikeParking, camping,
                        null, hikeDifficultyLevel, hike.getHikeSaved());

                viewModel.submitData(this, hike, from);
            }else {
                viewModel.submitData(this, hike);
            }
        }
    }

    private final TextWatcher hikeNameEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                hikeNameOutline.setErrorEnabled(false);
                hikeNameOutline.setBoxStrokeColor(getResources().getColor(R.color.teal_200, getTheme()));
            }
        }
    };

    private final TextWatcher locationEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                locationOutline.setErrorEnabled(false);
                locationOutline.setBoxStrokeColor(getResources().getColor(R.color.teal_200, getTheme()));
            }
        }
    };

    private final TextWatcher dateEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                dateOutline.setErrorEnabled(false);
                dateOutline.setBoxStrokeColor(getResources().getColor(R.color.teal_200, getTheme()));
            }
        }
    };

    private final TextWatcher distanceEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count < 1){
                levelIndicator1.setBackgroundColor(getColor(R.color.brownishGreyTwo));
                levelIndicator2.setBackgroundColor(getColor(R.color.brownishGreyTwo));
                levelIndicator3.setBackgroundColor(getColor(R.color.brownishGreyTwo));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                distanceOutline.setErrorEnabled(false);
                distanceOutline.setBoxStrokeColor(getResources().getColor(R.color.teal_200, getTheme()));
                if (Integer.valueOf(distance.getText().toString().trim()) <= Constants.LOW_DIFFICULTY_LEVEL){
                    difficultyLevel = Constants.DIFFICULTY_LEVEL1;
                    levelIndicator1.setBackgroundColor(getColor(R.color.low_level));
                }
                else if (Integer.valueOf(distance.getText().toString().trim()) <= Constants.MEDIUM_DIFFICULTY_LEVEL){
                    difficultyLevel = Constants.DIFFICULTY_LEVEL2;
                    levelIndicator1.setBackgroundColor(getColor(R.color.low_level));
                    levelIndicator2.setBackgroundColor(getColor(R.color.medium_level));
                }
                else {
                    difficultyLevel = Constants.DIFFICULTY_LEVEL3;
                    levelIndicator1.setBackgroundColor(getColor(R.color.low_level));
                    levelIndicator2.setBackgroundColor(getColor(R.color.medium_level));
                    levelIndicator3.setBackgroundColor(getColor(R.color.high_level));
                }
            }
            else {
                levelIndicator1.setBackgroundColor(getColor(R.color.brownishGreyTwo));
                levelIndicator2.setBackgroundColor(getColor(R.color.brownishGreyTwo));
                levelIndicator3.setBackgroundColor(getColor(R.color.brownishGreyTwo));
            }
        }
    };

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Constants.CHECK_LOCATION_REQUEST);
        } else {
            String uri = "http://maps.google.com/maps";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
//              TODO: OPEN GOOGLE MAPS
        }
    }

//    Get result of Location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.CHECK_LOCATION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//              TODO: OPEN GOOGLE MAPS
//                String uri = "http://maps.google.com/maps";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                startActivity(intent);
                getSupportFragmentManager().beginTransaction().add(R.id.container, new MapsFragment()).commit();
            }
        }
    }


    private final TextWatcher hikePurposeEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                hikePurposeOutline.setErrorEnabled(false);
                hikePurposeOutline.setBoxStrokeColor(getResources().getColor(R.color.teal_200, getTheme()));
            }
        }
    };

    private void datePicker() {
//        Set constraints on the date picker to makes only dates from today forward selectable
        calendarConstraints = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now());

//        Build date picker with material date builder
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(calendarConstraints.build());
        materialDateBuilder.setTitleText("SELECT A DATE");

//        instantiate material date picker with date builder
        materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {

//                   if the user clicks on the positive button that is ok button update the selected date
            date.setText(materialDatePicker.getHeaderText());
            // in the above statement, getHeaderText is the selected date preview from the dialog
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Home.class));
        finish();
    }
}


















