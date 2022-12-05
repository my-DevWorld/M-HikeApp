package com.example.m_hikeapp.viewmodels;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.utils.Constants;
import com.example.m_hikeapp.views.DetailsConfirmation;

public class AddHikeViewModel extends AndroidViewModel {
    public AddHikeViewModel(@NonNull Application application) {
        super(application);
    }

    public void submitData(Activity activity, Hike hike) {
        Intent intent = new Intent(activity, DetailsConfirmation.class);
        intent.putExtra(Constants.HIKE_DETAILS, hike);
        activity.startActivity(intent);
        activity.finish();
    }

    public void submitData(Activity activity, Hike hike, String from) {
        Intent intent = new Intent(activity, DetailsConfirmation.class);
        intent.putExtra(Constants.HIKE_DETAIL_KEY_FROM, from);
        intent.putExtra(Constants.HIKE_DETAILS, hike);
        activity.startActivity(intent);
        activity.finish();
    }
}
