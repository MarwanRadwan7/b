package aoa.guessers;

import aoa.utils.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveLetterFreqGuesser implements Guesser {
    private final List<String> words;

    public NaiveLetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }

    /** Makes a guess which ignores the given pattern. */
    @Override
    public char getGuess(String pattern, List<Character> guesses) {
        return getGuess(guesses);
    }

    /** Returns a map from a given letter to its frequency across all words.
     *  This task is similar to something you did in hw0b! */
    public Map<Character, Integer> getFrequencyMap() {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for(String word:words){
            for(char letter: word.toCharArray()){
                frequencyMap.put(letter, frequencyMap.getOrDefault(letter, 0) + 1);
            }
        }
        return frequencyMap;
    }

    /** Returns the most common letter in WORDS that has not yet been guessed
     *  (and therefore isn't present in GUESSES). */
    public char getGuess(List<Character> guesses) {
        Map<Character, Integer> frequencyMap = getFrequencyMap();

//        char mostCommon = ' ' ;
//        int maxOcc = -1;
//
//        for (char letter: frequencyMap.keySet()){
//            int freq = frequencyMap.get(letter);
//            if(freq > maxOcc){
//                maxOcc = freq;
//                mostCommon = letter;
//            }
//        }
//        return mostCommon;

//        Using streams
        return frequencyMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .filter(character -> !guesses.contains(character))
                .findFirst()
                .orElse('?');

    }

    public static void main(String[] args) {
        NaiveLetterFreqGuesser nlfg = new NaiveLetterFreqGuesser("data/example.txt");
        System.out.println("list of words: " + nlfg.words);
        System.out.println("frequency map: " + nlfg.getFrequencyMap());

        List<Character> guesses = List.of('e', 'l');
        System.out.println("guess: " + nlfg.getGuess(guesses));
    }
}
