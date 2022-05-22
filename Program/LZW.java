package Program;
import Program.Include.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LZW {

    public static ArrayList<String> encode(String text) {
        HashMap<String,Integer> dict = new LinkedHashMap<>();
        ArrayList<String> temp = new ArrayList<>();
        String p = "";
        int R = 255;
        int max_size = (int)Math.pow(2, 8);
        for (int i = 0; i < R; i++) dict.put("" + (char)i, i);
        for (char c : text.toCharArray()) {
            String cw = p + c;
            if (dict.containsKey(cw)) p = cw;
            else {
                temp.add(dict.get(p).toString());
                if (R < max_size) dict.put(cw, R++);
                p = "" + c;
            }
        }
        if (!p.equals("")) temp.add(dict.get(p).toString());
        return temp;
    }

    public static void saveEncoded(ArrayList<String> encoded, String filepath) {
        String filename = filepath.split("/")[filepath.split("/").length - 1];
        try {
            var hufdata = new HuffmanEncoder().encode(encoded);
            HuffmanIOHelper.writeHuffman(hufdata, FilePaths.LZW_COMPRESSED_DIRECTORY + filename + FilePaths.LZ78_CHARS_EXTENSION);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public static ArrayList<Integer> readEncoded(String filepath) {
        var encoded = new ArrayList<Integer>();
        String filename = filepath.split("/")[filepath.split("/").length - 1];
        try {
            var huffile = HuffmanIOHelper.readHuffman(FilePaths.LZW_COMPRESSED_DIRECTORY + filename + FilePaths.LZ78_CHARS_EXTENSION);
            var lzwfile = new HuffmanDecoder().decode(huffile);
            for (String x : lzwfile) encoded.add(Integer.parseInt(x));
        } catch (Exception ex) { ex.printStackTrace(); }
        return encoded;
    }

    public static String decode(ArrayList<Integer> encoded) {
        HashMap<Integer,String> dict = new LinkedHashMap<>();
        String p = "" + (char)(int)encoded.get(0);
        var temp = new StringBuilder(p);
        int R = 255;
        int max_size = (int)Math.pow(2, 8);
        for (int i = 0; i < R; i++) dict.put(i, "" + (char)i);

        String cw = null;
        for (int i = 1; i < encoded.size(); i++) {
            int ckey = encoded.get(i);
            if (dict.containsKey(ckey)) cw = dict.get(ckey);
            else if (ckey == R) cw = p + p.charAt(0);
            temp.append(cw);
            if (R < max_size) dict.put(R++, p + cw.charAt(0));
            p = cw;
        }

        return temp.toString();
    }
}
