package aoa.choosers;

import java.util.*;

import aoa.utils.FileUtils;

public class EvilChooser implements Chooser {
    private String pattern;
    private List<String> wordPool;

    public EvilChooser(int wordLength, String dictionaryFile) {
        if (wordLength > 1) {
            wordPool = FileUtils.readWordsOfLength(dictionaryFile, wordLength);
            if (wordPool.isEmpty()) {
                throw new IllegalStateException("No words of the specified length found.");
            } else {
                pattern = "-".repeat(wordLength);
            }
        } else {
            throw new IllegalArgumentException("Word length should be greater than 1.");
        }
    }

    @Override
    public int makeGuess(char letter) {
        TreeMap<String, List<String>> groupMap = new TreeMap<>();
        for (String word : wordPool) {
            char[] patternArray = pattern.toCharArray();
            for (int i = 0; i < word.length(); i++) {
                if (patternArray[i] == '-') {
                    if (word.charAt(i) != letter) {
                        patternArray[i] = '-';
                    } else {
                        patternArray[i] = letter;
                    }
                }
            }
            String patternString = String.valueOf(patternArray);
            if (groupMap.containsKey(patternString)) {
                groupMap.get(patternString).add(word);
            } else {
                List<String> newSet = new ArrayList<>();
                newSet.add(word);
                groupMap.put(patternString, newSet);
            }
        }

        pattern = groupMap.entrySet()
                .stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()))
                .map(Map.Entry::getKey)
                .get();

        wordPool = groupMap.get(pattern);

        int count = 0;
        for (char c : pattern.toCharArray()) {
            if (c == letter) {
                count++;
            }
        }

        return count;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public String getWord() {
        return wordPool.get(0);
    }
}