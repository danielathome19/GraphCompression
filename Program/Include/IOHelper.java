package Program.Include;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class IOHelper {

	/**
	 * Writes a byte array to the file.
	 */
	public static void writeBytes(byte[] bytes, String filePath) throws IOException {
		OutputStream os = new FileOutputStream(filePath);
		os.write(bytes);
		os.close();
	}

	/**
	 * Reads bytes from the file into an array.
	 */
	public static byte[] readBytes(String filePath) throws IOException {
		File file = new File(filePath);
		byte[] bFile = new byte[(int) file.length()];
		FileInputStream fileInputStream = new FileInputStream(file);
		fileInputStream.read(bFile);
		fileInputStream.close();
		return bFile;
	}

	/**
	 * Reads a file into an arraylist of strings. Each character is read as a
	 * string; I know this is weird, but this bad design makes certain things
	 * uniform and simpler (at least to me).
	 */
	static ArrayList<String> readFile(String path) throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader(path));
		int val = 0;
		ArrayList<String> content = new ArrayList<>();
		while ((val = fileReader.read()) != -1) {
			content.add("" + (char) val);
		}
		fileReader.close();
		return content;
	}
}
