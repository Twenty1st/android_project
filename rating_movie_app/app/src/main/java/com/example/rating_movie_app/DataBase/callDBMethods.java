package com.example.rating_movie_app.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rating_movie_app.rateFilms_Adapter.recycleDomain;

import java.util.ArrayList;

public class callDBMethods {

    //SELECT movie_name, movie_year, type_name, movie_dateRate, movie_rating,
    // GROUP_CONCAT(genre_name ORDER BY genre_name SEPARATOR ', ') AS movie_genres
    // FROM movies JOIN mtypes ON type_id = movie_type
    // JOIN movie_genres ON mg_movie_id = movie_id
    //JOIN genres ON genre_id = mg_genre_id;
    private static databaseManager dbManager;
    private static SQLiteDatabase db;

    public callDBMethods(Context context) {
        dbManager = new databaseManager(context);
        dbManager.open();
        db = dbManager.getDatabase();
    }


    public ArrayList<recycleDomain> selectFromDB(String query) {
        ArrayList<recycleDomain> movies = new ArrayList<>();
        tableMoviesDB dbMovies = new tableMoviesDB(db);
        Cursor cursor = dbMovies.executeCustomQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int movieId = cursor.getInt(cursor.getColumnIndex("movie_id"));
                String movieName = cursor.getString(cursor.getColumnIndex("movie_name"));
                int movieYear = cursor.getInt(cursor.getColumnIndex("movie_year"));
                String movieType = cursor.getString(cursor.getColumnIndex("type_name"));
                String movieGenres = cursor.getString(cursor.getColumnIndex("movie_genres"));
                String movieDateRate = cursor.getString(cursor.getColumnIndex("movie_dateRate"));
                double movieRating = cursor.getDouble(cursor.getColumnIndex("movie_rating"));

                movies.add(new recycleDomain(movieId, movieName, movieGenres, movieYear, movieDateRate, movieRating));
            } while (cursor.moveToNext());

            cursor.close();
        }
        return movies;
    }

    public int getCountFromTable(String query) {
        tableMoviesDB dbMovies = new tableMoviesDB(db);
        Cursor cursor = dbMovies.executeCustomQuery(query, null);

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        return count;
    }

    public boolean insertData(movieData_Domain movieData, tableEvalsDB.Eval movieEvals){
        try {
//            // Получаем экземпляр базы данных
//            databaseManager dbManager = new databaseManager(context);
//            dbManager.open();
//            db = dbManager.getDatabase();

            // Начинаем транзакцию
            db.beginTransaction();

            // Вставляем данные в таблицу movies
            ContentValues movieValues = new ContentValues();
            movieValues.put("movie_name", movieData.getTitle());
            movieValues.put("movie_rating", movieData.getRating());
            movieValues.put("movie_year", movieData.getYear());
            movieValues.put("movie_type", movieData.getmType());
            movieValues.put("movie_review", movieData.isReview() ? 1 : 0);
            movieValues.put("movie_dateRate", movieData.getDateRate());
            long movieId = db.insert("movies", null, movieValues);

            // Вставляем данные в таблицу evaluations
            ContentValues evalValues = new ContentValues();
            evalValues.put("eval_id", movieId); // Предполагается, что eval_id совпадает с movie_id
            evalValues.put("eval_story", movieEvals.eval_story);
            evalValues.put("eval_grath", movieEvals.eval_grath);
            evalValues.put("eval_actor", movieEvals.eval_actor);
            evalValues.put("eval_shoot", movieEvals.eval_shoot);
            evalValues.put("eval_feels", movieEvals.eval_feels);
            evalValues.put("eval_logik", movieEvals.eval_logik);
            evalValues.put("eval_orig", movieEvals.eval_orig);
            evalValues.put("eval_think", movieEvals.eval_think);
            evalValues.put("eval_gripp", movieEvals.eval_gripp);
            db.insert("evaluations", null, evalValues);

            // Вставляем данные в таблицу movie_genres (если есть жанры)
            if (movieData.getGenres() != null) {
                String[] genres = movieData.getGenres().split(", ");
                for (String genre : genres) {
                    ContentValues genreValues = new ContentValues();
                    genreValues.put("mg_movie_id", movieId);
                    genreValues.put("mg_genre_id", getGenreIdByName(db, genre)); // Метод для получения ID жанра по имени
                    db.insert("movie_genres", null, genreValues);
                }
            }

            // Устанавливаем успешное завершение транзакции
            db.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Завершаем транзакцию
            if (db != null) {
                db.endTransaction();
                //db.close(); ??
            }
        }
    }

    public boolean updateData(movieData_Domain movieData, tableEvalsDB.Eval movieEvals) {
        try {
            // Начинаем транзакцию
            db.beginTransaction();

            // Обновляем данные в таблице movies
            ContentValues movieValues = new ContentValues();
            movieValues.put("movie_name", movieData.getTitle());
            movieValues.put("movie_rating", movieData.getRating());
            movieValues.put("movie_year", movieData.getYear());
            movieValues.put("movie_type", movieData.getmType());
            movieValues.put("movie_review", movieData.isReview() ? 1 : 0);
            movieValues.put("movie_dateRate", movieData.getDateRate());

            // Обновление данных по movie_id
            String whereClause = "movie_id = ?";
            String[] whereArgs = { String.valueOf(movieData.getID()) };
            int rowsAffected = db.update("movies", movieValues, whereClause, whereArgs);

            if (rowsAffected == 0) {
                // Если не удалось обновить запись (например, запись не существует)
                return false;
            }

            // Обновляем данные в таблице evaluations
            ContentValues evalValues = new ContentValues();
            evalValues.put("eval_story", movieEvals.eval_story);
            evalValues.put("eval_grath", movieEvals.eval_grath);
            evalValues.put("eval_actor", movieEvals.eval_actor);
            evalValues.put("eval_shoot", movieEvals.eval_shoot);
            evalValues.put("eval_feels", movieEvals.eval_feels);
            evalValues.put("eval_logik", movieEvals.eval_logik);
            evalValues.put("eval_orig", movieEvals.eval_orig);
            evalValues.put("eval_think", movieEvals.eval_think);
            evalValues.put("eval_gripp", movieEvals.eval_gripp);

            // Обновление данных по eval_id (который равен movie_id)
            whereClause = "eval_id = ?";
            whereArgs = new String[]{ String.valueOf(movieData.getID()) };
            rowsAffected = db.update("evaluations", evalValues, whereClause, whereArgs);

            if (rowsAffected == 0) {
                // Если не удалось обновить запись (например, запись не существует)
                return false;
            }

            // Удаляем старые записи из таблицы movie_genres для данного movie_id
            db.delete("movie_genres", "mg_movie_id = ?", new String[]{ String.valueOf(movieData.getID()) });

            // Вставляем новые данные в таблицу movie_genres
            if (movieData.getGenres() != null) {
                String[] genres = movieData.getGenres().split(", ");
                for (String genre : genres) {
                    ContentValues genreValues = new ContentValues();
                    genreValues.put("mg_movie_id", movieData.getID());
                    genreValues.put("mg_genre_id", getGenreIdByName(db, genre)); // Метод для получения ID жанра по имени
                    db.insert("movie_genres", null, genreValues);
                }
            }

            // Устанавливаем успешное завершение транзакции
            db.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Завершаем транзакцию
            if (db != null) {
                db.endTransaction();
                // Обычно закрытие базы данных не требуется в методе update, если вы используете
                // SQLiteOpenHelper и используете один экземпляр базы данных в течение жизненного цикла активности
                //db.close(); // В этом случае не закрывайте базу данных, если это не требуется
            }
        }
    }

    // Метод для получения ID жанра по его имени, если жанра нет - добавить его
    private int getGenreIdByName(SQLiteDatabase db, String genreName) {
        int genreId = -1;
        Cursor cursor = null;

        try {
            // Попробуем найти жанр в таблице genres
            cursor = db.query("genres", new String[]{"genre_id"}, "genre_name=?", new String[]{genreName}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Жанр найден, получаем его ID
                genreId = cursor.getInt(cursor.getColumnIndex("genre_id"));
            } else {
                // Жанра нет, добавляем его в таблицу
                ContentValues values = new ContentValues();
                values.put("genre_name", genreName);
                genreId = (int) db.insert("genres", null, values);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return genreId;
    }

    public boolean deleteMovie(int movieId){
        try {
            // Удаление жанров фильма
            String deleteMovieGenres = "DELETE FROM movie_genres WHERE mg_movie_id = ?";
            db.execSQL(deleteMovieGenres, new Object[]{movieId});

            // Удаление оценок фильма
            String deleteMovieRatings = "DELETE FROM evaluations WHERE eval_id = ?";
            db.execSQL(deleteMovieRatings, new Object[]{movieId});

            // Удаление самого фильма
            String deleteMovie = "DELETE FROM movies WHERE movie_id = ?";
            db.execSQL(deleteMovie, new Object[]{movieId});
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public SQLiteDatabase getDb(){
        return db;
    }
}
