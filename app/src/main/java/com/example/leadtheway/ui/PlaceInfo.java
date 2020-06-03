package com.example.leadtheway.ui;

import android.os.Parcel;
import android.os.Parcelable;

public final class PlaceInfo implements Parcelable {
    private final String mPlaceId;
    private final String mTitle;


    public PlaceInfo(String PlaceId, String title) {
        mPlaceId = PlaceId;
        mTitle = title;
    }

    private PlaceInfo(Parcel source) {
        mPlaceId = source.readString();
        mTitle = source.readString();
    }
    public static final Creator<PlaceInfo> CREATOR =
            new Creator<PlaceInfo>() {
        @Override public PlaceInfo createFromParcel(Parcel source) {
                    return new PlaceInfo(source); }


                @Override public PlaceInfo[] newArray(int size) {
                    return new PlaceInfo[size];
                }
            };
    public String getPlaceId() {
        return mPlaceId;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceInfo that = (PlaceInfo) o;

        return mPlaceId.equals(that.mPlaceId);

    }
    @Override
    public int hashCode() {
        return mPlaceId.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPlaceId);
        dest.writeString(mTitle);
    }

}
