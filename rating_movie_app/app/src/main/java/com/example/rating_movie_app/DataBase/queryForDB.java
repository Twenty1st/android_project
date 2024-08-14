package com.example.rating_movie_app.DataBase;

public class queryForDB {
    private static String full_query = "SELECT movie_id, movie_name, movie_year, type_name, movie_dateRate, movie_rating, " +
            "GROUP_CONCAT(genre_name, ', ') AS movie_genres  " +
            "FROM movies JOIN mtypes ON type_id = movie_type " +
            "JOIN movie_genres ON mg_movie_id = movie_id " +
            "JOIN genres ON genre_id = mg_genre_id " +
            "GROUP BY movies.movie_id " +
            "ORDER BY movies.movie_id DESC";

    private static String query_forGetById = "SELECT movie_id, movie_name, movie_year, type_name, movie_review, " +
            "GROUP_CONCAT(genre_name, ', ') AS movie_genres  " +
            "FROM movies JOIN mtypes ON type_id = movie_type " +
            "JOIN movie_genres ON mg_movie_id = movie_id " +
            "JOIN genres ON genre_id = mg_genre_id " +
           // "GROUP BY movies.movie_id " +
            "WHERE movie_id = ?";
    private String full_query2 = "";

    public static String getFull_query() {
        return full_query;
    }

    public static String getQuery_forGetById() {
        return query_forGetById;
    }
}
