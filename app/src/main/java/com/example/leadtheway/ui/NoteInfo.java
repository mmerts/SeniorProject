package com.example.leadtheway.ui;

import android.os.Parcel;
import android.os.Parcelable;

/*
simple note class with getter and setter in order to keep as an object.
 */

public final class NoteInfo implements Parcelable {
    private PlaceInfo mPlace;
    private String mTitle;
    private String mText;

    public NoteInfo(PlaceInfo place, String title, String text) {
        mPlace = place;
        mTitle = title;
        mText = text;
    }

    private NoteInfo(Parcel parcel) {
        mPlace = parcel.readParcelable(PlaceInfo.class.getClassLoader());
        mTitle = parcel.readString();
        mText = parcel.readString();
    }

    public PlaceInfo getPlace() {
        return mPlace;
    }

    public void setPlace(PlaceInfo place) {
        mPlace = place;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    private String getCompareKey() {
        return mPlace.getPlaceId() + "|" + mTitle + "|" + mText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteInfo that = (NoteInfo) o;

        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    @Override
    public String toString() {
        return getCompareKey();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(mPlace,0);
        parcel.writeString(mTitle);
        parcel.writeString(mText);
    }
    public static final Parcelable.Creator<NoteInfo> CREATOR = new Parcelable.Creator<NoteInfo>(){
        @Override
        public NoteInfo createFromParcel(Parcel parcel) {
            return new NoteInfo(parcel);
        }

        @Override
        public NoteInfo[] newArray(int size) {
            return new NoteInfo[size];
        }
    };
}
