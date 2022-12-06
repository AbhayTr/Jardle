package com.abhaytr.games.jardle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Desktop;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.net.URISyntaxException;

public class App
{

    static private int currentWidth = 0;
    static private JPanel topUI;
    static private JComboBox<String> levelSelector;
    static private MessageHandler messageHandler;
    static private Player player;

    public static void main(String args[]) throws IOException, URISyntaxException, InvalidPlayerDataException
    {
        try
        {
            //Initialising player and level objects.
            player = new Player();
            Variables.currentLevel = player.getPlayerLevel();

            //Configuring the Main App frame.
            JFrame mainApp = new JFrame("Jardle by Abhay Tripathi");
            mainApp.setLayout(null);
            mainApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainApp.setResizable(false);
            mainApp.getContentPane().setBackground(Constants.WHITE);
            mainApp.setIconImage(new ImageIcon(ImageIO.read(ResourceManager.getResourceForRead(Constants.LOGO_SMALL_FILE_NAME))).getImage());

            //Initialising Popup Message Handler.
            messageHandler = new MessageHandler(mainApp);

            //Creating the components of the Main App Frame.
            topUI = new JPanel();
            topUI.setLayout(new BoxLayout(topUI, BoxLayout.Y_AXIS));
            topUI.setBackground(Constants.WHITE);
            JPanel jardleLogo = new JPanel();
            jardleLogo.setBackground(Constants.WHITE);
            jardleLogo.setLayout(new BoxLayout(jardleLogo, BoxLayout.Y_AXIS));
            ImageIcon logoIcon = new ImageIcon(ImageIO.read(ResourceManager.getResourceForRead(Constants.LOGO_FILE_NAME)));
            JLabel creditsTop = new JLabel("by Abhay Tripathi");
            Font font = Font.createFont(Font.TRUETYPE_FONT, ResourceManager.getResourceForRead(Constants.FONT_FILE_NAME));
            creditsTop.setFont(font.deriveFont(Font.BOLD, 16));
            jardleLogo.add(new JLabel(logoIcon));
            jardleLogo.add(creditsTop);
            JPanel logoPanel = new JPanel();
            logoPanel.setBackground(Constants.WHITE);
            logoPanel.add(jardleLogo);
            topUI.add(logoPanel);
            JLabel descriptionLabel = new JLabel("To know how to play, ");
            descriptionLabel.setFont(font.deriveFont(Font.PLAIN, Constants.DESCRIPTION_SIZE));
            JLabel instructionsButton = new JLabel("click here");
            instructionsButton.setFont(font.deriveFont(Font.PLAIN, Constants.DESCRIPTION_SIZE));
            Color instructionsButtonColor = instructionsButton.getForeground();
            instructionsButton.addMouseListener(new MouseAdapter()
            {

                @Override
                public void mouseClicked(MouseEvent e)
                {
                    try
                    {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
                        {
                            Desktop.getDesktop().open(new File(Constants.getHelpPath()));
                        }
                        else
                        {
                            throw new PlatformNotSupportedException("Your platform unfortunately does not support this feature. To get instructuctions on how to play the game, please open '" + Constants.GUIDE_FILE_NAME + "' which is located in the folder '" + ResourceManager.getRunningPath() + "/" + Constants.HELP_FILE_PATH + "'.");
                        }
                    }
                    catch (URISyntaxException urise)
                    {
                        Constants.throwException(urise);
                    }
                    catch (IOException ioex)
                    {
                        Constants.throwException(ioex);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent me)
                {
                    instructionsButton.setForeground(Constants.YELLOW);
                    instructionsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent me)
                {
                    instructionsButton.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    instructionsButton.setForeground(instructionsButtonColor);
                }

            });
            JLabel fsLabel = new JLabel(".");
            fsLabel.setFont(font.deriveFont(Font.PLAIN, Constants.DESCRIPTION_SIZE));
            JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            descriptionPanel.setBorder(new EmptyBorder(10, Constants.ROW_PADDING, 0, 0));
            descriptionPanel.setBackground(Constants.WHITE);
            descriptionPanel.add(descriptionLabel);
            descriptionPanel.add(instructionsButton);
            descriptionPanel.add(fsLabel);
            topUI.add(descriptionPanel);
            String[] choices = {"<html><p>Level 1<br>(" + Constants.LEVEL1_MIN_LETTERS + " to " + Constants.LEVEL1_MAX_LETTERS + "<br>letter words)</p></html>", "<html><p>Level 2<br>(" + Constants.LEVEL2_MIN_LETTERS + " to " + Constants.LEVEL2_MAX_LETTERS + "<br>letter words)</p></html>"};
            levelSelector = new JComboBox<String>(choices);
            levelSelector.setLightWeightPopupEnabled(false);
            levelSelector.setBackground(Constants.WHITE);
            levelSelector.setSelectedIndex(Variables.currentLevel.getDifficulty() - 1);
            levelSelector.addItemListener(new ItemListener()
            {

                @Override
                public void itemStateChanged(ItemEvent e)
                {
                    if ((e.getStateChange() == ItemEvent.SELECTED))
                    {
                        int selection = levelSelector.getSelectedIndex();
                        if (selection == 0)
                        {
                            Variables.currentLevel = new Level1();
                        }
                        else if (selection == 1)
                        {
                            Variables.currentLevel = new Level2();
                        }
                        try
                        {
                            player.storeLevel(Variables.currentLevel);
                        }
                        catch (IOException ex)
                        {
                            Constants.throwException(ex);
                        }
                        catch (URISyntaxException urise)
                        {
                            Constants.throwException(urise);
                        }
                        try
                        {
                            messageHandler.showPopupMessage();
                        }
                        catch (IOException ex)
                        {
                            Constants.throwException(ex);
                        }
                        catch (URISyntaxException urise)
                        {
                            Constants.throwException(urise);
                        }
                    }
                }

            });
            mainApp.add(topUI);
            mainApp.add(levelSelector);
            RowHandler rowHandler = new RowHandler(Constants.BOX_SIZE, mainApp);

            //Adding the key listener to the window.
            levelSelector.addKeyListener(new KeyListener()
            {

                @Override
                public void keyTyped(KeyEvent e){}
        
                @Override
                public void keyPressed(KeyEvent e)
                {
                    try
                    {
                        rowHandler.handleKeyPress(e.getKeyChar());
                    }
                    catch (InterruptedException ex)
                    {
                        Constants.throwException(ex);
                    }
                    catch (IOException iex)
                    {
                        Constants.throwException(iex);
                    }
                    catch (URISyntaxException urise)
                    {
                        Constants.throwException(urise);
                    }
                }
        
                @Override
                public void keyReleased(KeyEvent e){}

            });
        }
        catch (PlatformNotSupportedException pnsex)
        {
            messageHandler.showNotSupportedPopupMessage("Error opening help file.", pnsex.getMessage());
        }
        catch (Exception ex)
        {
            Constants.throwException(ex);
        }
    }

    public static void setAppWindowSize(JFrame mainApp, RowHandler rowHandler)
    {
        int width = Constants.ROW_PADDING + rowHandler.getGridWidth() + Constants.ROW_PADDING;
        int height = Constants.TOP_UI_HEIGHT + Constants.ROW_PADDING + rowHandler.getGridHeight() + Constants.ROW_PADDING + Constants.BOTTOM_UI_HEIGHT;
        currentWidth = width;
        levelSelector.setBounds(10, 10, (int)(currentWidth * 0.2), levelSelector.getPreferredSize().height);
        topUI.setSize(currentWidth, Constants.TOP_UI_HEIGHT);
        mainApp.getContentPane().setPreferredSize(new Dimension(width, height));
        mainApp.pack();
    }

}