package com.example.rating_movie_app.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class databaseManager {
    private SQLiteDatabase db;
    private helpDataBase dbHelper;

    public databaseManager(Context context) {
        dbHelper = new helpDataBase(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public SQLiteDatabase getDatabase() {
        return db;
    }
}
