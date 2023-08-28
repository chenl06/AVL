package test;
import org.junit.Test;
import wol.DuplicateKeyException;
import wol.ULTreeMap;

import java.util.*;

import static org.junit.Assert.*;
public class testForTreeMap {
    @Test
    public void testInsert() {
        ULTreeMap<String, Integer> map = new ULTreeMap<>();

        // Insert a key-value pair into an empty map
        map.insert("A", 1);
        assertEquals(1, map.size());

        // Insert a key-value pair with a new key
        map.insert("B", 2);
        assertEquals(2, map.size());

        // Insert a key-value pair with an existing key, should throw DuplicateKeyException
        try {
            map.insert("A", 3);
        } catch (DuplicateKeyException e) {
            assertEquals(2, map.size());
        }

        ULTreeMap<Integer, String> map1 = new ULTreeMap<>();

        map1.insert(3, "three");
        map1.insert(1, "one");
        map1.insert(2, "two");
        List<Integer> keys = new ArrayList<>(map1.keys());
        assertEquals(Arrays.asList(1, 2, 3), keys);
    }

    @Test
    public void testPut() {
        ULTreeMap<String, Integer> map = new ULTreeMap<String, Integer>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);

        assertEquals(4, map.size());
        assertEquals((Integer)1, map.lookup("A"));
        assertEquals((Integer)2, map.lookup("B"));
        assertEquals((Integer)3, map.lookup("C"));
        assertEquals((Integer)4, map.lookup("D"));

        try {
            map.put("A", 2);
        } catch (DuplicateKeyException e) {
            assertEquals((Integer)2, map.lookup("A"));
        }

        ULTreeMap<Integer, String> map1 = new ULTreeMap<>();
        map1.put(5, "value5");
        map1.put(3, "value3");
        map1.put(2, "value2");
        map1.put(4, "value4");
        assertEquals(4, map1.size());
        assertEquals("value2", map1.lookup(2));
        assertEquals("value3", map1.lookup(3));
        assertEquals("value4", map1.lookup(4));
        assertEquals("value5", map1.lookup(5));
    }

    @Test
    public void testContainsKey() {
        ULTreeMap<String, Integer> map = new ULTreeMap<>();
        map.put("key1", 10);
        map.put("key2", 20);
        assertTrue(map.containsKey("key1"));
        map.clear();
        assertFalse(map.containsKey("key1"));
        map.put("key1", 10);
        map.put("key2", 20);
        assertFalse(map.containsKey("key3"));
    }

    @Test
    public void testLookup() {
        ULTreeMap<String, Integer> map = new ULTreeMap<>();

        // Test case 1: Lookup on an empty map should return null
        assertNull(map.lookup("key1"));

        // Test case 2: Insert a key-value pair and lookup the key
        map.insert("key1", 1);
        assertEquals(Integer.valueOf(1), map.lookup("key1"));

        // Test case 3: Insert multiple key-value pairs and lookup a key
        map.insert("key2", 2);
        map.insert("key3", 3);
        assertEquals(Integer.valueOf(2), map.lookup("key2"));

        // Test case 4: Lookup a key that doesn't exist in the map
        assertNull(map.lookup("key4"));

        // Test case 5: Lookup a null key should throw a NullPointerException
        try {
            map.lookup(null);
        } catch (NullPointerException e) {
            // Expected exception thrown, test passed
        }
    }

    @Test
    public void testErase() {
        // Initialize test map with some values
        ULTreeMap<Integer, String> map = new ULTreeMap<>();
        map.insert(1, "One");
        map.insert(2, "Two");
        map.insert(3, "Three");
        map.insert(4, "Four");
        map.insert(5, "Five");

        // Test removing a non-existent key
        map.erase(6);
        assertEquals(5, map.size());

        // Test removing a leaf node
        map.erase(5);
        assertFalse(map.containsKey(5));
        assertNull(map.lookup(5));
        assertEquals(4, map.size());

        // Test removing a node with one child
        map.erase(2);
        assertFalse(map.containsKey(2));
        assertNull(map.lookup(2));
        assertEquals(3, map.size());

        // Test removing a node with two children
        map.erase(3);
        assertFalse(map.containsKey(3));
        assertNull(map.lookup(3));
        assertEquals(2, map.size());

        // Test removing the root node
        map.erase(1);
        assertFalse(map.containsKey(1));
        assertNull(map.lookup(1));
        assertEquals(1, map.size());

        // Test removing the only remaining node
        map.erase(4);
        assertFalse(map.containsKey(4));
        assertNull(map.lookup(4));
        assertTrue(map.empty());

    }

    @Test
    public void testClone() {
        ULTreeMap<String, Integer> map1 = new ULTreeMap<>();
        map1.insert("A", 1);
        map1.insert("B", 2);
        map1.insert("C", 3);

        ULTreeMap<String, Integer> map2 = map1.clone();

        // Test that the tree structure is copied correctly
        assertEquals(map1.size(), map2.size());

        // Test that the keys and values are not copied
        for (String entry: map1.keys()) {
            assertFalse(map2.containsKey(entry));
            assertNull(map2.lookup(entry));
        }

        // Test that modifications to map1 do not affect map2
        map1.insert("D", 4);
        assertFalse(map2.containsKey("D"));
    }

}
