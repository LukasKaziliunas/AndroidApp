package edu.ktu.example.guessthenumber;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends Activity {

    TextView test;

    ScoreEntry se;
    DataBaseHandler dbh;

    private List<ScoreEntry> allEntries;
    private List<ScoreEntry> temp;

    RecyclerView recyclerView;
    CustomAdapter_Score customAdapter_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_result);

        test = findViewById(R.id.result_text);

        dbh = new DataBaseHandler(this);


        allEntries = new ArrayList<>();
        temp = new ArrayList<>();

        recyclerView = findViewById(R.id.scoreboard);
        customAdapter_score  = new CustomAdapter_Score(this, allEntries);
        recyclerView.setAdapter(customAdapter_score);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customAdapter_score.setOnItemClickListener(new CustomAdapter_Score.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int gameId = allEntries.get(position).getID();
                showGuessHistory(gameId);
            }
        });

        showLeaderboard();
    }

    public void deleteLeaderboardClick(View view)
    {
        dbh.deleteAllEntries();
        showLeaderboard();
    }

    public void refreshLeaderboardClick(View view)
    {
        showLeaderboard();
    }


    public void showLeaderboard()
    {
        temp = dbh.getAllEntries();
        allEntries.clear();
        for (int i = 0 ; i < temp.size(); i ++)
        {
            allEntries.add(temp.get(i));
        }

        customAdapter_score.notifyDataSetChanged();
    }

    // testavimas
    public void addEntryClick(View view)
    {
        se = new ScoreEntry(0, "Joe" , 123, 2);
        dbh.addEntry(se);
    }

    //pereinama į viešą rezultatų suvestinę
    public void goToLeaderBoardClick(View view)
    {
        Intent intent = new Intent(this,  ScoreboardActivity.class);
        startActivity(intent);
    }

    public void showGuessHistory(int GameId){
        Intent intent = new Intent(this, GameGuessesHistory.class);
        intent.putExtra("id", GameId);
        startActivity(intent);
    }
}
