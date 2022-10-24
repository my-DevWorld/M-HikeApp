package com.example.m_hikeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class HikeObservation implements Parcelable {
    private int id;
    private String observation;
    private String time;
    private String additionalComment;

    public HikeObservation(int id, String observation, String time, String additionalComment) {
        this.id = id;
        this.observation = observation;
        this.time = time;
        this.additionalComment = additionalComment;
    }

    protected HikeObservation(Parcel in) {
        id = in.readInt();
        observation = in.readString();
        time = in.readString();
        additionalComment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(observation);
        dest.writeString(time);
        dest.writeString(additionalComment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HikeObservation> CREATOR = new Creator<HikeObservation>() {
        @Override
        public HikeObservation createFromParcel(Parcel in) {
            return new HikeObservation(in);
        }

        @Override
        public HikeObservation[] newArray(int size) {
            return new HikeObservation[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdditionalComment() {
        return additionalComment;
    }

    public void setAdditionalComment(String additionalComment) {
        this.additionalComment = additionalComment;
    }
}
