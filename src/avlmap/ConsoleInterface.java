package avlmap;

import java.io.PrintStream;
import java.util.*;

/**
 * @author Edward Oh
 * @author Chen Luo
 */

public class ConsoleInterface {

    private Scanner scanner;
    private PrintStream printer;


    public ConsoleInterface(Scanner scanner, PrintStream out) {
        this.scanner = scanner;
        printer = out;
    }

    public int askForWordLength(Lexicon lexicon) {
        int wordLength = -1;
        boolean validInput = false;
        while (!validInput) {
            printer.print("Enter a word length: ");
            String input = scanner.nextLine();
            try {
                // if the length meets the requirement of wordslength
                // print out length
                wordLength = Integer.parseInt(input);
                Collection<String> words = lexicon.wordsOfLength(wordLength);
                if (!words.isEmpty()) {
                    validInput = true;
                }
                // otherwise redo the input set validInput still fasle
                else {
                    printer.println("No words of length " + wordLength + " exist in the lexicon.");
                }
            } catch (NumberFormatException e) {
                printer.println("Invalid input. Please enter a positive integer.");
            }
        }
        return wordLength;
    }

    public int askForMaximumMisses() {
        int maximumMisses = 0;
        boolean validInput = false;
        while (!validInput) {
            printer.print("Enter number of guesses: ");
            String input = scanner.nextLine();
            try {
                // if the length meets the requirement
                // print out length
                maximumMisses = Integer.parseInt(input);
                if (maximumMisses > 0) {
                    validInput = true;
                } else {
                    // the input shold be greater than 0, going back to while loop
                    printer.println("The number of guesses must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                printer.println("Invalid input. Please enter a positive integer.");
            }
        }
        return maximumMisses;
    }

    public boolean askToDisplayWordCount() {
        boolean displayWordCount = false;
        boolean validInput = false;

        while (!validInput) {
            printer.print("Would you like to see the word count (Y/N)? ");
            String input = scanner.nextLine().toLowerCase();
            // if enter valid input
            // exit out while loop
            // otherwise re doing it
            if (input.equals("y")) {
                displayWordCount = true;
                validInput = true;
            } else if (input.equals("n")) {
                validInput = true;
            } else {
                printer.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
        return displayWordCount;
    }

    public char askNextGuess(Executioner executioner) {
        char guess = 0;
        boolean validInput = false;
        while (!validInput) {
           printer.print("Enter your guess: ");
            String input = scanner.next().toLowerCase();
            if (input.length() != 1) {
                printer.println("Invalid input. Please enter a single letter.");
            } else {
                guess = input.charAt(0);
                if (!Character.isLetter(guess)) {
                    printer.println("Invalid input. Please enter a letter.");
                } else if (executioner.letterAlreadyGuessed(guess)) {
                    printer.println("You've already guessed that letter. Please enter a different letter.");
                } else {
                    validInput = true;
                }
            }
        }
        return guess;
    }

    public boolean playAgain() {
        boolean play = true;
        printer.println("Would you like to play again? Yes or No: ");
        String input = scanner.nextLine();

        if (input.equals("y")) {
            play = true;
        }

        else if (input.equals("n")) {
            play = false;
        }

        return play;
    }

    public void displayGameState(Executioner executioner, boolean displayWordCount) {
        
        printer.println("You have " + executioner.incorrectGuessesRemaining() + " guesses left.");
        printer.println("Used letters: " + executioner.guessedLetters().toString().toUpperCase());
        printer.println("Word: " + executioner.formattedSecretWord().toUpperCase());
        if (displayWordCount) {
            printer.println("Word Count: " + executioner.countOfPossibleWords());
        }
    }

    public void displayResultsOfGuess(char guess, int occurrences) {
        if (occurrences == 0) {
            printer.println("Sorry there is no " + guess + "'s ");
        }
        else {
            printer.println("There is/are " + occurrences + " " + guess + "'s");
        }
    }

    public void displayGameOver(String secretWord, boolean playerWins) {
        if (playerWins) {
            printer.println("Congratulations, you guessed the secret word: " + secretWord + "!");
        } else {
            printer.println("You lose! The secret word was: " + secretWord);
        }
    }

    public String selectSecretWord(Collection<String> secretWords) {
        for (String str : secretWords) {
            printer.println(str);
        }
        printer.println("Please enter one of the passwords: ");
        String secret = this.scanner.nextLine();

        return secret;
    }
}
