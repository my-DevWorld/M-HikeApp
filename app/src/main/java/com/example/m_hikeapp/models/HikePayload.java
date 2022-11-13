package com.example.m_hikeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class HikePayload implements Parcelable {
    private ArrayList<Hike> userArrayList;
    private int total;

    public HikePayload(ArrayList<Hike> userArrayList, int total) {
        this.userArrayList = userArrayList;
        this.total = total;
    }

    protected HikePayload(Parcel in) {
        userArrayList = in.createTypedArrayList(Hike.CREATOR);
        total = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(userArrayList);
        dest.writeInt(total);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HikePayload> CREATOR = new Creator<HikePayload>() {
        @Override
        public HikePayload createFromParcel(Parcel in) {
            return new HikePayload(in);
        }

        @Override
        public HikePayload[] newArray(int size) {
            return new HikePayload[size];
        }
    };

    public ArrayList<Hike> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<Hike> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
