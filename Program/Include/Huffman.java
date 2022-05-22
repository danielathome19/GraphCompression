package Program.Include;

import java.util.HashMap;

public class Huffman {

	public String encoding;
	public HashMap<String, String> mappingTable;

	public Huffman(String encoding, HashMap<String, String> mappingTable) {
		this.encoding = encoding;
		this.mappingTable = mappingTable;
	}
}
