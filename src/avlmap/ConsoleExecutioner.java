package avlmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;
/**
 * Ask Human to guess the word
 * @author Edward Oh
 * @author Chen Luo
 */
public class ConsoleExecutioner implements Executioner {
    private ConsoleInterface ui;
    private String secretWord;
    private Set<Character> guessedLetters;
    private Set<String> possibleWords;
    private int incorrectGuessesRemaining;
    private char invalidChar;
    private boolean gameOver;

    public ConsoleExecutioner(ConsoleInterface ui) {
        this.ui = ui;
        this.guessedLetters = new HashSet<>();
        this.possibleWords = new TreeSet<>();
        this.invalidChar = '*';
        
    }

    @Override
    public void newGame(Collection<String> words, int maxIncorrectGuesses, char invalidChar) {
        this.invalidChar = invalidChar;
        this.incorrectGuessesRemaining = maxIncorrectGuesses;
        this.possibleWords.addAll(words);
        this.secretWord = ui.selectSecretWord(possibleWords);
    }

    

    @Override
    public int incorrectGuessesRemaining() {
        return incorrectGuessesRemaining;
    }

    @Override
    public Collection<Character> guessedLetters() {
        return guessedLetters;
    }

    @Override
    public String formattedSecretWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < secretWord.length(); i++) {
            char c = secretWord.charAt(i);
            if (guessedLetters.contains(c)) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(invalidChar);
            }
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    @Override
    public int countOfPossibleWords() {
        return possibleWords.size();
    }

    @Override
    public boolean letterAlreadyGuessed(char letter) {
        return guessedLetters.contains(letter);

    }

    @Override
    public int registerAGuess(char letter) {
        removeWordsThatDoNotContain(letter);
        
        // register the guess
        guessedLetters.add(letter);
        int count = countOccurrences(letter, secretWord);
        if (count == 0) {
            this.incorrectGuessesRemaining--;
        }
        
        return count;
    }

    @Override
    public String revealSecretWord() {
        return secretWord.toUpperCase();
    }

    @Override
    public boolean isGameOver() {
        return incorrectGuessesRemaining == 0 || formattedSecretWord().indexOf(invalidChar) == -1;
    }

    /**
     * Remove words that does not contain in the possibkeWord list
     * @param letter
     */
    private void removeWordsThatDoNotContain(char letter) {
        List<String> wordsToRemove = new ArrayList<String>();
        // looping through the possiblewords list
        // if the number of letters of occurance in a word is zero
        // add it to the wordtoRemove list
        for (String word : possibleWords) {
            if (countOccurrences(letter, secretWord) == 0) {
                if (word.indexOf(letter) == -1) {
                    wordsToRemove.add(word);
                }
            }
            else {
                //add to words to remove only letters with the that has letter
                if (word.indexOf(letter) == secretWord.indexOf(letter) && countOccurrences(letter, word) == countOccurrences(letter, secretWord)) {
                    wordsToRemove.add(word);
                }
            }
            

        }
        possibleWords.clear();
        possibleWords.addAll(wordsToRemove);
    }

    /**
     * Number of occuraces of a letter in a word
     * @param letter char
     * @param word word
     * @return number of occurace
     */
    private int countOccurrences(char letter, String word) {
        int count = 0;
        for (char c : word.toCharArray()) {
            if (c == letter) {
                count++;
            }
        }
        return count;
    }
}



