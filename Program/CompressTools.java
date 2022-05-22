package Program;
import java.nio.file.*;
import java.nio.charset.*;
import java.io.*;
import java.util.*;
import static Program.Include.HelperFunctions.verifyEquality;

public class CompressTools {
    private static String ArrayListToString(ArrayList<String> arl) {
        var sb = new StringBuilder();
        for (String s : arl) sb.append(s);
        return sb.toString();
    }

    private static String[] StringToArray(String text) {
        int cnt = 0;
        var arr = new String[text.length()];
        for (char c : text.toCharArray()) arr[cnt++] = Character.toString(c);
        return arr;
    }

    public static ArrayList<String> stringToArrayList(String str) {
        ArrayList<String> stringAsList = new ArrayList<>();
        for (int i = 0; i < str.length(); i++)
            stringAsList.add("" + str.charAt(i));
        return stringAsList;
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static long[] AdjMatCompressTest(String filepath, boolean undirected) {
        try {
            System.out.println("Compressing " + filepath + " using Adjacency Matrix");
            long startTime = System.nanoTime();
            String outfilepath = filepath + ".amc";
            var adjMat = AdjMatrix.constructFromGraphML(filepath, undirected);
            AdjMatrix.saveEncoded(stringToArrayList(adjMat.getMatrixRLEString()), stringToArrayList(adjMat.getDictionaryTable()), outfilepath);
            long endTime = System.nanoTime();
            long enduration = (endTime - startTime);
            System.out.printf("Compression time: %.3f ms\n", (double)enduration / 1000000);

            startTime = System.nanoTime();
            var adjMatDec = AdjMatrix.readEncoded(outfilepath);
            endTime = System.nanoTime();
            long deduration = (endTime - startTime);
            System.out.printf("Decompression time: %.3f ms\n", (double)deduration / 1000000);
            //verifyEquality(stringToArrayList(adjMat.getMatrixString()), stringToArrayList(adjMatDec.getMatrixString()));
            //verifyEquality(stringToArrayList(adjMat.getDictionaryTable()), stringToArrayList(adjMatDec.getDictionaryTable()));
            return new long[]{enduration, deduration};
        } catch (Exception ex) { ex.printStackTrace(); }
        return new long[]{0, 0};
    }

    public static long[] LZWCompressTest(String filepath) {
        try {
            System.out.println("Compressing " + filepath + " using LZW");
            long startTime = System.nanoTime();
            String infile = readFile(filepath, Charset.defaultCharset());
            var encoded = LZW.encode(infile);
            String outfilepath = filepath + ".lzw";
            LZW.saveEncoded(encoded, outfilepath);
            long endTime = System.nanoTime();
            long enduration = (endTime - startTime);
            System.out.printf("Compression time: %.3f ms\n", (double)enduration / 1000000);

            startTime = System.nanoTime();
            var linesenc = LZW.readEncoded(outfilepath);
            String decoded = LZW.decode(linesenc);
            endTime = System.nanoTime();
            long deduration = (endTime - startTime);
            System.out.printf("Decompression time: %.3f ms\n", (double)deduration / 1000000);
            //verifyEquality(stringToArrayList(infile), stringToArrayList(decoded));
            return new long[]{enduration, deduration};
        } catch (Exception ex) { ex.printStackTrace(); }
        return new long[]{0, 0};
    }


    public static long[] SparseMatCompressTest(String filepath, boolean undirected) {
        try {
            System.out.println("Compressing " + filepath + " using Sparse Matrix");
            long startTime = System.nanoTime();
            String outfilepath = filepath + ".amc";
            var adjMat = AdjMatrix.constructFromGraphML(filepath, undirected);
            AdjMatrix.saveSparseEncoded(adjMat.sparsify(false), stringToArrayList(adjMat.getDictionaryTable()), outfilepath);
            long endTime = System.nanoTime();
            long enduration = (endTime - startTime);
            System.out.printf("Compression time: %.3f ms\n", (double)enduration / 1000000);

            startTime = System.nanoTime();
            var adjMatDec = AdjMatrix.readSparseEncoded(outfilepath);
            endTime = System.nanoTime();
            long deduration = (endTime - startTime);
            System.out.printf("Decompression time: %.3f ms\n", (double)deduration / 1000000);
            //verifyEquality(stringToArrayList(adjMat.getMatrixString()), stringToArrayList(adjMatDec.getMatrixString()));
            //verifyEquality(stringToArrayList(adjMat.getDictionaryTable()), stringToArrayList(adjMatDec.getDictionaryTable()));
            return new long[]{enduration, deduration};
        } catch (Exception ex) { ex.printStackTrace(); }
        return new long[]{0, 0};
    }


}
