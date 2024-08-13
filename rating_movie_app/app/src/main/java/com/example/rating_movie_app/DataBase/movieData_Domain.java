package com.example.rating_movie_app.DataBase;

import com.example.rating_movie_app.rateFilms_Adapter.recycleDomain;

public class movieData_Domain extends recycleDomain {

    private int mType;
    private boolean isReview;

    // Конструктор дочернего класса
    public movieData_Domain(int id, String title, String genres, int year, String dateRate, double rating, int mType, boolean isReview) {
        // Вызов конструктора родительского класса
        super(id, title, genres, year, dateRate, rating);
        // Инициализация новых полей
        this.mType = mType;
        this.isReview = isReview;
    }

    // Геттеры и сеттеры для новых полей
    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }
}
