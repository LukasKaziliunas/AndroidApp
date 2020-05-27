package edu.ktu.example.guessthenumber;

import android.graphics.Bitmap;

/**
 * Created by super on 10/6/2016.
 */
public class ScoreEntry {

    private int mID;
    private String mName;
    private int mScore;
    private int difficulty;

    public ScoreEntry()
    {
        mID = 0;
        mName = "";
        mScore = 0;
        difficulty = 0;
    }

    public ScoreEntry(int id, String name, int score, int diff)
    {
        mID = id;
        mName = name;
        mScore = score;
        difficulty = diff;
    }

    public void setID(int val)
    {
        mID = val;
    }

    public int getID()
    {
        return mID;
    }

    public void setName(String val)
    {
        mName = val;
    }

    public String getName()
    {
        return mName;
    }

    public void setScore(int val)
    {
        mScore = val;
    }

    public int getScore()
    {
        return mScore;
    }

    public void setDifficulty(int val)
    {
        difficulty = val;
    }

    public int getDifficulty()
    {
        return difficulty;
    }

}
