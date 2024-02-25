package aoa.guessers;

import aoa.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PAGALetterFreqGuesser implements Guesser {
    private final List<String> words;

    public PAGALetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }

    public List<String> keepOnlyWordsThatMatchPattern(String pattern){
        List<String> wordsThatMatch = new ArrayList<>();
        for (String word : words) {
            List<Boolean> matches = new ArrayList<>();
            if (word.length() == pattern.length()) {
                for (int i = 0; i < word.length(); i++) {
                    char patternChar = pattern.charAt(i);
                    char wordChar = word.charAt(i);
                    if (patternChar != '-') {
                        if (patternChar != wordChar) {
                            matches.add(false);
                        } else {
                            matches.add(true);
                        }
                    } else {
                        if (pattern.contains(String.valueOf(wordChar))) {
                            matches.add(false);
                        }
                    }
                }
            } if (matches.contains(true) && !matches.contains(false)) {
                wordsThatMatch.add(word);
            }
        }
        return wordsThatMatch;
    }

    public List<String> wordsListLeft (char guess, List<String> wordsList) {
        for (String word : words) {
            if (word.indexOf(guess) == -1) {
                wordsList.remove(word);
            }
        }
        return wordsList;
    }

    public Map<Character, Integer> getFrequencyMap(List<String> targetList) {
        Map<Character, Integer> map = new TreeMap<>();
        for (String word : targetList) {
            for (char letter : word.toCharArray()) {
                map.put(letter, map.getOrDefault(letter, 0) + 1);
            }
        }
        return map;
    }

    /** Returns the most common letter in the set of valid words based on the current
     *  PATTERN and the GUESSES that have been made. */
    @Override
    public char getGuess(String pattern, List<Character> guesses) {
        List<String> wordsThatMatchPattern = keepOnlyWordsThatMatchPattern(pattern);
        if (wordsThatMatchPattern.isEmpty()) {
            wordsThatMatchPattern = words;
        }
        for (char guess : guesses) {
            if (pattern.indexOf(guess) == -1) {
                wordsThatMatchPattern.removeIf(word -> word.indexOf(guess) != -1);
            }
        }
        Map<Character, Integer> freqMap = getFrequencyMap(wordsThatMatchPattern);
        return freqMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .filter(character -> !guesses.contains(character))
                .findFirst()
                .orElse('?');
    }

    public static void main(String[] args) {
        PAGALetterFreqGuesser pagalfg = new PAGALetterFreqGuesser("data/example.txt");
        System.out.println(pagalfg.getGuess("----", List.of('e')));
    }
}