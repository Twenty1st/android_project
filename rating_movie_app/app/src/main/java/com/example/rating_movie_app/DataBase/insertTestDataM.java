package com.example.rating_movie_app.DataBase;

import android.content.Context;
import android.util.Log;

import java.util.Random;

public class insertTestDataM {

    private callDBMethods dbHelper;

    public void insertTestData(Context context) {
        dbHelper = new callDBMethods(context);
        for (int i = 1; i <= 1000; i++) {
            // Генерация случайных данных для каждого фильма
            String movieName = "Movie " + i;
            int movieYear = getRandomNumber(1980, 2024);
            int movieType = getRandomNumber(1, 2); // Предположим, что у вас 4 типа фильмов
            boolean movieReview = getRandomBoolean();
            String movieGenres = "Комедия, Драма, Дорама, Калл";
            double movieRating = getRandomDouble(1.0, 10.0);
            String movieDateRate = generateRandomDate();

            // Создаем объект movieData_Domain с сгенерированными данными
            movieData_Domain movieData = new movieData_Domain(i, movieName, movieGenres, movieYear, movieDateRate, movieRating, movieType, movieReview);

            // Генерация случайных оценок для параметров фильма
            tableEvalsDB.Eval evalsData = new tableEvalsDB.Eval(
                    getRandomNumber(1, 10), // estimationStory
                    getRandomNumber(1, 10), // estimationGrath
                    getRandomNumber(1, 10), // estimationActor
                    getRandomNumber(1, 10), // estimationShoot
                    getRandomNumber(1, 10), // estimationFeels
                    getRandomNumber(1, 10), // estimationLogik
                    getRandomNumber(1, 10), // estimationOrig
                    getRandomNumber(1, 10), // estimationThink
                    getRandomNumber(1, 10)  // estimationGripp
            );

            // Вставляем данные в базу
            boolean isOK = dbHelper.insertData(movieData, evalsData);
            if (isOK) {
                Log.d("TestData", "Record " + i + " inserted successfully.");
            } else {
                Log.e("TestData", "Error inserting record " + i);
            }
        }
    }

    // Метод для генерации случайного числа в заданном диапазоне
    private int getRandomNumber(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    // Метод для генерации случайного числа с плавающей запятой в заданном диапазоне
    private double getRandomDouble(double min, double max) {
        return min + (max - min) * new Random().nextDouble();
    }

    // Метод для генерации случайной даты (пример форматирования "dd.MM.yyyy")
    private String generateRandomDate() {
        int day = getRandomNumber(1, 28);
        int month = getRandomNumber(1, 12);
        int year = getRandomNumber(2000, 2024);
        return String.format("%02d.%02d.%d", day, month, year);
    }

    // Метод для генерации случайного boolean значения
    private boolean getRandomBoolean() {
        return new Random().nextBoolean();
    }
}
