package edu.ktu.example.guessthenumber;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardActivity extends Activity {

    TextView errorText;
    TextView pageNumber;

    int page;

    private List<ScoreEntry> publicEntrys;

    RecyclerView recyclerView;
    CustomAdapter_Score customAdapter_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scoreboard);

        errorText = findViewById(R.id.lb_ErrorMsg);
        pageNumber = findViewById(R.id.text_scoreBoard_page);

        page = 1;
        pageNumber.setText("1");

        publicEntrys = new ArrayList<>();

        recyclerView = findViewById(R.id.scoreboard_public);
        customAdapter_score  = new CustomAdapter_Score(this, publicEntrys);
        recyclerView.setAdapter(customAdapter_score);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sendGetRequest(page);
    }

    //gauna tam tikrą kiekį žaidimų rezultatų iš api , nurodyti puslapį
    public void sendGetRequest(int page)
    {
        RequestQueue queue = Volley.newRequestQueue(ScoreboardActivity.this);
        String url = "http://192.168.1.151/appApi/getEntries.php?page=" + page;

        JsonArrayRequest jsonRequest = new JsonArrayRequest (Request.Method.GET, url, null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                extractEntries(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    errorText.setText("error " + error.toString());
            }
        });

        queue.add(jsonRequest);
    }

    //JSON array išskaido į JSON objektus ir sudeda juos į sąrašą ir pranešą adaptoriui
    public void extractEntries(JSONArray jsonArray)
    {
        try {
            publicEntrys.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                // Get current json object
                JSONObject entry = jsonArray.getJSONObject(i);

                ScoreEntry se = new ScoreEntry();
                se.setName(entry.getString("name"));
                se.setScore(entry.getInt("score"));
                se.setDifficulty(entry.getInt("diff"));

                publicEntrys.add(se);

            }
            customAdapter_score.notifyDataSetChanged();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void pageBackClick(View view)
    {
        page--;
        page = clamp(page, 1, 999);
        pageNumber.setText(String.valueOf(page));
        sendGetRequest(page);
    }

    public void pageNextClick(View view)
    {
        page++;
        page = clamp(page, 1, 999);
        pageNumber.setText(String.valueOf(page));
        sendGetRequest(page);
    }

    public int clamp(int number, int min , int max)
    {
        if(number <= min)
        {
            return min;
        }
        else if(number >= max)
        {
            return max;
        }
        else
            return number;
    }
}

//manifeste prideti android:usesCleartextTraffic="true"