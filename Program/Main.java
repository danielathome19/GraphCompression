package Program;
import Program.Include.HelperFunctions;
import java.util.Hashtable;
import static java.lang.System.out;

class Program {
    static final boolean CONVERT_CSV = false;
    static final String PATH = "Program/Data/";
    static final String[] FILES = { "tiny.graphml", "manhattan.graphml", "nashville_meetup.graphml", "newyork.graphml" }; // , "spotify_dataset.graphml"  (not have enough RAM)

    public static void main(String[] args) {
        out.println("Running...");
        if (CONVERT_CSV) {
            DataConverters.CSVtoGML(PATH + "nashville_meetup.csv", 1, 2, true, true);
            DataConverters.CSVtoGML(PATH + "spotify_dataset.csv", 0, 1, false, true);
            DataConverters.CSVtoGML(PATH + "tiny.csv", 0, 1, false, true);
        }
        //individualMethodTest();
        SparseMatrixTest();
        LZWTest();
        AdjMatrixTest();
        out.println("Done!");
    }

    public static void individualMethodTest() {
        var adjlist1 = AdjList.constructFromGraphML(PATH + FILES[0], true);
        out.println("Printing full graph");
        adjlist1.print();
        //var bfsTree = adjlist1.breadthFirstSearch(true);
        //out.println("Printing BFS graph");
        //bfsTree.print();
        //bfsTree.saveAsGraphML(PATH + "BFS/" + FILES[1] + ".bfs", true);  // Increased space
        var adjmat1 = new AdjMatrix(adjlist1);
        adjmat1.print();
        out.println(adjmat1.getDictionaryTable());
        out.println(adjmat1.getMatrixRLEString());
        var RLEadjmat = AdjMatrix.buildFromMatrixRLEString(adjmat1.getMatrixRLEString(), adjmat1.getDictionary());
        out.println(adjmat1.getMatrixString());
        out.println(RLEadjmat.getMatrixString());
        //adjlist1.saveAsGraphML(PATH + "Old/" + FILES[1], true);
        String sparsemat = adjmat1.sparsify(true);
        System.out.println("Sparse Matrix:\n" + sparsemat);
        var adjMatFromSparse = new AdjMatrix(sparsemat, new Hashtable<>());
        System.out.println("Reconstructed Matrix:");
        //adjMatFromSparse.print();
        AdjMatrix.saveSparseEncoded(sparsemat, HelperFunctions.stringToArrayList(adjMatFromSparse.getDictionaryTable()), PATH + FILES[1]);
        var adjMatFromLZW = AdjMatrix.readSparseEncoded(PATH + FILES[1]);
        //adjMatFromLZW.print();
        CompressTools.AdjMatCompressTest(PATH + FILES[0], true);
    }

    public static void AdjMatrixTest() {
        long[] amavg = {0, 0};
        for (int i = 0; i < FILES.length; i++) {
            long[] amtest = CompressTools.AdjMatCompressTest(PATH + FILES[i], (i == 0 || i == 4));
            amavg[0] += amtest[0];
            amavg[1] += amtest[1];
        }
        System.out.printf("Average Adjacency Matrix compression time: %.3f ms\n", (double)((amavg[0])/FILES.length) / 1000000);
        System.out.printf("Average Adjacency Matrix decompression time: %.3f ms\n\n", (double)((amavg[1])/FILES.length) / 1000000);
    }

    public static void LZWTest() {
        long[] lzwavg = {0, 0};
        for (String file : FILES) {
            long[] lzwtest = CompressTools.LZWCompressTest(PATH + file);
            lzwavg[0] += lzwtest[0];
            lzwavg[1] += lzwtest[1];
        }
        System.out.printf("Average LZW compression time: %.3f ms\n", (double)((lzwavg[0])/FILES.length) / 1000000);
        System.out.printf("Average LZW decompression time: %.3f ms\n\n", (double)((lzwavg[1])/FILES.length) / 1000000);
    }

    public static void SparseMatrixTest() {
        long[] samcavg = {0, 0};
        for (int i = 0; i < FILES.length; i++) {
            long[] bfstest = CompressTools.SparseMatCompressTest(PATH + FILES[i], (i == 0 || i == 4));
            samcavg[0] += bfstest[0];
            samcavg[1] += bfstest[1];
        }
        System.out.printf("Average Sparse Matrix compression time: %.3f ms\n", (double)((samcavg[0])/FILES.length) / 1000000);
        System.out.printf("Average Sparse Matrix decompression time: %.3f ms\n\n", (double)((samcavg[1])/FILES.length) / 1000000);
    }
}