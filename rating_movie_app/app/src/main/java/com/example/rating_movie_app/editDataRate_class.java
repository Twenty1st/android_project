package com.example.rating_movie_app;

import static com.example.rating_movie_app.ShowToast.showToast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rating_movie_app.DataBase.databaseManager;
import com.example.rating_movie_app.DataBase.movieData_Domain;
import com.example.rating_movie_app.DataBase.queryForDB;
import com.example.rating_movie_app.DataBase.tableEvalsDB;
import com.example.rating_movie_app.DataBase.tableMoviesDB;
import com.example.rating_movie_app.rateFilms_Adapter.recycleDomain;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class editDataRate_class extends AppCompatActivity {

    private ChipGroup chipGroup;
    private EditText genresText;

    private boolean isEdit;
    private int movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_rate_activity);

        chipGroup = findViewById(R.id.chipGroup);
        genresText = findViewById(R.id.editText);


        chipGenresListener();
        syncSeekBarwithText();

        Intent intent = getIntent();
        if (intent != null) {
            this.isEdit = intent.getBooleanExtra("isEdit", false);

            if (isEdit) {
                TextView text = findViewById(R.id.textTitleActivity);
                text.setText(R.string.editDataText);
                Button button = findViewById(R.id.butOk);
                button.setText("Изменить");
                button = findViewById(R.id.butBack);
                button.setText("Назад");
                movieID = intent.getIntExtra("movieID", 0);
                getDataFromDB(movieID);
            }else{
                setDatainBox("", "");
                Button button = findViewById(R.id.butDel);
                button.setVisibility(View.GONE);
                //this.isEdit = true;
            }
        }

        countRatingandScore();
        setButtonListeners();
        setListenTouch();


    }



    //region UI listeners
    private void chipGenresListener(){
        genresText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                addChipFromInput();
                return true;
            }
            return false;
        });
        // Adding a TextWatcher to detect spaces
        genresText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if the last character entered is a space
                if (s.toString().endsWith(" ")) {
                    addChipFromInput();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text is changed
            }
        });
    }
    private void setupSeekBarListener(SeekBar seekBar, TextView textView) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Обновляем TextView с текущим значением
                textView.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Вызывается, когда пользователь начинает перемещение ползунка
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Вызывается, когда пользователь завершает перемещение ползунка
                countRatingandScore();
            }
        });
    }
    private void setButtonListeners(){
        Button butBack = findViewById(R.id.butBack);
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем Intent для перехода
                Intent intent = new Intent(editDataRate_class.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button butOk = findViewById(R.id.butOk);
        butOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataonDB();
            }
        });
        Button butDell = findViewById(R.id.butDel);
        butDell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMovie();
            }
        });
    }

    private void setListenTouch(){
        EditText editText = findViewById(R.id.textName);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    editText.clearFocus();
                }
                return false;
            }
        });

    }

//endregion

//region activity functions

    private void addChipFromInput() {
        String text = genresText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            Chip chip = new Chip(this);
            chip.setText(text);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));
            chipGroup.addView(chip);
            genresText.setText("");
        }
    }

    private void syncSeekBarwithText(){
        SeekBar seekBar = findViewById(R.id.seekBarStory);
        TextView text = findViewById(R.id.textEstimationStory);
        setupSeekBarListener(seekBar, text);

        seekBar = findViewById(R.id.seekBarGrath);
        text = findViewById(R.id.textEstimationGrath);
        setupSeekBarListener(seekBar, text);

        seekBar = findViewById(R.id.seekBarActor);
        text = findViewById(R.id.textEstimationActor);
        setupSeekBarListener(seekBar, text);

        seekBar = findViewById(R.id.seekBarFeels);
        text = findViewById(R.id.textEstimationFeels);
        setupSeekBarListener(seekBar, text);

        seekBar = findViewById(R.id.seekBarShoot);
        text = findViewById(R.id.textEstimationShoot);
        setupSeekBarListener(seekBar, text);

        seekBar = findViewById(R.id.seekBarLogik);
        text = findViewById(R.id.textEstimationLogik);
        setupSeekBarListener(seekBar, text);

        seekBar = findViewById(R.id.seekBarOrig);
        text = findViewById(R.id.textEstimationOrig);
        setupSeekBarListener(seekBar, text);

        seekBar = findViewById(R.id.seekBarThink);
        text = findViewById(R.id.textEstimationThink);
        setupSeekBarListener(seekBar, text);

        seekBar = findViewById(R.id.seekBarGripp);
        text = findViewById(R.id.textEstimationGripp);
        setupSeekBarListener(seekBar, text);

    }

    void countRatingandScore(){
        int countScore = 65; //10+10+10+10+5+5+5+5


        TextView textRating = findViewById(R.id.textRating);
        TextView textScore = findViewById(R.id.textScore);

        int estimationStory = getEstimation(findViewById(R.id.textEstimationStory));
        int estimationGrath = getEstimation(findViewById(R.id.textEstimationGrath));
        int estimationActor = getEstimation(findViewById(R.id.textEstimationActor));
        int estimationFeels = getEstimation(findViewById(R.id.textEstimationFeels));
        int estimationShoot = getEstimation(findViewById(R.id.textEstimationShoot));
        int estimationLogik = getEstimation(findViewById(R.id.textEstimationLogik));
        int estimationOrig = getEstimation(findViewById(R.id.textEstimationOrig));
        int estimationThink = getEstimation(findViewById(R.id.textEstimationThink));
        int estimationGripp = getEstimation(findViewById(R.id.textEstimationGripp));

        int scoreAll = estimationStory+estimationGrath+estimationActor+estimationFeels+estimationShoot+estimationThink+estimationLogik+estimationOrig+estimationGripp;
        Log.d("scoreAll", scoreAll+"");
        float rating = (float)(scoreAll)*10/countScore;
        // Определяем, в каком интервале находится дробная часть
        float fractionalPart = rating - (int) rating;
        float roundedValue;

        if (fractionalPart >= 0.000f && fractionalPart < 0.350f) {
            roundedValue = (float) Math.floor(rating);
        } else if (fractionalPart >= 0.350f && fractionalPart < 0.740f) {
            roundedValue = (float) Math.floor(rating) + 0.5f;
        } else {
            roundedValue = (float) Math.ceil(rating);
        }
        textRating.setText(String.valueOf(roundedValue));

        textScore.setText(String.format(Locale.getDefault(), "%d/"+countScore, scoreAll));


    }

    private int getEstimation(TextView text){
        if (text != null && text.getText() != null) {
            try {
                return Integer.parseInt(text.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace(); // или обработка ошибки, в зависимости от ваших требований
            }
        }
        return 0;
    }

    private void setDatainBox(String ganre, String type) {

        Spinner spinner = findViewById(R.id.boxTypes);

        // Задаем массив строк из ресурсов
        String[] items = getResources().getStringArray(R.array.movie_types);

        // Создаем ArrayAdapter с использованием встроенного макета для простого выпадающего списка
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        // Применяем адаптер к Spinner
        spinner.setAdapter(adapter);
        if(!type.equals("")){
            int index = -1;
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(type)) {
                    index = i;
                    break;
                }
            }
            // Устанавливаем выбор в Spinner
            if (index != -1) {
                spinner.setSelection(index);
            }
        }


        String[] ganres = ganre.split(",");
    }

    private void clearFields(){
        EditText editText = findViewById(R.id.textName);
        editText.setText("");

        editText = findViewById(R.id.textYear);
        editText.setText("");

        SeekBar sbar = findViewById(R.id.seekBarStory);
        sbar.setProgress(0);
        sbar = findViewById(R.id.seekBarGrath);
        sbar.setProgress(0);
        sbar = findViewById(R.id.seekBarActor);
        sbar.setProgress(0);
        sbar = findViewById(R.id.seekBarShoot);
        sbar.setProgress(0);
        sbar = findViewById(R.id.seekBarFeels);
        sbar.setProgress(0);
        sbar = findViewById(R.id.seekBarLogik);
        sbar.setProgress(0);
        sbar = findViewById(R.id.seekBarOrig);
        sbar.setProgress(0);
        sbar = findViewById(R.id.seekBarThink);
        sbar.setProgress(0);
        sbar = findViewById(R.id.seekBarGripp);
        sbar.setProgress(0);


        Spinner spinner = findViewById(R.id.boxTypes);
        spinner.setSelection(0);

        CheckBox checkBox = findViewById(R.id.checkReview);
        checkBox.setChecked(false);

        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        chipGroup.removeAllViews();

        countRatingandScore();

    }

//endregion

//region database work funcs

    private void getDataFromDB(int movieID) {
        databaseManager dbManager = new databaseManager(editDataRate_class.this);
        dbManager.open();

        SQLiteDatabase db = dbManager.getDatabase();
        tableMoviesDB dbMovies = new tableMoviesDB(db);

        String[] movieData = dbMovies.getMovieById(String.valueOf(movieID));

        Log.d("Дата", movieData[0]+", "+movieData[1]+", "+movieData[2]+", "+movieData[3]+", "+movieData[4]);

        EditText editText = findViewById(R.id.textName);
        editText.setText(movieData[0]);

        editText = findViewById(R.id.textYear);
        editText.setText(movieData[1]);

        setDatainBox(movieData[3], movieData[2]);

        CheckBox checkBox = findViewById(R.id.checkReview);
        if(movieData[4].equals("1")){
            checkBox.setChecked(true);
        }

        tableEvalsDB evals = new tableEvalsDB(db);

        // Получаем данные оценки для фильма с определенным ID
        tableEvalsDB.Eval evalsData = evals.getEvalById(movieID);

        // Теперь можно получить доступ к каждому полю:
        SeekBar seekBar = findViewById(R.id.seekBarStory);
        seekBar.setProgress(evalsData.eval_story-1);
        seekBar = findViewById(R.id.seekBarGrath);
        seekBar.setProgress(evalsData.eval_grath-1);
        seekBar = findViewById(R.id.seekBarActor);
        seekBar.setProgress(evalsData.eval_actor-1);
        seekBar = findViewById(R.id.seekBarShoot);
        seekBar.setProgress(evalsData.eval_shoot-1);
        seekBar = findViewById(R.id.seekBarFeels);
        seekBar.setProgress(evalsData.eval_feels-1);
        seekBar = findViewById(R.id.seekBarLogik);
        seekBar.setProgress(evalsData.eval_logik-1);
        seekBar = findViewById(R.id.seekBarOrig);
        seekBar.setProgress(evalsData.eval_orig-1);
        seekBar = findViewById(R.id.seekBarThink);
        seekBar.setProgress(evalsData.eval_think-1);
        seekBar = findViewById(R.id.seekBarGripp);
        seekBar.setProgress(evalsData.eval_gripp-1);
    }

    private void setDataonDB() {

        queryForDB dbQuery = new queryForDB();



        int estimationStory = getEstimation(findViewById(R.id.textEstimationStory));
        int estimationGrath = getEstimation(findViewById(R.id.textEstimationGrath));
        int estimationActor = getEstimation(findViewById(R.id.textEstimationActor));
        int estimationFeels = getEstimation(findViewById(R.id.textEstimationFeels));
        int estimationShoot = getEstimation(findViewById(R.id.textEstimationShoot));
        int estimationLogik = getEstimation(findViewById(R.id.textEstimationLogik));
        int estimationOrig = getEstimation(findViewById(R.id.textEstimationOrig));
        int estimationThink = getEstimation(findViewById(R.id.textEstimationThink));
        int estimationGripp = getEstimation(findViewById(R.id.textEstimationGripp));

        tableEvalsDB.Eval evalsData = new tableEvalsDB.Eval(estimationStory, estimationGrath, estimationActor, estimationFeels,
                estimationShoot, estimationLogik, estimationOrig, estimationThink, estimationGripp);

        EditText editText = findViewById(R.id.textName);
        String movieName = editText.getText().toString();
        if (movieName.equals("")) {
            showToast(this, "Заполните все поля!");
            return;
        }

        editText = findViewById(R.id.textYear);
        int movieYear;
        try {
            movieYear = Integer.parseInt(editText.getText().toString());
        } catch (Exception ex) {
            showToast(this, "Заполните все поля!");
            return;
        }

        Spinner spinner = findViewById(R.id.boxTypes);
        int movieType;
        movieType = spinner.getSelectedItemPosition();
        if (movieType == 0) {
            showToast(this, "Заполните все поля!");
            return;
        }

        StringBuilder chipData = new StringBuilder();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            View chip = chipGroup.getChildAt(i);
            if (chip instanceof Chip) {
                chipData.append(((Chip) chip).getText().toString()).append(", ");
            }
        }

        // Удалить последнее ", " для корректного формата строки
        if (chipData.length() > 0) {
            chipData.setLength(chipData.length() - 2);
        }

        String movieGenres = chipData.toString();

        CheckBox checkBox = findViewById(R.id.checkReview);
        boolean movieReview = false;
        if (checkBox.isChecked()) {
            movieReview = true;
        }

        TextView text = findViewById(R.id.textRating);
        double rating = Double.parseDouble(text.getText().toString());

        String movieDateRate = LocalDateTime.now().format("dd.MM.yyyy");

        movieData_Domain movieData = new movieData_Domain(movieID, movieName, movieGenres, movieYear, movieDateRate, rating, movieType, movieReview);


        if (!isEdit) {

            boolean isOK = dbQuery.insertData(movieData, evalsData, editDataRate_class.this);

            showToast(this, "Успешно!");

            clearFields();

        } else {

        }
    }

    private void deleteMovie() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Подтверждение удаления");
        builder.setMessage("Вы уверены, что хотите удалить этот элемент?");

        // Кнопка для подтверждения удаления
        builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseManager dbManager = new databaseManager(editDataRate_class.this);
                dbManager.open();

                SQLiteDatabase db = dbManager.getDatabase();
                tableMoviesDB movie = new tableMoviesDB(db);
                movie.deleteMovie(movieID);
                db.close();
                dialog.dismiss();
                Intent intent = new Intent(editDataRate_class.this, MainActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

//endregion
}
