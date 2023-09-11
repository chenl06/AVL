package avlmap;

/**
 * @author Chen Luo
 * @author Edward Oh
 */
public class WheelOfLies {
    private Lexicon lexicon;
    private ConsoleInterface ui;
    private Executioner executioner;
    private char invalidCharater;

    public WheelOfLies(Lexicon lexicon, ConsoleInterface ui, Executioner executioner, char invalidCharater) {
        this.lexicon = lexicon;
        this.ui = ui;
        this.executioner = executioner;
        this.invalidCharater = invalidCharater;
    }

    public void playGame() {
        boolean play = true;
        while (play) {
            int wordLength = ui.askForWordLength(lexicon);
            int maxMisses = ui.askForMaximumMisses();
            // start the game ask user for length and guesses chances
            // and also to display word count or not
            executioner.newGame(lexicon.wordsOfLength(wordLength), maxMisses, invalidCharater);
            boolean wordCountDisplay = ui.askToDisplayWordCount();
            ui.displayGameState(executioner, wordCountDisplay);
            // while game is not over
            while (!executioner.isGameOver()) {
                // ask user for guesses
                char guess = ui.askNextGuess(executioner);
                int occurrences = executioner.registerAGuess(guess);
                ui.displayResultsOfGuess(guess, occurrences);
                // if no more guesses game is over
                if (executioner.incorrectGuessesRemaining() == 0) {               
                    ui.displayGameOver(executioner.revealSecretWord(), false);
                    
                }
                // or the challenger wins the game
                else if(executioner.formattedSecretWord().indexOf(invalidCharater) == -1) {
                    ui.displayGameOver(executioner.revealSecretWord(), true);
                }
                // keep going and display currnet status of the game
                else {
                    ui.displayGameState(executioner, wordCountDisplay);
                }
            }
            // ask user if they want to play again
            play = ui.playAgain();
        }
    }
}