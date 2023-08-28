package wol;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
/**
 * @author Edward Oh
 * @author Chen Luo
 */
public class MS1Main {
    public static void main(String[] args) {
        char a = '*';
        Scanner scanner = new Scanner(System.in);
        PrintStream printStream = new PrintStream(System.out);
        Lexicon lexicon = new Lexicon(readFile("resources/lexicon.txt"));
        ConsoleInterface ui = new ConsoleInterface(scanner, printStream);
        Executioner ex = new ConsoleExecutioner(ui);
        WheelOfLies game = new WheelOfLies(lexicon, ui, ex, a);
        game.playGame();
    }

    private static Collection<String> readFile(String fileName) {
        Collection<String> words = new HashSet<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                words.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return words;
    }
}