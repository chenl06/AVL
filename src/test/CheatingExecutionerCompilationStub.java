package test;

import java.util.Arrays;
import java.util.Collection;

import avlmap.CheatingExecutioner;

/**
 * This class is designed to help you test whether your CheatingExecutioner code will compile in my JUnit tests.
 * Be sure that you can compile this class, unmodified.  
 * If your code does not compile with this class, I will not be able to test your code.
 * If your code does compile with this class, odds are good it will compile in my JUnit tests.
 * Your code compiling with this class is not a sufficient level of testing.
 * @author Joe Meehean
 *
 */
public class CheatingExecutionerCompilationStub {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String[] words = new String[] {"ally", "beta", "cool", "deal", "else", 
				"flew", "good", "hope", "ibex"};

		CheatingExecutioner exec = new CheatingExecutioner();
		exec.newGame(Arrays.asList(words), 26, '*');

		// guess e
		int occur = exec.registerAGuess('e');
		int wordCount = exec.countOfPossibleWords();
		boolean gameOver = exec.isGameOver();
		int guessesRemaining = exec.incorrectGuessesRemaining();
		Collection<Character> guessedLetters = exec.guessedLetters();
		String formattedSecretWord = exec.formattedSecretWord(); 
		boolean letterGuessedAlready = exec.letterAlreadyGuessed('a');
		String secretWord = exec.revealSecretWord();
		
		System.out.println("Cheating Executioner Compiles");
	}
}
