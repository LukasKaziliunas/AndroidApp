package edu.ktu.example.guessthenumber;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
public class GameGuessesHistory extends Activity {

    int id;

    DataBaseHandler dbh;

    private List<GuessEntry> allGuesses;

    private List<String> guessText;
    private List<Integer> images;

    RecyclerView recyclerView;
    CustomAdapter_Guess customAdapterGuess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_guess_history);


        Intent intent = getIntent();

        id = intent.getIntExtra("id", -1);

        images = new ArrayList<>();
        guessText = new ArrayList<>();


        recyclerView = findViewById(R.id.rv_guessHistory);
        customAdapterGuess = new CustomAdapter_Guess(this, guessText, images);
        recyclerView.setAdapter(customAdapterGuess);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbh = new DataBaseHandler(this);

        getGuesses();
    }

    public void getGuesses()
    {
        allGuesses = dbh.getGameGuesses(id);

        for (GuessEntry ge: allGuesses) {
            guessText.add(String.valueOf(ge.GetNumber()));
            images.add(ge.GetHint());
        }

        customAdapterGuess.notifyDataSetChanged();
    }
}
