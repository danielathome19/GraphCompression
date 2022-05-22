package Program.Include;

public final class FilePaths {

	public static final String DATA_DIRECTORY = "Program/Data/";
	public static final String BWT_COMPRESSED_DIRECTORY = DATA_DIRECTORY + "BWT/";
	public static final String LZ78_COMPRESSED_DIRECTORY = DATA_DIRECTORY + "LZ78/";
	public static final String LZW_COMPRESSED_DIRECTORY = DATA_DIRECTORY + "LZW/";
	public static final String AM_COMPRESSED_DIRECTORY = DATA_DIRECTORY + "AMC/";
	public static final String SAM_COMPRESSED_DIRECTORY = DATA_DIRECTORY + "SAMC/";

	public static final String DATA[] = { "tiny.txt", "medium.txt", "H.pylori.fna", "L.monocytogenes.fna", "E.Coli.fna",
			"S.cerevisiae.fna", "aliceinwonderland.txt", "bible.txt" };
	public static final int NUM_DATA = 8;

	/**
	 * Large DNA files. You do not have to run this.
	 */
	// public static final String DATA[] = { "N.crassa.fna", "C.elegans.fna",
	// "T.nigroviridis.fna"};
	// public static final int NUM_DATA = 3;

	public static final String HUFFMAN_ENCODING_EXTENSION = ".huffman.encoding";
	public static final String HUFFMAN_MAP_EXTENSION = ".huffman.map";

	public static final String LZ78_NODE_ENCODING_EXTENSION = ".nodes";
	public static final String LZ78_CHARS_EXTENSION = ".chars";

	public static final String AM_DICTIONARY_EXTENSION = ".dict";
	public static final String AM_MATRIX_EXTENSION = ".matrix";
	public static final String SAM_MATRIX_EXTENSION = ".sparse";
}
