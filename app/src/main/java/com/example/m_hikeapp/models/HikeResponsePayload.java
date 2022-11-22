package com.example.m_hikeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HikeResponsePayload  implements Parcelable {
    @SerializedName("uploadResponseCode")
    private String uploadStatus;
    @SerializedName("userId")
    private String userID;
    @SerializedName("number")
    private int number;
    @SerializedName("names")
    private String names;
    @SerializedName("message")
    private String status;

    protected HikeResponsePayload(Parcel in) {
        uploadStatus = in.readString();
        userID = in.readString();
        number = in.readInt();
        names = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uploadStatus);
        dest.writeString(userID);
        dest.writeInt(number);
        dest.writeString(names);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HikeResponsePayload> CREATOR = new Creator<HikeResponsePayload>() {
        @Override
        public HikeResponsePayload createFromParcel(Parcel in) {
            return new HikeResponsePayload(in);
        }

        @Override
        public HikeResponsePayload[] newArray(int size) {
            return new HikeResponsePayload[size];
        }
    };

    public String getUploadStatus() {
        return uploadStatus;
    }

    public String getUserID() {
        return userID;
    }

    public int getNumber() {
        return number;
    }

    public String getNames() {
        return names;
    }

    public String getStatus() {
        return status;
    }
}
