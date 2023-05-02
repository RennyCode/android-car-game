package com.example.a23b_11345_l01b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

public class MenuActivity extends Activity {

    private MaterialButton[] menu_BTN_options;
    private ToggleButton menu_TOGGLE;
    private int isToggled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isToggled = 0;
        findViews();
        setAnswersClickListeners();

    }


    private void setAnswersClickListeners() {
        for (MaterialButton mb : menu_BTN_options) {
            mb.setOnClickListener(v -> clicked(mb.getText().toString()));
        }
        menu_TOGGLE.setOnCheckedChangeListener((toggleButton, isChecked) -> isToggled = isChecked? 1 : 0);
    }

    private void clicked(String button_name){

            if (button_name.compareTo("play_button") == 0) {
                // start game , pass flag for btn or movement
                Intent secondActivityIntent = new Intent(this, MainActivity.class);
                secondActivityIntent.putExtra(MainActivity.KEY_BTN, isToggled);
                startActivity(secondActivityIntent);
                finish();
            } else{
                // go straight to score board
                Intent secondActivityIntent = new Intent(this, endgameActivity.class);
                startActivity(secondActivityIntent);
                finish();
        }
    }
    private void findViews() {
        menu_BTN_options = new MaterialButton[]{
                findViewById(R.id.play_button),
                findViewById(R.id.score_button)};

        menu_TOGGLE = (ToggleButton) findViewById(R.id.toggleButton);
    }
}
