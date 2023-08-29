package wol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
/**
 * Implementation of ULTreeMap using BST
 * @author Chen
 * @author Edward
 **/

public class ULTreeMap<K extends Comparable<K>,V> implements Cloneable {
    /**
     * Node class to contain key and value
     */
    private class Node {
        K key;
        V value;
        private Node left;
        private Node right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
        }
    }
    private Node root;
    private Comparator<K> comparator;
    private int size;

    public ULTreeMap(){
        this(new ComparableComparator<K>());
    }

    public ULTreeMap(Comparator<K> comparator) {
        root = null;
        this.comparator = comparator;
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
            }
            else if (compare > 0) {
                node.right = insert(node.right, key, value);
            }
            // if keys are equaled duplicated key found
            else {
                throw new DuplicateKeyException();
            }
        }
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
        if (node == null) {
            // do nothing
        }
        else {
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

}
