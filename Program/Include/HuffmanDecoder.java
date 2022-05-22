package Program.Include;

import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanDecoder {

	public ArrayList<String> decode(Huffman huffObj) throws Exception {
		HashMap<String, String> reverseMap = new HashMap<>();
		HashMap<String, String> mappingTable = huffObj.mappingTable;
		for (String x : mappingTable.keySet())
			reverseMap.put(mappingTable.get(x), x);
		ArrayList<String> decodedMsg = new ArrayList<>();
		StringBuilder encode = new StringBuilder();
		for (int i = 0; i < huffObj.encoding.length(); i++) {
			encode.append(huffObj.encoding.charAt(i));
			if (reverseMap.containsKey(encode.toString())) {
				decodedMsg.add(reverseMap.get(encode.toString()));
				encode = new StringBuilder();
			}
		}
		return decodedMsg;
	}
}
