package com.example.m_hikeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HikePayload implements Parcelable {
    @SerializedName("userId")
    private String userID;
    @SerializedName("detailList")
    private ArrayList<Hike> userArrayList;
//    private int total;

    public HikePayload(String userID, ArrayList<Hike> userArrayList, int total) {
//        this.userID = userID;
        this.userArrayList = userArrayList;
//        this.total = total;
    }

    public HikePayload(String userID, ArrayList<Hike> userArrayList) {
//        this.userID = userID;
        this.userArrayList = userArrayList;
    }

    protected HikePayload(Parcel in) {
//        userID = in.readString();
        userArrayList = in.createTypedArrayList(Hike.CREATOR);
//        total = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(userID);
        dest.writeTypedList(userArrayList);
//        dest.writeInt(total);
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

//    public String getUserID() {
//        return userID;
//    }

    public ArrayList<Hike> getUserArrayList() {
        return userArrayList;
    }

//    public int getTotal() {
//        return total;
//    }
}
