package avlmap;
/**
 * Default comparator for ULTREEMAP
 *@author Edward Oh
 *@author Chen Luo
 */

import java.util.Comparator;

public class ComparableComparator<T extends Comparable<T>> implements Comparator<T> {
    public ComparableComparator() {
        //Default does nothing
    }

    /**
     * Compare two objects
     * @param lhs the first object to be compared.
     * @param rhs the second object to be compared.
     * @return
     */
    public int compare(T lhs, T rhs) {
        return lhs.compareTo(rhs);
    }
}
