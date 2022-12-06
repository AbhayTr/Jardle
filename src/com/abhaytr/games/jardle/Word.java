package com.abhaytr.games.jardle;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Word
{

    private String word;
    
    public Word(String word)
    {
        this.word = word.toUpperCase();
    }

    public String getWord()
    {
        return word;
    }

    public int wordLength()
    {
        return word.length();
    }

    public boolean isTheWord(String enteredWord)
    {
        return enteredWord.equals(this.word);
    }

    public Map<String, int[]> getCharPositions(String enteredWord)
    {
        List<Character> wordChars = new ArrayList<Character>();
        for (char cw : this.word.toCharArray())
        {
            wordChars.add(cw);
        }
        Map<String, int[]> charsPositions = new HashMap<String, int[]>();
        int[] includedCharsPostion = new int[enteredWord.length()];
        int[] matchedCharsPostion = new int[enteredWord.length()];
        int posi = 0;
        int posm = 0;
        for (int i = 0; i < enteredWord.length(); i++)
        {
            char c = enteredWord.charAt(i);
            if (wordChars.contains(c))
            {
                for (int j = 0; j < wordChars.size(); j++)
                {
                    if (wordChars.get(j) == c)
                    {
                        wordChars.remove(j);
                        break;
                    }
                }
                if (this.word.charAt(i) == c)
                {
                    matchedCharsPostion[posm] = i + 1;
                    posm++;
                    continue;
                }
                includedCharsPostion[posi] = i + 1;
                posi++;
            }
            else
            {
                if (this.word.charAt(i) == c)
                {
                    for (int k = 0; k < includedCharsPostion.length; k++)
                    {
                        if (includedCharsPostion[k] == 0)
                        {
                            continue;
                        }
                        if (enteredWord.charAt(includedCharsPostion[k] - 1) == c)
                        {
                            deleteElement(includedCharsPostion, k);
                            posi--;
                            matchedCharsPostion[posm] = i + 1;
                            posm++;
                            break;
                        }
                    }
                }
            }
        }
        charsPositions.put("included", includedCharsPostion);
        charsPositions.put("matched", matchedCharsPostion);
        return charsPositions;
    }

    private void deleteElement(int[] arr, int index)
    {
        for (int i = index; i < arr.length - 1; i++)
        {
            arr[i] = arr[i + 1];
        }
    }

}