package com.abhaytr.games.jardle;

import javax.swing.JOptionPane;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.imageio.ImageIO;

public class MessageHandler
{
 
    private JFrame appFrame;

    public MessageHandler(JFrame appFrame)
    {
        this.appFrame = appFrame;
    }

    public void showPopupMessage(String title, String infoMessage, PopupOKListener popupOKListener) throws IOException, URISyntaxException
    {
        Object[] options = {"Next Word"};
        int input = JOptionPane.showOptionDialog(this.appFrame, infoMessage, title, JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, new ImageIcon(ImageIO.read(ResourceManager.getResourceForRead(Constants.LOGO_SMALL_FILE_NAME))), options, options[0]);
        if(input == JOptionPane.OK_OPTION)
        {
            if (popupOKListener != null)
            {
                popupOKListener.onOKClick();   
            }
        }
    }

    public void showPopupMessage() throws IOException, URISyntaxException
    {
        Object[] options = {"OK"};
        JOptionPane.showOptionDialog(this.appFrame, "Next word will have " + Variables.currentLevel.getNMinLetters() + " to " + Variables.currentLevel.getNMaxLetters() + " letters.", "Level changed to Level " + Variables.currentLevel.getDifficulty(), JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(ImageIO.read(ResourceManager.getResourceForRead(Constants.LOGO_SMALL_FILE_NAME))), options, options[0]);
    }

    public void showNotSupportedPopupMessage(String title, String infoMessage) throws IOException, URISyntaxException
    {
        Object[] options = {"Copy File Path to Clipboard"};
        int input = JOptionPane.showOptionDialog(this.appFrame, infoMessage, title, JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if(input == 0)
        {
            StringSelection stringSelection = new StringSelection(Constants.getHelpPath());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }

}