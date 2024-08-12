package com.example.rating_movie_app.rateFilms_Adapter;

public class recycleDomain {

    private String Title;
    private String Genres;
    private int Year;
    private String DateRate;
    private double Rating;


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getGenres() {
        return Genres;
    }

    public void setGenres(String genres) {
        Genres = genres;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getDateRate() {
        return DateRate;
    }

    public void setDateRate(String dateRate) {
        DateRate = dateRate;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }
}
