package com.abhaytr.games.jardle;

public class Level1 extends Level
{
    
    public Level1()
    {
        super(Constants.LEVEL1_MIN_LETTERS, Constants.LEVEL1_MAX_LETTERS, Constants.LEVEL1_DIFFICULTY);
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