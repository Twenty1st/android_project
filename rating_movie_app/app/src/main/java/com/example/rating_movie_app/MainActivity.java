package com.example.rating_movie_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton butRatingActivity = findViewById(R.id.fabAdd);
        butRatingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для перехода
                Intent intent = new Intent(MainActivity.this, editDataRate_class.class);
                intent.putExtra("check", true);
                startActivity(intent);
            }
        });
    }


}