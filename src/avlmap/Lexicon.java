package avlmap;

import java.util.ArrayList;
import java.util.Collection;
/**
 * @author Edward Oh
 * @author Chen Luo
 */
public class Lexicon {
    private Collection<String> collection;

    /**
     * Constructs a lexicon from the given collection of words.
     * @param words collection of words
     */
    public Lexicon(Collection<String> words) {
        collection = words;
    }

    /**
     * This method returns a Collection of words of the given length. The words will be drawn from the lexicon.
     * If the lexicon does not include any words of the given length, the returned collection will be empty.
     * @param length length user asked
     * @return filter words
     */
    public Collection<String> wordsOfLength(int length) {
        Collection<String> result = new ArrayList<>();
        for (String word : collection) {
            if (word.length() == length) {
                result.add(word);
            }
        }
        return result;
    }


}
