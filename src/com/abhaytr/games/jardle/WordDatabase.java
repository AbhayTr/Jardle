package com.abhaytr.games.jardle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class WordDatabase
{

    private int databaseLength = 0;

    public WordDatabase() throws IOException, URISyntaxException
    {
        InputStreamReader dataFile = new InputStreamReader(ResourceManager.getResourceForRead(Constants.DATA_FILE_NAME), "UTF-8");
        BufferedReader dataFileReader = new BufferedReader(dataFile);
        while (dataFileReader.readLine() != null)
        {
            this.databaseLength++;
        }
        dataFileReader.close();
        dataFile.close();
    }

    public int getLength()
    {
        return this.databaseLength;
    }

    private boolean isWord(String string)
    {
        boolean isAWord = true;
        for (int i = 0; i < string.length(); i++)
        {
            char c = string.charAt(i);
            c = Character.toUpperCase(c);
            int asciiC = (int) c;
            if (asciiC < 65 || asciiC > 90)
            {
                isAWord = false;
                break;
            }
        }
        return isAWord;
    }

    public boolean isAsWord(String word) throws IOException, URISyntaxException
    {
        InputStreamReader dataFile = new InputStreamReader(ResourceManager.getResourceForRead(Constants.WORDS_DATABASE_FILE_NAME), "UTF-8");
        BufferedReader dataFileReader = new BufferedReader(dataFile);
        String currentWord;
        while ((currentWord = dataFileReader.readLine()) != null)
        {
            if (word.toUpperCase().equals(currentWord.toUpperCase()))
            {
                Constants.closeFile(dataFile, dataFileReader);
                return true;
            }
        }
        Constants.closeFile(dataFile, dataFileReader);
        return false;
    }

    public String getIthWord(int i) throws IOException, URISyntaxException
    {
        InputStreamReader dataFile = new InputStreamReader(ResourceManager.getResourceForRead(Constants.DATA_FILE_NAME), "UTF-8");
        BufferedReader dataFileReader = new BufferedReader(dataFile);
        String currentWord;
        int pos = 0;
        while ((currentWord = dataFileReader.readLine()) != null)
        {
            if (pos == i)
            {
                if (isWord(currentWord))
                {
                    return currentWord;                   
                }
                else
                {
                    return "[X]";
                }
            }
            pos++;
        }
        dataFileReader.close();
        dataFile.close();
        return null;
    }

}