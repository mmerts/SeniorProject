package com.example.leadtheway;


public class Museum {
    private String title;
    private String expectedTime;
    private String OpenClosedTime;
    private String imageUrl;
    private int id;
    private String longitude;
    private String latitude;


    public Museum(){}
    public Museum(int id,String title, String expectedTime, String OpenClosedTime, String imageUrl,String longitude,String latitude) {
        this.id = id;
        this.setTitle(title);
        this.setExpectedTime(expectedTime);
        this.setOpenClosedTime(OpenClosedTime);
        this.setImageUrl(imageUrl);
        this.longitude = longitude;
        this.latitude = latitude;
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
}
