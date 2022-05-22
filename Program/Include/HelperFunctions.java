package Program.Include;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class HelperFunctions {

	/**
	 * Returns the distinct characters in sorted order.
	 */
	public static <T> ArrayList<T> getSortedAlphabet(ArrayList<T> str) {
		TreeSet<T> sigma = new TreeSet<>();
		for (T c : str)
			sigma.add(c);
		ArrayList<T> sigmaAsArray = new ArrayList<>(sigma.size());
		for (T c : sigma)
			sigmaAsArray.add(c);
		return sigmaAsArray;
	}

	/**
	 * Returns frequency of each character.
	 */
	public static <T> HashMap<T, Integer> getFrequencies(ArrayList<T> str) {
		HashMap<T, Integer> freqMap = new HashMap<>();
		for (T x : str)
			if (freqMap.containsKey(x))
				freqMap.put(x, freqMap.get(x) + 1);
			else
				freqMap.put(x, 1);
		return freqMap;
	}

	/**
	 * Segregates each character into a string arraylist
	 */
	public static ArrayList<String> stringToArrayList(String str) {
		ArrayList<String> stringAsList = new ArrayList<>();
		for (int i = 0; i < str.length(); i++)
			stringAsList.add("" + str.charAt(i));
		return stringAsList;
	}

	/**
	 * Concatenates all characters into a string.
	 */
	public static <T> String arrayListToString(ArrayList<T> list) {
		StringBuilder sb = new StringBuilder(list.size());
		for (int i = 0; i < list.size(); i++)
			sb.append(list.get(i));
		return sb.toString();
	}

	/**
	 * Verifies if the content of both arraylists are the same.
	 */
	public static <T> void verifyEquality(ArrayList<T> arg1, ArrayList<T> arg2) throws Exception {
		if (arg1.size() == arg2.size()) {
			for (int i = 0; i < arg1.size(); i++)
				if (!arg1.get(i).equals(arg2.get(i)))
					throw new Exception("Something is wrong!");
		} else
			throw new Exception("Something is wrong!");

	}
}
