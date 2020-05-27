package com.example.leadtheway;

public class UserChoice {
 private int UserId;
 private String userCityChoice;
 private String userCountryChoice;
 private String userDateChoice;

    public UserChoice(){}

    public UserChoice(int userId,String userCityChoice, String userCountryChoice, String userDateChoice) {
        this.UserId = userId;
        this.userCityChoice = userCityChoice;
        this.userCountryChoice = userCountryChoice;
        this.userDateChoice = userDateChoice;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserCityChoice() {
        return userCityChoice;
    }

    public void setUserCityChoice(String userCityChoice) {
        this.userCityChoice = userCityChoice;
    }

    public String getUserCountryChoice() {
        return userCountryChoice;
    }

    public void setUserCountryChoice(String userCountryChoice) {
        this.userCountryChoice = userCountryChoice;
    }

    public String getUserDateChoice() {
        return userDateChoice;
    }

    public void setUserDateChoice(String userDateChoice) {
        this.userDateChoice = userDateChoice;
    }

}
