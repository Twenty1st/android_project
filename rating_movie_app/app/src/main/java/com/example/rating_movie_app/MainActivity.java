package com.example.rating_movie_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.rating_movie_app.DataBase.queryForDB;
import com.example.rating_movie_app.rateFilms_Adapter.recycleAdapter;
import com.example.rating_movie_app.rateFilms_Adapter.recycleDomain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewRatingList;

    private static queryForDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new queryForDB(this);

        FloatingActionButton butAddRate = findViewById(R.id.fabAdd);
        butAddRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для перехода
                Intent intent = new Intent(MainActivity.this, editDataRate_class.class);
                intent.putExtra("isEdit", false);
                startActivity(intent);
            }
        });

        create_recycleView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();  // Закрытие базы данных
        }
    }

    private void create_recycleView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewRatingList = findViewById(R.id.recycleViewRatings);
        recyclerViewRatingList.setLayoutManager(linearLayoutManager);

        // Запрос данных из базы данных
        String query = "SELECT movie_name, movie_year, type_name, movie_dateRate, movie_rating, " +
                "GROUP_CONCAT(genre_name ORDER BY genre_name SEPARATOR ', ') AS movie_genres " +
                "FROM movies JOIN mtypes ON type_id = movie_type " +
                "JOIN movie_genres ON mg_movie_id = movie_id " +
                "JOIN genres ON genre_id = mg_genre_id " +
                "GROUP BY movies.movie_id DESC";

        ArrayList<recycleDomain> movies = dbHelper.selectFromDB(query);

        recycleAdapter.ItemClickListener listener = new recycleAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String movieID) {
                Intent intent = new Intent(MainActivity.this, editDataRate_class.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("movieID", movieID);
                startActivity(intent);
            }
        };

//        movies.add(new recycleDomain(1, "Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 8.5));
//        movies.add(new recycleDomain(2, "Длинное название для какого-то фильма jfjffjfjfjfjjffjfjfjfjfjjfjfdfkdfjkdfkjhgkdsjsf", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 7.5));
//        movies.add(new recycleDomain(3, "Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика, боевик, фантастика", 2008, "05.08.2024", 2.5));
//        movies.add(new recycleDomain(4, "Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 4.5));
//        movies.add(new recycleDomain(5, "Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика, боевик, фантастика, боевик, фантастика, боевик, фантастика, боевик, фантастика", 2008, "05.08.2024", 5.5));
//        movies.add(new recycleDomain(6, "Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 6.5));
//        movies.add(new recycleDomain(7, "Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 9.5));

        adapter = new recycleAdapter(movies, listener);

        recyclerViewRatingList.setAdapter(adapter);
    }

}