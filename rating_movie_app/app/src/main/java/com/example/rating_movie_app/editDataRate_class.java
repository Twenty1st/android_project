package com.example.rating_movie_app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class editDataRate_class extends AppCompatActivity {

    private ChipGroup chipGroup;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_rate_activity);

        chipGroup = findViewById(R.id.chipGroup);
        editText = findViewById(R.id.editText);

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                addChipFromInput();
                return true;
            }
            return false;
        });
        // Adding a TextWatcher to detect spaces
        editText.addTextChangedListener(new TextWatcher() {
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

    private void addChipFromInput() {
        String text = editText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            Chip chip = new Chip(this);
            chip.setText(text);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));
            chipGroup.addView(chip);
            editText.setText("");
        }
    }
}
