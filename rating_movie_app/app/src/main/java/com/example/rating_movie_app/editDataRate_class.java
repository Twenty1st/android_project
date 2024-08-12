package com.example.rating_movie_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Locale;

public class editDataRate_class extends AppCompatActivity {

    private ChipGroup chipGroup;
    private EditText genresText;

    private boolean isEdit;
    private int movieID = 0;

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
                Button button = findViewById(R.id.butOk);
                button.setText("Изменить");
                button = findViewById(R.id.butBack);
                button.setText("Назад");
                // this.isEdit = false;
                String movieName = intent.getStringExtra("movieName");
                //getDataFromDB(movieName);
            }else{
                setDatainBox("", "");
                Button button = findViewById(R.id.butDel);
                button.setVisibility(View.GONE);
                //this.isEdit = true;
            }
        }

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

//endregion

//region functions

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

//endregion
}
