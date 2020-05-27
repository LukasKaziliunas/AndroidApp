package edu.ktu.example.guessthenumber;
//spėjimo įrašas
public class GuessEntry {

    private int gameId;
    private int hint;
    private int number;

    public GuessEntry(int _hint, int _number)
    {
        this.gameId = -1;
        this.hint = _hint;
        this.number = _number;
    }

    public GuessEntry()
    {
        this.gameId = -1;
        this.hint = 0;
        this.number = 0;
    }

    public int GetGameId()
    {
        return gameId;
    }

    public int GetHint()
    {
        return hint;
    }

    public int GetNumber()
    {
        return number;
    }

    public void SetGameId(int _gameId)
    {
        this.gameId = _gameId;
    }

    public void SetHint(int _hint)
    {
        this.hint = _hint;
    }

    public void SetNumber(int _number)
    {
        this.number = _number;
    }
}
