package avlmap;

import java.util.*;
/**
 * This class encapsulates the AI that cheats at hangman.
 * It also encapsulates the game state for Wheel of Lies.
 * @author Edward Oh
 * @author Chen Luo
 */

public class CheatingExecutioner implements Executioner {

    private char invalidChar;
    private String secretWord;
    private int guessesRemaining;
    private ULTreeMap<Character, Integer> guessedLetters;
    private Set<String> possibleWords;

    /**
     * Default constructor for Cheating Executioner.
     */
    public CheatingExecutioner() {
        this.guessedLetters = new ULTreeMap<>();
    }

    /**
     * Prepares the executioner for a new game with the given collection of potential secret words.
     * @param words listOfWords
     * @param maxIncorrectGuesses maxAmount of guesses
     * @param invalidChar *
     */
    @Override
    public void newGame(Collection<String> words, int maxIncorrectGuesses, char invalidChar) {
        this.guessedLetters.clear();
        possibleWords = new TreeSet<>(words);
        this.guessesRemaining = maxIncorrectGuesses;
        this.invalidChar = invalidChar;
        secretWord = possibleWords.iterator().next();
    }

    /**
     * Return remaining of guesses
     * @return remaining of guesses
     */
    @Override
    public int incorrectGuessesRemaining() {
        return guessesRemaining;
    }

    /**
     * Return a list of guessed letters
     * @return list of guessed letters
     */
    @Override
    public Collection<Character> guessedLetters() {
        return guessedLetters.keys();
    }

    /**
     * Return a specially formatted version of the secret word.
     * @return secret word
     */
    @Override
    public String formattedSecretWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            char c = secretWord.charAt(i);
            if (guessedLetters.containsKey(c)) {
                sb.append(c);
            } else {
                sb.append(invalidChar);
            }
        }
        return sb.toString().trim();
    }

    /**
     * Returns the count of words that could still be the secret word
     * @return count of words
     */
    @Override
    public int countOfPossibleWords() {
        return possibleWords.size();
    }

    @Override
    public boolean letterAlreadyGuessed(char letter) {
        return guessedLetters.containsKey(letter);
    }

    @Override
    public int registerAGuess(char letter) {
        int value = 0;
        if (guessedLetters.lookup(letter) != null) {
            value = guessedLetters.lookup(letter);
        }
        guessedLetters.put(letter, value + 1);
        // Divide the possible words into word families based on the guessed letter
        ULTreeMap<String, Set<String>> wordFamilies = new ULTreeMap<>();
        for (String word : possibleWords) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (c == letter) {
                    sb.append(letter);
                } else {
                    sb.append(invalidChar);
                }
            }
            String pattern = sb.toString();

            Set<String> family = wordFamilies.lookup(pattern);
            if (family == null) {
                family = new TreeSet<>();
            }
            family.add(word);
            wordFamilies.put(pattern, family);
        }

        String pattern = "";
        int largestFamilySize = 0;
        // Select the largest word family with keys
        for (String key : wordFamilies.keys()) {
            Set<String> family = wordFamilies.lookup(key);
            // if the size of the family is bigger than current, replace it
            if (family.size() > largestFamilySize) {
                largestFamilySize = family.size();
                pattern = key;
            }
        }

        Set<String> largestFamily = wordFamilies.lookup(pattern);

        // Remove any words that are not in the largest word family
        possibleWords.retainAll(largestFamily);

        secretWord = pattern;
        // Return the number of occurrences of the given letter in the largest word family
        int count = 0;
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter) {
                count++;
            }
        }
        // Decrement the remaining guess count if the guessed letter does not appear in the largest word family
        if (count == 0) {
            guessesRemaining--;
        }
        return count;
    }

    @Override
    public String revealSecretWord() {
        secretWord = possibleWords.iterator().next();
        return secretWord.toUpperCase();
    }
    
    public boolean isGameOver() {
        return possibleWords.size() == 1 || guessesRemaining == 0;
    }

}
    