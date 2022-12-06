package com.abhaytr.games.jardle;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.net.URISyntaxException;

public class Player
{
    
    public Level getPlayerLevel() throws IOException, InvalidPlayerDataException, URISyntaxException
    {
        Level currentLevel = null;
        InputStreamReader dataFile = new InputStreamReader(ResourceManager.getResourceForRead(Constants.PLAYER_DATA_FILE_NAME), "UTF-8");
        BufferedReader dataFileReader = new BufferedReader(dataFile);
        String currentDataLine;
        while ((currentDataLine = dataFileReader.readLine()) != null)
        {
            String[] dataParts = currentDataLine.split(",");
            if (dataParts[0].equals("level"))
            {
                try
                {
                    int levelNumber = Integer.parseInt(dataParts[1]);
                    if (levelNumber == 1)
                    {
                        currentLevel = new Level1();
                    }
                    else if (levelNumber == 2)
                    {
                        currentLevel = new Level2();
                    }
                    else
                    {
                        throw new ArithmeticException();
                    }
                }
                catch (ArithmeticException ex)
                {
                    Constants.closeFile(dataFile, dataFileReader);
                    throw new InvalidPlayerDataException("Player level data corrupted.");
                }
            }
            else
            {
                continue;
            }
        }
        Constants.closeFile(dataFile, dataFileReader);
        return currentLevel;
    }
 
    public void storeLevel(Level level) throws IOException, URISyntaxException
    {
        FileWriter dataWriter = ResourceManager.getResourceForWrite(Constants.PLAYER_DATA_FILE_NAME);
        dataWriter.write("level," + level.getDifficulty());
        Constants.closeFile(dataWriter);
    }

}