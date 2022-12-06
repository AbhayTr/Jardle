package com.abhaytr.games.jardle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.net.URISyntaxException;

public class Constants
{

    static final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    static final int WIDTH = (int) size.getWidth();
    static final int HEIGHT = (int) size.getHeight();

    static final int BOX_SIZE = (int)(Math.max(HEIGHT, WIDTH) * 0.06);

    static final int TOP_UI_HEIGHT = (int)(HEIGHT * 0.15);
    static final int BOTTOM_UI_HEIGHT = (int)(HEIGHT * 0.03);

    static final String DATA_FILE_NAME = "assets/data/datacw.txt";
    static final String WORDS_DATABASE_FILE_NAME = "assets/data/data.txt";
    static final String LOGO_FILE_NAME = "assets/logos/Jardle_Logo.png";
    static final String LOGO_SMALL_FILE_NAME = "assets/logos/logo_small.png";
    static final String FONT_FILE_NAME = "assets/fonts/ABeeZee-Regular.otf";
    static final String CORRECT_AUDIO_FILE_NAME = "assets/audio/correct.wav";
    static final String INCLUDED_AUDIO_FILE_NAME = "assets/audio/included.wav";
    static final String WRONG_AUDIO_FILE_NAME = "assets/audio/wrong.wav";
    static final String NOTWORD_AUDIO_FILE_NAME = "assets/audio/wrong.wav";
    static final String TAP_AUDIO_FILE_NAME = "assets/audio/tap.wav";
    static final String PLAYER_DATA_FILE_NAME = "assets/player/player_data.txt";
    static final String GUIDE_FILE_NAME = "Jardle_Guide.pdf";
    static final String HELP_FILE_PATH = "assets/help";

    static final int ROWS = 6;
    static final int ROW_PADDING = 10;
    static final int BORDER_THICKNESS = 2;

    static final Color GREEN = hex2Rgb("#6aaa64");
    static final Color YELLOW = hex2Rgb("#c9b458");
    static final Color GRAY = hex2Rgb("#787c7e");
    static final Color BORDER_GRAY = hex2Rgb("#d3d6da");
    static final Color WHITE = hex2Rgb("#FFFFFF");

    static final int LEVEL1_DIFFICULTY = 1;
    static final int LEVEL1_MIN_LETTERS = 5;
    static final int LEVEL1_MAX_LETTERS = 7;
    static final int LEVEL2_DIFFICULTY = 2;
    static final int LEVEL2_MIN_LETTERS = 8;
    static final int LEVEL2_MAX_LETTERS = 10;

    static final int DESCRIPTION_SIZE = (int)(Math.max(HEIGHT, WIDTH) * 0.012);
    static final int CREDITS_SIZE = (int)(Math.max(HEIGHT, WIDTH) * 0.0095);
    static
    {
        System.out.println(DESCRIPTION_SIZE + " " + CREDITS_SIZE);
    }

    private static Color hex2Rgb(String hexCode)
    {
        return new Color
        (
                Integer.valueOf(hexCode.substring( 1, 3 ), 16),
                Integer.valueOf(hexCode.substring( 3, 5 ), 16),
                Integer.valueOf(hexCode.substring( 5, 7 ), 16)
        );
    }

    public static void closeFile(InputStreamReader dataFile, BufferedReader dataFileReader) throws IOException
    {
        dataFileReader.close();
        dataFile.close();
    }

    public static void closeFile(FileWriter fileWriter) throws IOException
    {
        fileWriter.close();
    }

    public static void throwException(Exception ex)
    {
        System.out.println("The following error occured while running the App: " + ex.getMessage());
    }

    public static String getHelpPath() throws IOException, URISyntaxException
    {
        return ResourceManager.getRunningPath() + "/" + HELP_FILE_PATH + "/" + GUIDE_FILE_NAME;
    }

}