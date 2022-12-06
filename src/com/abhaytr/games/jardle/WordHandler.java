package com.abhaytr.games.jardle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.net.URISyntaxException;

public class WordHandler
{
    
    private WordDatabase wordDatabase;
    private ArrayList<Word> doneWords;
    private Word currentWord;

    public WordHandler(WordDatabase wordDatabase)
    {
        this.doneWords = new ArrayList<Word>();
        this.wordDatabase = wordDatabase;
    }

    public Word getWord() throws IOException, URISyntaxException
    {
        int randomNum = ThreadLocalRandom.current().nextInt(0, this.wordDatabase.getLength());
        String newWord = this.wordDatabase.getIthWord(randomNum);
        while (newWord.equals("[X]") || doneWords.contains(new Word(newWord)) || newWord.length() < Variables.currentLevel.getNMinLetters() || newWord.length() > Variables.currentLevel.getNMaxLetters())
        {
            randomNum = ThreadLocalRandom.current().nextInt(0, this.wordDatabase.getLength());
            newWord = this.wordDatabase.getIthWord(randomNum);
        }
        this.currentWord = new Word(newWord);
        return this.currentWord;
    }

    public void wordDone()
    {
        this.doneWords.add(this.currentWord);
        this.currentWord = null;
    }

}