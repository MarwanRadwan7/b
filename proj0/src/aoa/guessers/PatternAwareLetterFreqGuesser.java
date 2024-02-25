package aoa.guessers;

import aoa.utils.FileUtils;

import java.util.*;

public class PatternAwareLetterFreqGuesser implements Guesser {
    private final List<String> words;

    public PatternAwareLetterFreqGuesser(String dictionaryFile) {
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
                    }
                }
            } if (matches.contains(true) && !matches.contains(false)) {
                wordsThatMatch.add(word);
            }
        }
        return wordsThatMatch;
    }

    public Map<Character, Integer> getFrequencyMap(List<String> wordsThatMatchPattern) {
        Map<Character, Integer> map = new TreeMap<>();
        for (String word : wordsThatMatchPattern) {
            for (char letter : word.toCharArray()) {
                map.put(letter, map.getOrDefault(letter, 0) + 1);
            }
        }
        return map;
    }

    /** Returns the most common letter in the set of valid words based on the current
     *  PATTERN. */
    @Override
    public char getGuess(String pattern, List<Character> guesses) {
        List<String> keepOnlyWordsThatMatchPattern = keepOnlyWordsThatMatchPattern(pattern);
        if (keepOnlyWordsThatMatchPattern.isEmpty()) {
            keepOnlyWordsThatMatchPattern = words;
        }
        Map<Character, Integer> freqMap = getFrequencyMap(keepOnlyWordsThatMatchPattern);
        return freqMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .filter(character -> !guesses.contains(character))
                .findFirst()
                .orElse('?');
    }

    public static void main(String[] args) {
        PatternAwareLetterFreqGuesser palfg = new PatternAwareLetterFreqGuesser("data/example.txt");
        System.out.println(palfg.getGuess("-e--", List.of('e')));
    }
}