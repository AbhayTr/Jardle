package com.abhaytr.games.jardle;

import java.io.IOException;
import java.awt.FontFormatException;
import java.net.URISyntaxException;

public interface EventHandler
{
    public void onWordFound() throws IOException, FontFormatException, URISyntaxException;
    public void onWordNotFound() throws IOException, FontFormatException, URISyntaxException;
}