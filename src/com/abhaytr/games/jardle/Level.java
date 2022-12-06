package com.abhaytr.games.jardle;

public abstract class Level
{
    protected int nMinLetters;
    protected int nMaxLetters;
    protected int difficulty;

    public Level(int nMinLetters, int nMaxLetters, int difficulty)
    {
        this.nMinLetters = nMinLetters;
        this.nMaxLetters = nMaxLetters;
        this.difficulty = difficulty;
    }

    public abstract int getNMinLetters();
    public abstract int getNMaxLetters();
    public abstract int getDifficulty();

}