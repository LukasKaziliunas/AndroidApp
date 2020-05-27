package edu.ktu.example.guessthenumber;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by super on 10/6/2016.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "results.db";

    private static String SCORES_TABLE_NAME = "scores";
    private static String GUESSES_TABLE_NAME = "guesses";

    private final static String KEY_ID = "id";
    private final static String NAME = "name";
    private final static String SCORE = "score";
    private final static String DIFFICULTY = "difficulty";

    public DataBaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  //sukuria db
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + SCORES_TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                NAME + " TEXT," +
                SCORE + " INTEGER," +
                DIFFICULTY + " INTEGER" +
                ")";
        db.execSQL(query); //ivygdo uzklausa

        //antra lentelė spėjimams saugoti
        String query2 = "CREATE TABLE " + GUESSES_TABLE_NAME + "(" +
                "id INTEGER PRIMARY KEY," +
                "number INTEGER," +
                "hint INTEGER," +
                "gameId_fk INTEGER" +
                ")";

        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query = "DROP TABLE IF EXISTS " + SCORES_TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    //pridėti žaidimo rezultatą , parametrai : žaidimo rezultatas
    public long addEntry(ScoreEntry entry)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(NAME, entry.getName());
        cv.put(SCORE, entry.getScore());
        cv.put(DIFFICULTY, entry.getDifficulty());

        long id = db.insert(SCORES_TABLE_NAME, null, cv);
        db.close();
        return id;
    }

    //pridėti žaidimo spėjimą , parametrai : spėjimas , žaidimo id
    public long addGuess(GuessEntry entry, int gameId)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("number", entry.GetNumber());
        cv.put("hint", entry.GetHint());
        cv.put("gameId_fk", gameId);

        long id = db.insert(GUESSES_TABLE_NAME, null, cv);
        db.close();
        return id;
    }

    //gauna žaidimo rezultatą pagal ID
    public ScoreEntry getEntry(int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = null;

        cursor = db.query(SCORES_TABLE_NAME, new String[] { KEY_ID, NAME, SCORE}, KEY_ID + "=?", new String[] { Integer.toString(id) }, null, null, null);

        ScoreEntry entry = new ScoreEntry();
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                entry.setID(cursor.getInt(0));
                entry.setName(cursor.getString(1));
                entry.setScore(cursor.getInt(2));
                entry.setDifficulty(cursor.getInt(3));
            }
        }

        cursor.close();
        db.close();

        return entry;
    }

    //gauna visų žaidimų rezultatai
    public ArrayList<ScoreEntry> getAllEntries()
    {
        ArrayList<ScoreEntry> entries = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + SCORES_TABLE_NAME + " ORDER BY " + SCORE + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    ScoreEntry entry = new ScoreEntry();

                    entry.setID(cursor.getInt(0));
                    entry.setName(cursor.getString(1));
                    entry.setScore(cursor.getInt(2));
                    entry.setDifficulty(cursor.getInt(3));

                    entries.add(entry);
                } while(cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();

        return entries;
    }

    //gauna tam tikro žaidimo visus spėjimus
    public ArrayList<GuessEntry> getGameGuesses(int id)
    {
        ArrayList<GuessEntry> guesses = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + GUESSES_TABLE_NAME + " WHERE gameID_fk = " + String.valueOf(id) +" ORDER BY id ASC";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    GuessEntry guess = new GuessEntry();

                    guess.SetNumber(cursor.getInt(1));
                    guess.SetHint(cursor.getInt(2));


                    guesses.add(guess);
                } while(cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();

        return guesses;
    }

    //ištrina žaidimo rezultatą
    public void deleteEntry(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SCORES_TABLE_NAME, KEY_ID + "=?", new String[]{Integer.toString(id)});
        db.close();
    }

    //ištrina spėjimą
    public void deleteGuess(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(GUESSES_TABLE_NAME, KEY_ID + "=?", new String[]{Integer.toString(id)});
        db.close();
    }

    //ištrina visų žaidimų rezultatus
    public void deleteAllEntries()
    {
        SQLiteDatabase db = getReadableDatabase();

        int id;

        String query = "SELECT * FROM " + SCORES_TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {

                    id = cursor.getInt(0);
                    deleteEntry(id);

                } while(cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();

        deleteAllGuesses();
    }

    //ištrina visų žaidimų visus spėjimus
    public void deleteAllGuesses()
    {
        SQLiteDatabase db = getReadableDatabase();

        int id;

        String query = "SELECT * FROM " + GUESSES_TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {

                    id = cursor.getInt(0);
                    deleteGuess(id);

                } while(cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
    }

    //atnaujina žaidimo rezultatą
    public void updateEntry(ScoreEntry entry)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(NAME, entry.getName());
        cv.put(SCORE, entry.getScore());
        cv.put(DIFFICULTY, entry.getDifficulty());

        db.update(SCORES_TABLE_NAME, cv, KEY_ID + "=?", new String[] { Integer.toString(entry.getID()) });

        db.close();
    }
}
