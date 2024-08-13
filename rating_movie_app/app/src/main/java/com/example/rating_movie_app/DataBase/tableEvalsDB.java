package com.example.rating_movie_app.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class tableEvalsDB {
    private SQLiteDatabase db;

    public tableEvalsDB(SQLiteDatabase db) {
        this.db = db;
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

    public Eval getEvalById(int evalId) {
        Cursor cursor = null;
        Eval eval = null;
        try {
            cursor = db.query("evaluations", null, "eval_id=?", new String[]{String.valueOf(evalId)},
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                eval = new Eval(
                        cursor.getInt(cursor.getColumnIndex("eval_story")),
                        cursor.getInt(cursor.getColumnIndex("eval_grath")),
                        cursor.getInt(cursor.getColumnIndex("eval_actor")),
                        cursor.getInt(cursor.getColumnIndex("eval_shoot")),
                        cursor.getInt(cursor.getColumnIndex("eval_feels")),
                        cursor.getInt(cursor.getColumnIndex("eval_logik")),
                        cursor.getInt(cursor.getColumnIndex("eval_orig")),
                        cursor.getInt(cursor.getColumnIndex("eval_think")),
                        cursor.getInt(cursor.getColumnIndex("eval_gripp"))
                );
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return eval;
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

    public int deleteEvals(int evalId) {
        return db.delete("evaluations", "eval_id=?", new String[]{String.valueOf(evalId)});
    }

    // Вспомогательный класс для хранения данных оценок
    public static class Eval {
        public int eval_story;
        public int eval_grath;
        public int eval_actor;
        public int eval_shoot;
        public int eval_feels;
        public int eval_logik;
        public int eval_orig;
        public int eval_think;
        public int eval_gripp;

        public Eval(int eval_story, int eval_grath, int eval_actor, int eval_shoot, int eval_feels, int eval_logik, int eval_orig, int eval_think, int eval_gripp) {
            this.eval_story = eval_story;
            this.eval_grath = eval_grath;
            this.eval_actor = eval_actor;
            this.eval_shoot = eval_shoot;
            this.eval_feels = eval_feels;
            this.eval_logik = eval_logik;
            this.eval_orig = eval_orig;
            this.eval_think = eval_think;
            this.eval_gripp = eval_gripp;
        }
    }
}
