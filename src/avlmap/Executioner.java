package avlmap;

import java.util.*;
/**
 * @author Edward Oh
 * @author Chen Luo
 */
public interface Executioner {
    void newGame(java.util.Collection<java.lang.String> words, int maxIncorrectGuesses, char invalidChar);
    int incorrectGuessesRemaining();
    Collection<Character> guessedLetters();
    String formattedSecretWord();
    int countOfPossibleWords();
    boolean letterAlreadyGuessed(char letter);
    int registerAGuess(char letter);
    String revealSecretWord();
    boolean isGameOver();
}
