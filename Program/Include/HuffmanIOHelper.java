package Program.Include;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class HuffmanIOHelper {

	/**
	 * Writes the Huffman map into a file.
	 */
	public static void writeMap(HashMap<String, String> map, String filePath) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
		Iterator<String> it = map.keySet().iterator();
		if (it.hasNext()) {
			String key = it.next();
			bw.write(String.format("%s:%s", key, map.get(key)));
		}
		while (it.hasNext()) {
			String key = it.next();
			bw.write(String.format("\n%s:%s", key, map.get(key)));
		}
		bw.close();
	}

	/**
	 * Reads the Huffman map from the file.
	 */
	public static HashMap<String, String> readMap(String filePath) throws FileNotFoundException {
		HashMap<String, String> map = new HashMap<>();
		Scanner in = new Scanner(new FileReader(filePath));
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (line.equals("")) // a newline character has been written
				continue;
			if (line.startsWith("::")) { // if the : character has been written
				line = line.substring(2);
				map.put(":", line);
			} else if (line.startsWith(":")) { // this means a new line character is written in the previous line
				line = line.substring(1);
				map.put("\n", line);
			} else {
				String[] tokens = line.split(":");
				map.put(tokens[0], tokens[1]);
			}
		}
		in.close();
		return map;
	}

	/**
	 * Writes the Huffman object into a file.
	 * 
	 * The encoding part is written as a byte file. So, the bits need to packed
	 * first using BitPacker/pack method.
	 * 
	 * The map is written using the writeMap function.
	 */
	public static void writeHuffman(Huffman obj, String filePath) throws IOException {
		IOHelper.writeBytes(BitPacker.pack(obj.encoding), filePath + FilePaths.HUFFMAN_ENCODING_EXTENSION);
		writeMap(obj.mappingTable, filePath + FilePaths.HUFFMAN_MAP_EXTENSION);
	}

	/**
	 * Read the Huffman object from the file.
	 * 
	 * The encoding part is read from the byte file; note that the byte file needs
	 * to unpacked using BitPacker/unpack method.
	 * 
	 * The map is read using the readMap function.
	 */
	public static Huffman readHuffman(String filePath) throws IOException {
		byte[] bytes = IOHelper.readBytes(filePath + FilePaths.HUFFMAN_ENCODING_EXTENSION);
		String encoding = BitPacker.unpack(bytes);
		HashMap<String, String> mappingTable = readMap(filePath + FilePaths.HUFFMAN_MAP_EXTENSION);
		return new Huffman(encoding, mappingTable);
	}
}
