package test;

import java.util.Collection;

import wol.ULTreeMap;

/**
 * This class is designed to help you test whether your ULTreeMap code will compile in my JUnit tests.
 * Be sure that you can compile this class, unmodified.  
 * If your code does not compile with this class, I will not be able to test your code.
 * If your code does compile with this class, odds are good it will compile in my JUnit tests.
 * Your code compiling with this class is not a sufficient level of testing.
 * @author Joe Meehean
 *
 */
public class ULTreeMapCompilationStub {
	
	public static void main(String[] args) {
		ULTreeMap<String, Integer> map1 = new ULTreeMap<>();
		ULTreeMap<String, Integer> map2 = new ULTreeMap<>(String.CASE_INSENSITIVE_ORDER);
		callAllMethods(map1);
		callAllMethods(map2);
		
		System.out.println("ULTreeMap compiles");
	}
	
	@SuppressWarnings("unused")
	public static void callAllMethods(ULTreeMap<String,Integer> map) {
		ULTreeMap<String, Integer> clone = map.clone();
		map.insert("key", 1);
		map.put("key", 1);
		boolean contains = map.containsKey("key");
		Integer i = map.lookup("key");
		map.erase("key");
		Collection<String> keys = map.keys();
		int size = map.size();
		boolean empty = map.empty();
		map.clear();
	}
}
