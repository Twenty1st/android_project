package com.example.rating_movie_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rating_movie_app.DataBase.callDBMethods;
import com.example.rating_movie_app.DataBase.queryForDB;
import com.example.rating_movie_app.rateFilms_Adapter.recycleAdapter;
import com.example.rating_movie_app.rateFilms_Adapter.recycleDomain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewRatingList;

    private static callDBMethods dbHelper;
    private queryForDB dbQueries;
    private paginationForList paginationList;

    private boolean isReview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new callDBMethods(this);
        dbQueries = new queryForDB();

        setListeners();

        countAllRecords("WHERE movie_review = 1");
        countAllRecords("");

        // Получаем SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int savedPage = sharedPreferences.getInt("curPage", 1); // 1 - значение по умолчанию
        paginationList.setCurPage(savedPage);

        countPagination();

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Просто оставляем активити открытой и ничего не делаем
        // Это предотвратит переход к предыдущей активности
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();  // Закрытие базы данных
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Сохранение значения текущей страницы при уходе из активности
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("curPage", paginationList.getCurPage());
        editor.apply(); // Или editor.commit(); для синхронного сохранения
    }



    private void setDataInRecycleView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewRatingList = findViewById(R.id.recycleViewRatings);
        recyclerViewRatingList.setLayoutManager(linearLayoutManager);

        // Запрос данных из базы данных
        String query = queryForDB.getFull_query() + (isReview ? " WHERE movie_review = 1 " + queryForDB.getAddForQuery() : queryForDB.getAddForQuery());

        query +=" LIMIT "+ paginationList.getMaxRecords()+" OFFSET "+paginationList.getMaxRecords()*(paginationList.getCurPage()-1);
        ArrayList<recycleDomain> movies = dbHelper.selectFromDB(query);

        recycleAdapter.ItemClickListener listener = new recycleAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int movieID) {
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

    private void setListeners(){
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
        ImageButton button = findViewById(R.id.leftPag);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paginationList.minusPage();
                countPagination();
            }
        });

        button = findViewById(R.id.rightPag);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paginationList.plusPage();
                countPagination();
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        isReview = false;
                        countAllRecords("");
                        break;

                    case 1:
                        isReview = true;
                        countAllRecords(" WHERE movie_review = 1");
                        break;
                }
                //clearQueries();

                countPagination();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Ваш код при снятии выбора с вкладки
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Ваш код при повторном выборе вкладки
            }
        });

    }

    //region pagination

    private void countAllRecords(String addQuery){
        String query = queryForDB.getQuery_CountMovies() + addQuery;
        int count = dbHelper.getCountFromTable(query);

        TabLayout tabLayout = findViewById(R.id.tabs);

        TabLayout.Tab tab;
        if (addQuery.equals("")) {
            tab = tabLayout.getTabAt( 0);
            String newText = getString(R.string.textAllRates) + " (" + count+")";
            tab.setText(newText);

            paginationList = new paginationForList(count);
        }else{
            tab = tabLayout.getTabAt( 1);
            String newText = getString(R.string.textReviewMovies) + " (" + count+")";
            tab.setText(newText);

            if(isReview){paginationList = new paginationForList(count);}

        }


    }

    private void countPagination(){
        ImageButton button = findViewById(R.id.leftPag);
        button.setEnabled(false);
        if(paginationList.getCurPage() != 1){
            button.setEnabled(true);
        }
        button = findViewById(R.id.rightPag);
        button.setEnabled(false);
        if(paginationList.getCurPage() != paginationList.getAllPages()){
            button.setEnabled(true);
        }

        TextView numPage = findViewById(R.id.numpageText);
        String numpageText = paginationList.getCurPage()+"/"+paginationList.getAllPages();
        numPage.setText(numpageText);

        setDataInRecycleView();
    }


    //endregion

}