package com.example.m_hikeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Hike implements Parcelable {
    private int id;
    private String hikeName;
    private String location;
    private String date;
    private String distance;
    private String purposeOfHike;
    private String description;
    private String numberOfPersons;
    private String parkingAvailable;
    private String camping;
    private String imageURL;
    private String hikeDifficultyLevel;
    private int hikeSaved;

    public Hike(int id, String hikeName, String location, String date, String distance,
                String purposeOfHike, String description, String numberOfPersons,
                String parkingAvailable, String camping, String imageURL,
                String hikeDifficultyLevel, int hikeSaved) {
        this.id = id;
        this.hikeName = hikeName;
        this.location = location;
        this.date = date;
        this.distance = distance;
        this.purposeOfHike = purposeOfHike;
        this.description = description;
        this.numberOfPersons = numberOfPersons;
        this.parkingAvailable = parkingAvailable;
        this.camping = camping;
        this.imageURL = imageURL;
        this.hikeDifficultyLevel = hikeDifficultyLevel;
        this.hikeSaved = hikeSaved;
    }

    public Hike(String hikeName, String location, String date, String distance,
                String purposeOfHike, String description, String numberOfPersons,
                String parkingAvailable, String camping, String imageURL,
                String hikeDifficultyLevel, int hikeSaved) {
        this.hikeName = hikeName;
        this.location = location;
        this.date = date;
        this.distance = distance;
        this.purposeOfHike = purposeOfHike;
        this.description = description;
        this.numberOfPersons = numberOfPersons;
        this.parkingAvailable = parkingAvailable;
        this.camping = camping;
        this.imageURL = imageURL;
        this.hikeDifficultyLevel = hikeDifficultyLevel;
        this.hikeSaved = hikeSaved;
    }

    protected Hike(Parcel in) {
        id = in.readInt();
        hikeName = in.readString();
        location = in.readString();
        date = in.readString();
        distance = in.readString();
        purposeOfHike = in.readString();
        description = in.readString();
        numberOfPersons = in.readString();
        parkingAvailable = in.readString();
        camping = in.readString();
        imageURL = in.readString();
        hikeDifficultyLevel = in.readString();
        hikeSaved = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(hikeName);
        dest.writeString(location);
        dest.writeString(date);
        dest.writeString(distance);
        dest.writeString(purposeOfHike);
        dest.writeString(description);
        dest.writeString(numberOfPersons);
        dest.writeString(parkingAvailable);
        dest.writeString(camping);
        dest.writeString(imageURL);
        dest.writeString(hikeDifficultyLevel);
        dest.writeInt(hikeSaved);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Hike> CREATOR = new Creator<Hike>() {
        @Override
        public Hike createFromParcel(Parcel in) {
            return new Hike(in);
        }

        @Override
        public Hike[] newArray(int size) {
            return new Hike[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHikeName() {
        return hikeName;
    }

    public void setHikeName(String hikeName) {
        this.hikeName = hikeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPurposeOfHike() {
        return purposeOfHike;
    }

    public void setPurposeOfHike(String purposeOfHike) {
        this.purposeOfHike = purposeOfHike;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(String numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getParkingAvailable() {
        return parkingAvailable;
    }

    public void setParkingAvailable(String parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public String getCamping() {
        return camping;
    }

    public void setCamping(String camping) {
        this.camping = camping;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getHikeDifficultyLevel() {
        return hikeDifficultyLevel;
    }

    public void setHikeDifficultyLevel(String hikeDifficultyLevel) {
        this.hikeDifficultyLevel = hikeDifficultyLevel;
    }

    public int getHikeSaved() {
        return hikeSaved;
    }

    public void setHikeSaved(int hikeSaved) {
        this.hikeSaved = hikeSaved;
    }
}
