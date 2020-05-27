package edu.ktu.example.guessthenumber;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class GameActivity extends Activity {

    private static final String PREFERENCES_FILE_NAME = "SettingsPref";
    SharedPreferences settings;
    int difficulty;

    private int minNumber;
    private int maxNumber;
    private int maxTurns;

    private int randomNumber;


    private int currentTurn = 0;

    private int result = 0;
    private int points = 1000;

    private TextView numberRange;
    private TextView resultText;
    private TextView turnsText;

    private EditText numberField;

    //naudoju linked list kad būtų galima naujus įrašus dėti į sąrašo pradžią
    private LinkedList<Integer> hintIcons;
    private LinkedList<String> guessedNumbers;
    RecyclerView recyclerView;
    CustomAdapter_Guess customAdapterGuess;

    ScoreEntry se;
    DataBaseHandler dbh;

    List<GuessEntry> guesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        settings = getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);
        difficulty = settings.getInt("difficulty", 0);

        switch (difficulty)
        {
            case 0 :
                minNumber = 0;
                maxNumber = 10;
                maxTurns = 8;
                break;
            case 1 :
                minNumber = 0;
                maxNumber = 25;
                maxTurns = 7;
                break;
            case 2 :
                minNumber = 0;
                maxNumber = 45;
                maxTurns = 5;
                break;
            default:
                minNumber = 0;
                maxNumber = 10;
                maxTurns = 8;
                break;

        }

        hintIcons = new LinkedList<>();
        guessedNumbers = new LinkedList<>();
        guesses = new ArrayList<>();

        setContentView(R.layout.activity_game);

        numberRange = findViewById(R.id.game_1);
        resultText = findViewById(R.id.game_2);
        turnsText = findViewById(R.id.game_3);

        updateText(0);

        numberField = findViewById(R.id.game_input);

        Random random = new Random();

        randomNumber = random.nextInt(maxNumber - minNumber) + minNumber;
       // randomNumber = 3;

        Button saveButton = findViewById(R.id.game_submit);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                guessClick(v);
            }
        });

        //recyclerview
        recyclerView = findViewById(R.id.rv);
        customAdapterGuess = new CustomAdapter_Guess(this, guessedNumbers, hintIcons);
        recyclerView.setAdapter(customAdapterGuess);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbh = new DataBaseHandler(this);

    }

    //atnaujina tekstą ekrane priklausomai nuo to koks prieš tai buvo spėjimas
    private void updateText(int guessedNumber)
    {
        numberRange.setText(String.format(getResources().getString(R.string.number_range_format), minNumber, maxNumber));
        if(result > 0)
        {
            resultText.setText(String.format(getResources().getString(R.string.result_format), guessedNumber , getResources().getString(R.string.result_high)));
        }
        else if(result < 0)
        {
            resultText.setText(String.format(getResources().getString(R.string.result_format), guessedNumber , getResources().getString(R.string.result_low)));
        }
        else
        {
            resultText.setText("");
        }

        turnsText.setText(String.format(getResources().getString(R.string.turns_format), currentTurn, maxTurns));
    }

    //paspausta spėti
    public void guessClick(View view)
    {
        GuessEntry guess;
        currentTurn++;
        int guessNumber = Integer.parseInt(numberField.getText().toString());


        //tikrinamas spėjimas
        if(randomNumber > guessNumber)
        {
            result = -1;
            addGuessHistoryRow(guessNumber, true);
            points = points - 10 * currentTurn;
        }
        else if (randomNumber < guessNumber)
        {
            result = 1;
            addGuessHistoryRow(guessNumber, false);
            points = points - 10 * currentTurn;
        }
        else
        {
            result = 0;
        }


        if(currentTurn >= maxTurns && result != 0)
        {
            //lose
            Intent intent = new Intent(this, GameOver.class);
            intent.putExtra("guessedNumber", guessNumber);
            intent.putExtra("randomNumber", randomNumber);
            intent.putExtra("score", points);
            intent.putExtra("win", false);
            startActivity(intent);
            finish();
        }
        else if(result == 0)
        {
            //win
            Intent intent = new Intent(this, GameOver.class);
            intent.putExtra("guessedNumber", guessNumber);
            intent.putExtra("randomNumber", randomNumber);
            intent.putExtra("score", points);
            intent.putExtra("difficulty", difficulty);
            intent.putExtra("win", true);

            //į lokalę duomenų bazę įdadamas žaidimo rezultatas ir gaunamas šio įrašo id
            se = new ScoreEntry(0, settings.getString("playerName", "no_name") , points, difficulty);
            int gameId = (int)dbh.addEntry(se);

            guess = new GuessEntry(R.drawable.trophy, guessNumber);
            guesses.add(guess);

            //į duomenų bazę sudedami šio žaidimo spėjimai
            for (GuessEntry ge: guesses) {
                dbh.addGuess(ge, gameId);
            }

            startActivity(intent);
            finish();
        }

        updateText(guessNumber);

    }

    public void testClick(View view)
    {
        hintIcons.add(R.drawable.arrow_up);

        guessedNumbers.add("text3 " + currentTurn);

        customAdapterGuess.notifyItemInserted(currentTurn);
    }

    //prideda spėjimą į recycler view ir išsaugau sąraše
    public void addGuessHistoryRow(int number , boolean hintLower)
    {
        GuessEntry guess;

        if(hintLower)
        {
            hintIcons.addFirst(R.drawable.arrow_down);

            guessedNumbers.addFirst(String.valueOf(number));

            customAdapterGuess.notifyItemInserted(0);

            guess = new GuessEntry(R.drawable.arrow_down, number);
            guesses.add(guess);
        }else
        {
            hintIcons.addFirst(R.drawable.arrow_up);

            guessedNumbers.addFirst(String.valueOf(number));

            customAdapterGuess.notifyItemInserted(0);

            guess = new GuessEntry(R.drawable.arrow_up, number);
            guesses.add(guess);
        }
    }

}
