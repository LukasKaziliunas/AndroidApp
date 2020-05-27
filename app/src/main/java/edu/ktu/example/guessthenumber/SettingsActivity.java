package edu.ktu.example.guessthenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends Activity {

    private static final String PREFERENCES_FILE_NAME = "SettingsPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);

        String playerName = prefs.getString("playerName", "Name");
        int playerage = prefs.getInt("playerAge", 1);
        int difficulty = prefs.getInt("difficulty", 0);
        boolean music = prefs.getBoolean("music", true);

        EditText playerNameField = findViewById(R.id.editText_name);
        EditText playerAgeField = findViewById(R.id.editText_age);
        Spinner spinner = findViewById(R.id.spinner);
        Switch musicSwitch = findViewById(R.id.switch_music);

        playerNameField.setText(playerName);
        playerAgeField.setText(Integer.toString(playerage));
        spinner.setSelection(difficulty);
        musicSwitch.setChecked(music);


        Button backButton = findViewById(R.id.SettingsSave_btn);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveSettings(v);
            }
        });

    }

    public void saveSettings(View v){
        EditText playerNameField = findViewById(R.id.editText_name);
        EditText playerAgeField = findViewById(R.id.editText_age);
        Spinner spinner = findViewById(R.id.spinner);
        Switch musicSwitch = findViewById(R.id.switch_music);

        String playerName = playerNameField.getText().toString();
        int playerAge = Integer.parseInt(playerAgeField.getText().toString());
        int difficulty = spinner.getSelectedItemPosition();
        boolean music = musicSwitch.isChecked();

        SharedPreferences.Editor prefs = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE).edit();

        prefs.putString("playerName", playerName);
        prefs.putInt("playerAge", playerAge);
        prefs.putInt("difficulty", difficulty);
        prefs.putBoolean("music", music);

        prefs.apply();

        finish();
    }
}
