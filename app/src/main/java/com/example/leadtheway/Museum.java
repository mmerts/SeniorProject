package com.example.leadtheway;


import android.os.Parcel;
import android.os.Parcelable;

public class Museum implements Parcelable {
    //get all musueum information as an object.
    private int id;
    private String title;
    private String expectedTime;
    private String OpenClosedTime;
    private String imageUrl;
    private String longitude;
    private String latitude;
    private String timeSchedule;
    private String description;
    private String RestaurantName;
    private String Rank;
    private String Rating;
    private String Fee;
    private String Restaurantlatitude;
    private String Restaurantlongitude;


    public Museum(){}
    public Museum(int id,String title, String expectedTime, String OpenClosedTime, String imageUrl,String longitude,String latitude,String description,String Rname,String Rlat,String Rlong,String Rank,String Rating,String Fee) {
        this.id = id;
        this.setTitle(title);
        this.setExpectedTime(expectedTime);
        this.setOpenClosedTime(OpenClosedTime);
        this.setImageUrl(imageUrl);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setDescription(description);
        this.setRestaurantName(Rname);
        this.setRestaurantlongitude(Rlong);
        this.setRestaurantlatitude(Rlat);
        this.setRank(Rank);
        this.setRating(Rating);
        this.setFee(Fee);
    }

    protected Museum(Parcel in) {
        title = in.readString();
        expectedTime = in.readString();
        OpenClosedTime = in.readString();
        imageUrl = in.readString();
        id = in.readInt();
        longitude = in.readString();
        latitude = in.readString();
        timeSchedule = in.readString();
        description = in.readString();
    }
    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getFee() {
        return Fee;
    }

    public void setFee(String fee) {
        Fee = fee;
    }


    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public String getRestaurantlatitude() {
        return Restaurantlatitude;
    }

    public void setRestaurantlatitude(String restaurantlatitude) {
        Restaurantlatitude = restaurantlatitude;
    }

    public String getRestaurantlongitude() {
        return Restaurantlongitude;
    }

    public void setRestaurantlongitude(String restaurantlongitude) {
        Restaurantlongitude = restaurantlongitude;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeSchedule() {
        return timeSchedule;
    }

    public void setTimeSchedule(String timeSchedule) {
        this.timeSchedule = timeSchedule;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    public String getOpenClosedTime() {
        return OpenClosedTime;
    }

    public void setOpenClosedTime(String openClosedTime) {
        OpenClosedTime = openClosedTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

    parcel.writeString(title);
    parcel.writeString(description);

    }
    public static final Creator<Museum> CREATOR = new Creator<Museum>() {
        @Override
        public Museum createFromParcel(Parcel parcel) {
            return new Museum(parcel);
        }

        @Override
        public Museum[] newArray(int size) {
            return new Museum[size];
        }
    };
}
