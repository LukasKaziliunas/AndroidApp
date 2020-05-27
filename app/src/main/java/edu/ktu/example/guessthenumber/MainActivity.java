package edu.ktu.example.guessthenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    int m_number = 0;

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        Button startGameButton = findViewById(R.id.start_game_btn);

        Button resultButton = findViewById(R.id.result_btn);

        Button settingsButton = findViewById(R.id.settings_btn);

        Button aboutButton = findViewById(R.id.about_btn);

        startGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectActivity(v);
            }
        });

        resultButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectActivity(v);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectActivity(v);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                selectActivity(v);
            }
        });

    }

    public void selectActivity(View view)
    {
        if(view.getId() == R.id.start_game_btn)
        {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.result_btn)
        {
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.settings_btn)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.about_btn)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

    }

}
