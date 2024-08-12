package com.example.rating_movie_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.rating_movie_app.rateFilms_Adapter.recycleAdapter;
import com.example.rating_movie_app.rateFilms_Adapter.recycleDomain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewRatingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton butAddRate = findViewById(R.id.fabAdd);
        butAddRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для перехода
                Intent intent = new Intent(MainActivity.this, editDataRate_class.class);
                intent.putExtra("edit", false);
                startActivity(intent);
            }
        });

        create_recycleView();

    }

    private void create_recycleView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewRatingList = findViewById(R.id.recycleViewRatings);
        recyclerViewRatingList.setLayoutManager(linearLayoutManager);


        ArrayList<recycleDomain> movies = new ArrayList();
        recycleAdapter.ItemClickListener listener = new recycleAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String movieName) {
                Intent intent = new Intent(MainActivity.this, editDataRate_class.class);
                intent.putExtra("edit", true);
                intent.putExtra("movieName", movieName);
                startActivity(intent);
            }
        };

        movies.add(new recycleDomain("Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 8.5));
        movies.add(new recycleDomain("Длинное название для какого-то фильма jfjffjfjfjfjjffjfjfjfjfjjfjfdfkdfjkdfkjhgkdsjsf", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 7.5));
        movies.add(new recycleDomain("Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика, боевик, фантастика", 2008, "05.08.2024", 2.5));
        movies.add(new recycleDomain("Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 4.5));
        movies.add(new recycleDomain("Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика, боевик, фантастика, боевик, фантастика, боевик, фантастика, боевик, фантастика", 2008, "05.08.2024", 5.5));
        movies.add(new recycleDomain("Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 6.5));
        movies.add(new recycleDomain("Длинное название для какого-то фильма", "комедия, биография, драма, боевик, фантастика", 2008, "05.08.2024", 9.5));

        adapter = new recycleAdapter(movies, listener);

        recyclerViewRatingList.setAdapter(adapter);
    }

}