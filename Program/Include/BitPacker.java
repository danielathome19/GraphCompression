package Program.Include;

public class BitPacker {

	/**
	 * Converts str, a bit-string, to decimal
	 */
	public static int binaryToDecimal(String str) {
		int dec = 0;
		for (int i = str.length() - 1, k = 1; i >= 0; i--, k = k << 1)
			if (str.charAt(i) == '1')
				dec += k;
		return dec;
	}

	/**
	 * Converts n into a binary string of length b.
	 */
	public static String decimalToBinary(int n, int b) {
		StringBuilder bin = new StringBuilder(b);
		for (int i = 0; i < b; i++)
			bin.append("0");
		b--;
		while (n > 0) {
			bin.replace(b, b + 1, "" + n % 2);
			n = n >> 1;
			b--;
		}
		return bin.toString();
	}

	/**
	 * Packs 8 bits at a time into a byte. bits must contain only 1 & 0. The total
	 * number of bytes = 1 + ceil(bits.length/8). Last byte indicates length of the
	 * last packed byte.
	 */
	public static byte[] pack(String bits) {
		int numbytes = 1 + (int)Math.ceil((double)bits.length()/8);
		byte[] arr = new byte[numbytes];
		arr[numbytes - 1] = (bits.length() % 8 != 0) ? (byte)(bits.length() % 8) : (byte)8;
		for (int b = 0; b < numbytes - 1; b++) {
			var substr = bits.substring((b * 8), (bits.length() % 8 == 0 || b + 1 != numbytes - 1) ? (b + 1) * 8 : bits.length());
			arr[b] = (byte)binaryToDecimal(substr);
		}
		return arr;
	}

	/**
	 * Unpacks each byte into a bit representation. All representations are in 8
	 * bits, except possibly the last one.
	 */
	public static String unpack(byte[] bytes) {
		var s = new StringBuilder();
		for (int i = 0; i < bytes.length - 1; i++) s.append(decimalToBinary((int)(bytes[i] & 0xFF), (i != bytes.length - 2) ? 8 : bytes[bytes.length - 1] & 0xFF));
		return s.toString();
	}
}
