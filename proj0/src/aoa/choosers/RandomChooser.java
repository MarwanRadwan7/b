package aoa.choosers;

import edu.princeton.cs.algs4.StdRandom;
import aoa.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class RandomChooser implements Chooser {
    private final String chosenWord;
    private String pattern;
    private int wordLength;
    private List<Character> guesses;

    public RandomChooser(int wordLength, String dictionaryFile) {
        this.wordLength = wordLength;
        guesses = new ArrayList<>();
        if (wordLength > 1) {
            List<String> wordsOfLength = FileUtils.readWordsOfLength(dictionaryFile, wordLength);
            if (!wordsOfLength.isEmpty()) {
                int numWords = wordsOfLength.size();
                int randomlyChosenWordNumber = StdRandom.uniform(numWords);
                chosenWord = wordsOfLength.get(randomlyChosenWordNumber);
            } else {
                throw new IllegalStateException("No words of the specified length found.");
            }
        } else {
            throw new IllegalArgumentException("Word length should be greater than 1.");
        }
    }

    @Override
    public int makeGuess(char letter) {
        int num = 0;
        guesses.add(letter);
        for (char w : chosenWord.toCharArray()) {
            if (w == letter) {
                num++;
            }
        }
        return num;
    }

    @Override
    public String getPattern() {
        pattern = "-".repeat(wordLength);
        char[] patternArray = pattern.toCharArray();
        for (char guess : guesses) {
            for (int i = 0; i < chosenWord.length(); i++) {
                char patternChar = pattern.charAt(i);
                char wordChar = chosenWord.charAt(i);
                if (wordChar == guess) {
                    patternArray[i] = guess;
                }
            }
        }
        pattern = String.valueOf(patternArray);
        return pattern;
    }

    @Override
    public String getWord() {
        return chosenWord;
    }
}