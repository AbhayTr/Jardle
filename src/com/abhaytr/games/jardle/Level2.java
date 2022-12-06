package com.abhaytr.games.jardle;

public class Level2 extends Level
{

    public Level2()
    {
        super(Constants.LEVEL2_MIN_LETTERS, Constants.LEVEL2_MAX_LETTERS, Constants.LEVEL2_DIFFICULTY);
    }

    public int getNMinLetters()
    {
        return this.nMinLetters;
    }

    public int getNMaxLetters()
    {
        return this.nMaxLetters;
    }

    public int getDifficulty()
    {
        return this.difficulty;
    }

}