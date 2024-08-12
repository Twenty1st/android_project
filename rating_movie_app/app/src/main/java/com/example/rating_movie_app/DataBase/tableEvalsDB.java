package com.example.rating_movie_app.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class tableEvalsDB {
    private final SQLiteDatabase db;
    private final helpDataBase dbHelper;
    public tableEvalsDB(Context context) {
        dbHelper = new helpDataBase(context);
        db = dbHelper.getWritableDatabase();
    }

    public void insertEval(int eval_id, int eval_story, int eval_grath, int eval_actor, int eval_shoot, int eval_feels, int eval_logik,
                           int eval_orig, int eval_think, int eval_gripp) {
        ContentValues values = new ContentValues();
        values.put("eval_id", eval_id);
        values.put("eval_story", eval_story);
        values.put("eval_grath", eval_grath);
        values.put("eval_actor", eval_actor);
        values.put("eval_shoot", eval_shoot);
        values.put("eval_feels", eval_feels);
        values.put("eval_logik", eval_logik);
        values.put("eval_orig", eval_orig);
        values.put("eval_think", eval_think);
        values.put("eval_gripp", eval_gripp);
        db.insert("evaluations", null, values);
    }

//    public Cursor getAllMovies() {
//        return db.query("movies", null, null, null, null, null, null);
//    }

    public String[] getEvalById(int evalId) {
        Cursor cursor = db.query("evaluations", null, "eval_id=?", new String[]{String.valueOf(evalId)},
                null, null, null);
        String[] evalData = null;
        if (cursor != null && cursor.moveToFirst()) {
            int eval_story = cursor.getInt(cursor.getColumnIndex("eval_story"));
            int eval_grath = cursor.getInt(cursor.getColumnIndex("eval_grath"));
            int eval_actor = cursor.getInt(cursor.getColumnIndex("eval_actor"));
            int eval_shoot = cursor.getInt(cursor.getColumnIndex("eval_shoot"));
            int eval_feels = cursor.getInt(cursor.getColumnIndex("eval_feels"));
            int eval_logik = cursor.getInt(cursor.getColumnIndex("eval_logik"));
            int eval_orig = cursor.getInt(cursor.getColumnIndex("eval_orig"));
            int eval_think = cursor.getInt(cursor.getColumnIndex("eval_think"));
            int eval_gripp = cursor.getInt(cursor.getColumnIndex("eval_gripp"));

            // Создаем массив строк для хранения данных
            evalData = new String[]{String.valueOf(eval_story), String.valueOf(eval_grath), String.valueOf(eval_actor), String.valueOf(eval_shoot),
                    String.valueOf(eval_feels), String.valueOf(eval_logik), String.valueOf(eval_orig), String.valueOf(eval_think), String.valueOf(eval_gripp)};

            cursor.close();
        }
        return evalData;
    }

    public int updateEvalRating(int evalId, int eval_story, int eval_grath, int eval_actor, int eval_shoot, int eval_feels, int eval_logik, int eval_orig,
                                int eval_think, int eval_gripp) {
        ContentValues values = new ContentValues();
        values.put("eval_story", eval_story);
        values.put("eval_grath", eval_grath);
        values.put("eval_actor", eval_actor);
        values.put("eval_shoot", eval_shoot);
        values.put("eval_feels", eval_feels);
        values.put("eval_logik", eval_logik);
        values.put("eval_orig", eval_orig);
        values.put("eval_think", eval_think);
        values.put("eval_gripp", eval_gripp);
        return db.update("evaluations", values, "eval_id=?", new String[]{String.valueOf(evalId)});
    }
    public void close() {
        dbHelper.close();
    }
}
