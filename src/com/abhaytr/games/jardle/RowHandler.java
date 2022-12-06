package com.abhaytr.games.jardle;

import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.MatteBorder;
import javax.swing.border.Border;
import java.awt.Font;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.net.URISyntaxException;
import java.awt.FontFormatException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;

public class RowHandler implements EventHandler
{

    private JFrame appFrame;
    private JLabel[][] boxes;
    private int columns;
    private int size;
    private int currentRow;
    private int currentColumn;
    private String currentWord;
    private Word currentWordToFind;
    private boolean wordFound = false;
    private Clip correctClip;
    private Clip includedClip;
    private Clip wrongClip;
    private Clip notwordClip;
    private Clip tapClip;
    private boolean isRowUpdating = false;
    private WordDatabase wordDatabase;
    private WordHandler wordHandler;
    private MessageHandler messageHandler;
    private JLabel creditsBottom;

    public RowHandler(int size, JFrame appFrame) throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException, URISyntaxException
    {
        this.appFrame = appFrame;
        this.currentRow = 0;
        this.currentColumn = 0;
        this.currentWord = "";
        this.size = size;
        this.wordDatabase = new WordDatabase();
        this.wordHandler = new WordHandler(this.wordDatabase);
        this.messageHandler = new MessageHandler(this.appFrame);
        this.creditsBottom = new JLabel("<html><p>&copy; Abhay Tripathi (Jardle inspired by &copy; New York Time's Wordle)</p></html>");
        Font font = Font.createFont(Font.TRUETYPE_FONT, ResourceManager.getResourceForRead(Constants.FONT_FILE_NAME));
        this.creditsBottom.setFont(font.deriveFont(Font.BOLD, Constants.CREDITS_SIZE));
        getWord();
        AudioInputStream correctAudioInputStream = AudioSystem.getAudioInputStream(ResourceManager.getResourceForRead(Constants.CORRECT_AUDIO_FILE_NAME));
        this.correctClip = AudioSystem.getClip();
        this.correctClip.addLineListener(new LineListener()
        {

            @Override
            public void update(LineEvent e)
            {
                if (e.getType() == LineEvent.Type.STOP)
                {
                    synchronized(RowHandler.this.correctClip)
                    {
                        RowHandler.this.correctClip.notifyAll();
                    }
                }

            }

        });
        this.correctClip.open(correctAudioInputStream);
        AudioInputStream includedAudioInputStream = AudioSystem.getAudioInputStream(ResourceManager.getResourceForRead(Constants.INCLUDED_AUDIO_FILE_NAME));
        this.includedClip = AudioSystem.getClip();
        this.includedClip.addLineListener(new LineListener()
        {

            @Override
            public void update(LineEvent e)
            {
                if (e.getType() == LineEvent.Type.STOP)
                {
                    synchronized(RowHandler.this.includedClip)
                    {
                        RowHandler.this.includedClip.notifyAll();
                    }
                }

            }

        });
        this.includedClip.open(includedAudioInputStream);
        AudioInputStream wrongAudioInputStream = AudioSystem.getAudioInputStream(ResourceManager.getResourceForRead(Constants.WRONG_AUDIO_FILE_NAME));
        this.wrongClip = AudioSystem.getClip();
        this.wrongClip.addLineListener(new LineListener()
        {

            @Override
            public void update(LineEvent e)
            {
                if (e.getType() == LineEvent.Type.STOP)
                {
                    synchronized(RowHandler.this.wrongClip)
                    {
                        RowHandler.this.wrongClip.notifyAll();
                    }
                }

            }

        });
        this.wrongClip.open(wrongAudioInputStream);
        AudioInputStream notwordAudioInputStream = AudioSystem.getAudioInputStream(ResourceManager.getResourceForRead(Constants.NOTWORD_AUDIO_FILE_NAME));
        this.notwordClip = AudioSystem.getClip();
        this.notwordClip.addLineListener(new LineListener()
        {

            @Override
            public void update(LineEvent e)
            {
                if (e.getType() == LineEvent.Type.STOP)
                {
                    synchronized(RowHandler.this.notwordClip)
                    {
                        RowHandler.this.notwordClip.notifyAll();
                    }
                }

            }

        });
        this.notwordClip.open(notwordAudioInputStream);
        AudioInputStream tapAudioInputStream = AudioSystem.getAudioInputStream(ResourceManager.getResourceForRead(Constants.TAP_AUDIO_FILE_NAME));
        this.tapClip = AudioSystem.getClip();
        this.tapClip.addLineListener(new LineListener()
        {

            @Override
            public void update(LineEvent e)
            {
                if (e.getType() == LineEvent.Type.STOP)
                {
                    synchronized(RowHandler.this.tapClip)
                    {
                        RowHandler.this.tapClip.notifyAll();
                    }
                }

            }

        });
        this.tapClip.open(tapAudioInputStream);
        constructRows();
    }

    private void getWord() throws IOException, URISyntaxException
    {
        if (this.boxes != null)
        {
            for (int i = 0; i < Constants.ROWS; i++)
            {
                for (int j = 0; j < this.columns; j++)
                {
                    this.appFrame.remove(this.boxes[i][j]);
                }
            }
            this.appFrame.remove(this.creditsBottom);
        }
        this.appFrame.revalidate();
        this.currentWordToFind = this.wordHandler.getWord();
        this.columns = currentWordToFind.wordLength();
    }

    private void constructRows() throws IOException, FontFormatException, URISyntaxException
    {
        this.currentColumn = 0;
        this.currentRow = 0;
        this.appFrame.setVisible(false);
        int x = Constants.ROW_PADDING;
        int y = Constants.TOP_UI_HEIGHT + Constants.ROW_PADDING;
        this.boxes = new JLabel[Constants.ROWS][columns];
        for (int i = 0; i < Constants.ROWS; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                JLabel box = new JLabel("", SwingConstants.CENTER);
                box.setBounds(x, y, size, size);
                Border border = new MatteBorder(Constants.BORDER_THICKNESS, Constants.BORDER_THICKNESS, Constants.BORDER_THICKNESS, Constants.BORDER_THICKNESS, Constants.BORDER_GRAY);
                box.setBorder(border);
                Font font = Font.createFont(Font.TRUETYPE_FONT, ResourceManager.getResourceForRead(Constants.FONT_FILE_NAME));
                box.setFont(font.deriveFont(Font.PLAIN, size - 2));
                box.setOpaque(true);
                box.setBackground(Constants.WHITE);
                this.boxes[i][j] = box;
                this.appFrame.add(box);
                x += size + Constants.ROW_PADDING;
            }
            y += size + Constants.ROW_PADDING;
            x = Constants.ROW_PADDING;
        }
        resizeApp();
        this.creditsBottom.setBounds(Constants.ROW_PADDING, Constants.TOP_UI_HEIGHT + getGridHeight() + (int)(Constants.BOTTOM_UI_HEIGHT * 0.6), Constants.ROW_PADDING + getGridWidth() + Constants.ROW_PADDING, this.creditsBottom.getPreferredSize().height);
        this.appFrame.add(this.creditsBottom);
        this.appFrame.setVisible(true);
    }

    private void resetCurrentWord()
    {
        RowHandler.this.currentWord = "";
    }

    private void newWord() throws IOException, FontFormatException, URISyntaxException
    {
        this.wordHandler.wordDone();
        getWord();
        constructRows();
    }

    @Override
    public void onWordFound() throws IOException, FontFormatException, URISyntaxException
    {
        this.wordFound = false;
        newWord();
    }

    @Override
    public void onWordNotFound() throws IOException, FontFormatException, URISyntaxException
    {
        newWord();
    }

    public int getColumns()
    {
        return this.columns;
    }

    private void resizeApp()
    {
        App.setAppWindowSize(this.appFrame, this);
    }

    public JLabel[][] getRows()
    {
        return this.boxes;
    }

    public int getGridWidth()
    {
        return ((this.size * this.columns) + (Constants.ROW_PADDING * (this.columns - 1)));
    }

    public int getGridHeight()
    {
        return ((this.size * Constants.ROWS) + (Constants.ROW_PADDING * (Constants.ROWS - 1)));
    }

    public void handleKeyPress(char characterEntered) throws InterruptedException, IOException, URISyntaxException
    {
        if (this.isRowUpdating || this.wordFound || this.currentRow == Constants.ROWS)
        {
            return;
        }
        characterEntered = Character.toUpperCase(characterEntered);
        int asciiCharacterEntered = (int) characterEntered;
        if (asciiCharacterEntered == 8)
        {
            if (this.currentColumn == 0)
            {
                return;
            }
            this.currentColumn--;
            this.boxes[currentRow][currentColumn].setText("");
            this.currentWord = this.currentWord.substring(0, this.currentWord.length() - 1);
        }
        if (asciiCharacterEntered < 65 || asciiCharacterEntered > 90)
        {
            if (asciiCharacterEntered != 10)
            {
                return;
            }
        }
        if (this.currentColumn == this.columns)
        {
            if (asciiCharacterEntered != 10)
            {
                return;
            }
            if (!this.wordDatabase.isAsWord(currentWord))
            {
                playNotWordSound();
                return;
            }
            if (this.currentWordToFind.isTheWord(this.currentWord))
            {
                processRow();
                return;
            }
            Map<String, int[]> charsPosition = this.currentWordToFind.getCharPositions(this.currentWord);
            Map<Integer, Character> rowData = new HashMap<Integer, Character>();
            for (int pos : charsPosition.get("included"))
            {
                rowData.put(pos, 'I');
            }
            for (int pos : charsPosition.get("matched"))
            {
                rowData.put(pos, 'M');
            }
            processRow(rowData);
            return;
        }
        if (asciiCharacterEntered == 10)
        {
            return;
        }
        this.currentWord += characterEntered;
        this.boxes[this.currentRow][this.currentColumn].setText(Character.toString(characterEntered));
        this.currentColumn++;
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    playTapSound();
                }
                catch (InterruptedException ex)
                {
                    Constants.throwException(ex);           
                }
            }

        }).start();
    }

    private void playCorrectSound() throws InterruptedException
    {
        this.correctClip.start();
        while (true)
        {
            synchronized(this.correctClip)
            {
                this.correctClip.wait();
            }
            if (!this.correctClip.isRunning())
            {
                break;
            }
        }
        this.correctClip.setFramePosition(0);
    }

    private void playIncludedSound() throws InterruptedException
    {
        this.includedClip.start();
        while (true)
        {
            synchronized(this.includedClip)
            {
                this.includedClip.wait();
            }
            if (!this.includedClip.isRunning())
            {
                break;
            }
        }
        this.includedClip.setFramePosition(0);
    }

    private void playWrongSound() throws InterruptedException
    {
        this.wrongClip.start();
        while (true)
        {
            synchronized(this.wrongClip)
            {
                this.wrongClip.wait();
            }
            if (!this.wrongClip.isRunning())
            {
                break;
            }
        }
        this.wrongClip.setFramePosition(0);
    }

    private void playNotWordSound() throws InterruptedException
    {
        this.notwordClip.start();
        while (true)
        {
            synchronized(this.notwordClip)
            {
                this.notwordClip.wait();
            }
            if (!this.notwordClip.isRunning())
            {
                break;
            }
        }
        this.notwordClip.setFramePosition(0);
    }

    private void playTapSound() throws InterruptedException
    {
        this.tapClip.start();
        while (true)
        {
            synchronized(this.tapClip)
            {
                this.tapClip.wait();
            }
            if (!this.tapClip.isRunning())
            {
                break;
            }
        }
        this.tapClip.setFramePosition(0);
    }

    private void processRow(Map<Integer, Character> rowData) throws InterruptedException
    {
        this.isRowUpdating = true;
        SwingWorker<String, Integer> rowWorker = new SwingWorker<String, Integer>()
        {

            @Override
            protected String doInBackground() throws Exception
            {
  
                for (int i = 1; i <= RowHandler.this.columns; i++)
                {
                    publish(i);
                    try
                    {
                        char boxState = rowData.get(i);
                        if (boxState == 'I')
                        {
                            playIncludedSound();
                        }
                        else if (boxState == 'M')
                        {
                            playCorrectSound();
                        }
                    }
                    catch (NullPointerException ex)
                    {
                        playWrongSound();
                    }
                    catch (IndexOutOfBoundsException iobex){}
                }
                return "0";
            }
  
            @Override
            protected void process(List<Integer> chunks)
            {
                int i = (int) chunks.get(chunks.size() - 1);
                try
                {
                    RowHandler.this.boxes[RowHandler.this.currentRow][i - 1].setForeground(Constants.WHITE);
                    char boxState = rowData.get(i);
                    if (boxState == 'I')
                    {
                        RowHandler.this.boxes[RowHandler.this.currentRow][i - 1].setBackground(Constants.YELLOW);
                    }
                    else if (boxState == 'M')
                    {
                        RowHandler.this.boxes[RowHandler.this.currentRow][i - 1].setBackground(Constants.GREEN);
                    }
                    else
                    {
                        RowHandler.this.boxes[RowHandler.this.currentRow][i - 1].setBackground(Constants.GRAY);
                    }
                }
                catch (NullPointerException ex)
                {
                    RowHandler.this.boxes[RowHandler.this.currentRow][i - 1].setBackground(Constants.GRAY);
                }
                catch (IndexOutOfBoundsException iobex){}
            }
  
            @Override
            protected void done()
            {
                resetCurrentWord();
                RowHandler.this.currentRow++;
                RowHandler.this.currentColumn = 0;
                RowHandler.this.isRowUpdating = false;
                if (RowHandler.this.currentRow == Constants.ROWS)
                {
                    try
                    {
                        RowHandler.this.messageHandler.showPopupMessage("Better Luck Next Time!", "The word was '" + RowHandler.this.currentWordToFind.getWord() + "'. Better luck in the next attempt!", new PopupOKListener()
                        {

                            @Override
                            public void onOKClick()
                            {
                                try
                                {
                                    onWordNotFound();
                                }
                                catch (IOException ex)
                                {
                                    Constants.throwException(ex);
                                }
                                catch (FontFormatException ffex)
                                {
                                    Constants.throwException(ffex);
                                }
                                catch (URISyntaxException urise)
                                {
                                    Constants.throwException(urise);
                                }
                            }

                        });
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

        };
        rowWorker.execute();
    }

    private void processRow()
    {
        this.isRowUpdating = true;
        SwingWorker<String, Integer> rowWorker = new SwingWorker<String, Integer>()
        {

            @Override
            protected String doInBackground() throws Exception
            {

                for (int i = 0; i < RowHandler.this.columns; i++)
                {
                    publish(i);
                    playCorrectSound();
                }
                return "1";
            }
  
            @Override
            protected void process(List<Integer> chunks)
            {
                int i = (int) chunks.get(chunks.size() - 1);
                RowHandler.this.boxes[RowHandler.this.currentRow][i].setForeground(Constants.WHITE);
                RowHandler.this.boxes[RowHandler.this.currentRow][i].setBackground(Constants.GREEN);
            }
  
            @Override
            protected void done()
            {
                resetCurrentWord();
                RowHandler.this.wordFound = true;
                RowHandler.this.isRowUpdating = false;
                try
                {
                    RowHandler.this.messageHandler.showPopupMessage("Congragulations!", "You guessed the word!", new PopupOKListener()
                    {
                        
                        @Override
                        public void onOKClick()
                        {
                            try
                            {
                                onWordFound();
                            }
                            catch (IOException ex)
                            {
                                Constants.throwException(ex);
                            }
                            catch (FontFormatException ffex)
                            {
                                Constants.throwException(ffex);
                            }
                            catch (URISyntaxException urise)
                            {
                                Constants.throwException(urise);
                            }
                        }

                    });
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

        };
        rowWorker.execute();
    }

}