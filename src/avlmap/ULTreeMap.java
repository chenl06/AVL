package avlmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
/**
 * Implementation of ULTreeMap using BST
 * @author Chen
 * @author Edward
 **/

public class ULTreeMap<K,V> implements Cloneable {
    /**
     * Node class to contain key and value
     */
    private class Node {
        K key;
        V value;
        Node left;
        Node right;
        int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 0;
        }
    }

    public static class Mapping<K, V> {
        private K key;
        private V value;
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
    }
    private Node root;
    private Comparator<K> comparator;
    private int size;

    public ULTreeMap(){
        this(new ComparableComparator<K>());
    }

    public ULTreeMap(Comparator<K> compare) {
        root = null;
        this.comparator = compare;
        size = 0;
    }

    @Override
    public ULTreeMap<K, V> clone() {
        ULTreeMap<K, V> clone = new ULTreeMap<>();
        clone.size = size;
        clone.comparator = comparator;
        return clone;
    }

    public void insert(K key, V value) throws DuplicateKeyException {
        if (key != null) {
            root = insert(root, key, value);
            updateHeight(root);
            root = rebalance(root);
        }
    }

    public void put(K key, V value) {
        if (key != null) {
            try {
                insert(key, value);
            } catch (DuplicateKeyException e) {
                Node node = lookupNode(root, key);
                node.value = value;
            }
        }
    }

    public boolean containsKey(K key) {
        Node node = lookupNode(root, key);
        return node != null;
    }

    public V lookup(K key) {
        Node node = lookupNode(root, key);
        return node == null ? null : node.value;
    }

    public void erase(K key) {
        root = erase(root, key);
        if (root != null) {
            updateHeight(root);
            root = rebalance(root);
        }

    }

    public Collection<K> keys() {
        List<K> list = new ArrayList<>();
        inorder(root, list);
        return list;
    }

    public int size() {
        return size;
    }

    public boolean empty() {
        return root == null;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public int heightOfKey(K key) {
        Node node = lookupNode(root, key);
        return height(node);
    }

    /**
     * Helper method for insert
     * @param node root
     * @param key key
     * @param value value
     * @return root
     * @throws DuplicateKeyException duplicated key
     */
    private Node insert(Node node, K key, V value) throws DuplicateKeyException {
        // if node is null create a new Node
        if (node == null) {
            size++;
            node = new Node(key, value);
        }
        else {
            int compare = comparator.compare(key, node.key);
            // by comparing the key we either transverse to right or left
            // and recursively call insert again
            if (compare < 0) {
                node.left = insert(node.left, key, value);
            } else if (compare > 0) {
                node.right = insert(node.right, key, value);
            }
            // if keys are equaled duplicated key found
            else {
                throw new DuplicateKeyException();
            }
        }
        updateHeight(node);
        return node;
    }

    /**
     * Helper method to find a node in a BST
     * @param node root
     * @param key key yo find
     * @return Node to search
     */
    private Node lookupNode(Node node, K key) {
        Node returnNode;
        // if node is null return null
        if (node == null) {
            returnNode = null;
        }
        else {
            int compare = comparator.compare(key, node.key);
            // by comparing the key we either transverse to right or left
            // and recursively call lookup again
            if (compare < 0 ){
                returnNode = lookupNode(node.left, key);
            }
            else if (compare > 0) {
                returnNode = lookupNode(node.right, key);
            }
            else {
                returnNode = node;
            }
        }

        return returnNode;

    }

    /**
     * Helper for erase method
     * @param node root
     * @param key key to remove
     * @return removed node
     */
    private Node erase(Node node, K key) {
        if (node != null) {
            int compare = comparator.compare(key, node.key);
            // by comparing the key we either transverse to right or left
            // and recursively call erase again
            if (compare < 0 ){
                node.left = erase(node.left, key);
            }
            else if (compare > 0) {
                node.right = erase(node.right, key);
            }
            else {
                size--;
                // has 0 or 1 child
                if (node.left == null || node.right == null) {
                    node = removeNodeSimple(node);
                }
                // has 2 children
                else {
                    Node smallest = findMin(node.right);
                    node.key = smallest.key;
                    node.right = erase(node.right, node.key);
                }
            }
        }
        updateHeight(root);
        return node;
    }

    /**
     *  remove root has either 0 or 1 child
     * @param node root
     * @return node being removed
     */
    private Node removeNodeSimple(Node node) {
        return (node.left != null) ? node.left : node.right;
    }

    /**
     * Find the smallest node
     * @param node root
     * @return smallest node
     */
    private Node findMin(Node node) {
        while(node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Inorder trasversal the tree
     * @param node root
     * @param list list of node
     */
    private void inorder(Node node, List<K> list) {
        if (node != null) {
            inorder(node.left, list);
            list.add(node.key);
            inorder(node.right, list);
        }
    }

    /**
     * Return the height of a sub tree stored in node.height
     * @param node node
     * @return height of the node or -1 if it is emoty
     */
    private int height(Node node) {
        return node != null ? node.height : -1;
    }

    /**
     * Update the height of the node after rebalacning
     * @param node
     */
    private void updateHeight(Node node) {
        int leftChildHeight = height(node.left);
        int rightChildHeight = height(node.right);
        node.height = Math.max(leftChildHeight, rightChildHeight) + 1;
    }

    /**
     * Calculating balancing factor
     * @param node node
     * @return balanced tree number
     */
    private int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    /**
     * Right rotation
     * @param node
     * @return
     */
    private Node rotateRight(Node node) {
        Node leftChild = node.left;

        node.left = leftChild.right;
        leftChild.right = node;

        updateHeight(node);
        updateHeight(leftChild);

        return leftChild;
    }

    /**
     * Left rotation
     * @param node
     * @return
     */
    private Node rotateLeft(Node node) {
        Node rightChild = node.right;

        node.right = rightChild.left;
        rightChild.left = node;

        updateHeight(node);
        updateHeight(rightChild);

        return rightChild;
    }

    /**
     *
     * @param node
     * @return
     */
    private Node rebalance(Node node) {
        int balanceFactor = balanceFactor(node);

        // Left-heavy?
        if (balanceFactor < -1) {
            if (balanceFactor(node.left) <= 0) {    // Case 1
                // Rotate right
                node = rotateRight(node);
            } else {                                // Case 2
                // Rotate left-right
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
            }
        }

        // Right-heavy?
        if (balanceFactor > 1) {
            if (balanceFactor(node.right) >= 0) {    // Case 3
                // Rotate left
                node = rotateLeft(node);
            } else {                                 // Case 4
                // Rotate right-left
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
        }

        return node;
    }


}
