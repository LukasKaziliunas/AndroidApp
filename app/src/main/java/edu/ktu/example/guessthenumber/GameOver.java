package edu.ktu.example.guessthenumber;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class GameOver extends Activity {

    private static final String PREFERENCES_FILE_NAME = "SettingsPref";
    SharedPreferences settings;

    int difficulty;
    int  score;
    int guessedNumber;
    int randomNumber;

    AlertDialog.Builder builder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        settings = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);

        setContentView(R.layout.blank);

        Intent intent = getIntent();
        boolean win = intent.getBooleanExtra("win", false);
        guessedNumber = intent.getIntExtra("guessedNumber", -1);
        randomNumber = intent.getIntExtra("randomNumber", -1);
        score = intent.getIntExtra("score", 0);
        difficulty =  intent.getIntExtra("difficulty", 0);

        builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(R.string.alert_msg_confirm_share);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.alert_yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendPostRequest(settings.getString("playerName" , "no_name"), score , difficulty);
                    }
                });

        builder1.setNegativeButton(
                R.string.alert_no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        if(win)
        {
            setContentView(R.layout.activity_game_over_win);
            TextView scoreText = findViewById(R.id.gameOver_score);
            scoreText.setText("Score : " + String.valueOf(score));

        }
        else
        {
            setContentView(R.layout.game_over_lost);
            TextView randomNumberText = findViewById(R.id.gameOver_randomNumber);
            TextView guessedNumberText = findViewById(R.id.gameOver_guessedNumer);
            randomNumberText.setText("Random number was " + String.valueOf(randomNumber));
            guessedNumberText.setText("Your guess was " + String.valueOf(guessedNumber));

        }
    }

    public void shareClick(View view)
    {
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    //išsiunčia žaidimo rezultatą į nutolusią duomenų bazę
    public void sendPostRequest(final String name, final int score, final int difficulty)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(GameOver.this);
        String url = "http://192.168.1.151/appApi/addEntry.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            // tam kad duomenis pridėti į http užklausos body dalį reikia parrašyti getParams() metodą ir duomenis dėti į hashMap
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("score", String.valueOf(score));
                params.put("difficulty",String.valueOf(difficulty));
                return params;
            }
            // prie http užklausos pridedami headers parrašant getHeaders() metodą
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
