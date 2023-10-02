package avlmap;

import java.util.*;

/**
 * Implementation of ULTreeMap using BST
 * @author Chen
 * @author Edward
 **/

public class ULTreeMap<K,V> implements Cloneable, Iterable<ULTreeMap.Mapping<K, V>> {
    /**
     * Node class to contain key and value
     */
    private class Node {
        K key;
        V value;
        Node left;
        Node right;
        Node parent;
        int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
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

    /**
     * Shallow copy of the treemap
     * @return treemap
     */
    @Override
    public ULTreeMap<K, V> clone() {
        ULTreeMap<K, V> clonedMap = new ULTreeMap<>();
        clonedMap.root = cloneTree(root, clonedMap);
        return clonedMap;
    }

    public void insert(K key, V value) throws DuplicateKeyException {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        root = insert(root, key, value);
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
        if (root != null) {
            root = erase(root, key);
            size--;
        }
    }
    public int size() {
        return size;
    }

    /**
     * Need to fix
     * @return
     */
    public boolean empty() {
        return root == null;
    }

    /**
     * Need to fix
     */
    public void clear() {
        clearRec(root); // Start clearing from the root
        root = null;    // Set the root to null after clearing
        size = 0;
    }

    public int heightOfKey(K key) {
        Node node = lookupNode(root, key);
        return (node != null) ? node.height : -1;
    }
    @Override
    public Iterator<Mapping<K, V>> iterator() {
        return new ULTreeMapItrator();
    }

    /**
     * Helper itrator class to irerate the map
     */
    private class ULTreeMapItrator implements Iterator<Mapping<K, V>> {
        private Stack<Node> stack;
        // Keep track of the expected modification count
        private int expectedModificationCount = size;

        public ULTreeMapItrator() {
            stack = new Stack<>();
            updateStack(root);
        }

        @Override
        public boolean hasNext() {
            // if size change during the iteration during the while or hasnext
            if (expectedModificationCount != size) {
                throw new ConcurrentModificationException();
            }
            return !stack.isEmpty();
        }

        @Override
        public Mapping<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node currentNode = stack.pop();
            updateStack(currentNode.right);
            Mapping<K, V> mapping = new Mapping<>();
            mapping.key = currentNode.key;
            mapping.value = currentNode.value;
            return mapping;
        }

        /**
         * Push the element into the stack
         * @param node
         */
        private void updateStack(Node node) {
            while(node != null){
                stack.add(node);
                node = node.left;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
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
        return rebalance(node);
    }
    /**
     * Helper method to find a node in a BST
     * @param node root
     * @param key key yo find
     * @return Node to search
     */
    private Node lookupNode(Node node, K key) {
        return (node == null) ? null
                : (comparator.compare(key, node.key) < 0) ? lookupNode(node.left, key)
                : (comparator.compare(key, node.key) > 0) ? lookupNode(node.right, key)
                : node; // Key found or not found
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
                // has 0 or 1 child
                if (node.left == null || node.right == null) {
                    node = removeNodeSimple(node);
                }
                // has 2 children
                else {
                    Node smallest = findMin(node.right);
                    node.key = smallest.key;
                    node.value = smallest.value;
                    node.height = smallest.height;
                    node.right = erase(node.right, smallest.key);
                }
            }
        }
        updateHeight(node);
        return rebalance(node);
    }

    /**
     *  remove root has either 0 or 1 child
     * @param node root
     * @return node being removed
     */
    private Node removeNodeSimple(Node node) {
        return (node.left == null) ? node.right : node.left;
    }

    /**
     * Find the smallest node
     * @param node root
     * @return smallest node
     */
    private Node findMin(Node node) {
        while(node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }
    /**
     * Return the height of a sub tree stored in node.height
     * @param node node
     * @return height of the node or -1 if it is emoty
     */
    private int height(Node node) {
        return (node != null) ? node.height : 0;
    }

    /**
     * Update the height of the node after rebalacning
     * @param node
     */
    private void updateHeight(Node node) {
        if (node != null) {
            int leftChildHeight = height(node.left);
            int rightChildHeight = height(node.right);
            node.height = Math.max(leftChildHeight, rightChildHeight) + 1;
        }
        // does nothing height is not update
    }

    /**
     * Calculating balancing factor
     * @param node node
     * @return balanced tree number
     */
    private int balanceFactor(Node node) {
        int value = 0;
        if (node != null) {
            value =  height(node.right) - height(node.left);
        }
        return value;
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
     * rebalancing treemap
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

    /**
     * Clearing out entire treemap
     * @param node
     */
    private void clearRec(Node node) {
        if (node == null) {
            return;
        }

        // Recursively clear left and right subtrees
        clearRec(node.left);
        clearRec(node.right);

        // Release memory associated with the node
        node.key = null;
        node.value = null;
        node.left = null;
        node.right = null;
        node.parent = null;
    }

    /**
     * Helper method to copy the entire tree
     * @param node
     * @param clonedMap
     * @return
     */
    private Node cloneTree(Node node, ULTreeMap<K, V> clonedMap) {
        if (node == null) {
            return null;
        }
        Node clonedNode = new Node(node.key, node.value);
        clonedNode.left = cloneTree(node.left,clonedMap);
        clonedNode.right = cloneTree(node.right, clonedMap);
        return clonedNode;
    }


}
