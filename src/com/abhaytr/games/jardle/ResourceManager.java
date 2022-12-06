package com.abhaytr.games.jardle;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.net.URISyntaxException;

public class ResourceManager
{

    private static File getRunningFile() throws URISyntaxException
    {
        return new File(ResourceManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
    }

    public static String getRunningPath() throws IOException, URISyntaxException
    {
        return getRunningFile().getAbsolutePath().replace("\\", "/");
    }

    public static InputStream getResourceForRead(String path) throws IOException, URISyntaxException
    {
        return new BufferedInputStream(new FileInputStream(new File(getRunningPath() + "/" + path)));
    }

    public static FileWriter getResourceForWrite(String path) throws IOException, URISyntaxException
    {
        return new FileWriter(getRunningPath() + "/" + path);
    }

}