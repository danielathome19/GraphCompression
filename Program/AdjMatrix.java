package Program;
import Program.Include.*;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import java.io.FileWriter;
import java.util.*;

public class AdjMatrix {
    private boolean adjMatrix[][];
    private int numVertices;
    private Dictionary<Integer, String> vertexLookup;

    public AdjMatrix(int numVertices) {
      this.numVertices = numVertices;
      adjMatrix = new boolean[numVertices][numVertices];
      vertexLookup = null;
    }

    public AdjMatrix(int numVertices, boolean adjMatrix[][], Dictionary<Integer, String> dictionary) {
        this.numVertices = numVertices;
        this.adjMatrix = adjMatrix;
        vertexLookup = dictionary;
    }

    public <T> AdjMatrix(AdjList<T> adjList) {
        Dictionary<String, Integer> map  = new Hashtable<>();
        vertexLookup = new Hashtable<>();
        int cnt = 0;
        for (GraphNode<T> node : adjList.getList()) {
            vertexLookup.put(cnt, node.getId());
            map.put(node.getId(), cnt++);
        }
        numVertices = cnt;
        adjMatrix = new boolean[numVertices][numVertices];
        for (GraphNode<T> node : adjList.getList()) {
            for (GraphNode<T> neighbor : node.getNeighbors()) {
                int from = map.get(node.getId());
                int to = map.get(neighbor.getId());
                adjMatrix[from][to] = true;
            }
        }
    }

    public AdjMatrix(TinkerGraph graph, boolean undirected) {
        Dictionary<String, Integer> map  = new Hashtable<>();
        vertexLookup = new Hashtable<>();
        int cnt = 0;
        Iterable<Vertex> vertices = graph.getVertices();
        for (Vertex vertex : vertices) {
            vertexLookup.put(cnt, (String)vertex.getId());
            map.put((String)vertex.getId(), cnt++);
        }
        numVertices = cnt;
        adjMatrix = new boolean[numVertices][numVertices];
        for (Vertex vertex : vertices) {
            Iterable<Edge> edges = vertex.getEdges(Direction.BOTH);
            for (Edge edge : edges) {
                Vertex inVertex = edge.getVertex(Direction.IN);
                Vertex outVertex = edge.getVertex(Direction.OUT);
                String idin = (String)inVertex.getId();
                String idout = (String)outVertex.getId();
                int from = map.get(idin);
                int to = map.get(idout);
                adjMatrix[from][to] = true;
                if (undirected) adjMatrix[to][from] = true;
            }
        }
    }

    public AdjMatrix(String sparseForm, Dictionary<Integer, String> dictionary) {
        var sb = new StringBuilder();
        int line = 2;
        //int numverts = 0;  //Implied by len(re) - 1
        //int nnz = 0;
        var re = new ArrayList<Integer>();
        var ci = new ArrayList<Integer>();
        int cnt = 0;
        for (char c : sparseForm.toCharArray()) {
            if (line < 2) {
                if (c == '\n') {
                    //if (line++ == 0) numverts = Integer.parseInt(sb.toString());
                    //if (line == 1) nnz = Integer.parseInt(sb.toString());
                    sb = new StringBuilder();
                    line++;
                } else sb.append(c);
            } else {
                if (c == ' ' || c == '\n') {
                    if (line == 2) re.add(Integer.parseInt(sb.toString()));
                    else ci.add(Integer.parseInt(sb.toString()));
                    sb = new StringBuilder();
                }
                if (c == '\n') line++;
                else if (c != ' ') sb.append(c);
                if (cnt == sparseForm.length() - 1) ci.add(Integer.parseInt(sb.toString()));
            }
            cnt++;
        }

        //Reconstruct
        numVertices = re.size() - 1;
        vertexLookup = dictionary;
        adjMatrix = new boolean[numVertices][numVertices];
        int cecnt = 0;
        int recnt = 1;
        for (int r = 0; r < numVertices; r++) {
            int amtPerRow = re.get(recnt) - re.get(recnt - 1);
            while (amtPerRow != 0) {
                adjMatrix[r][ci.get(cecnt++)] = true;
                amtPerRow--;
            }
            recnt++;
        }
    }

    public void addEdgeForward(int idFrom, int idTo) { adjMatrix[idFrom][idTo] = true; }
    public void addEdge(int i, int j) {
        addEdgeForward(i, j);
        addEdgeForward(j, i);
    }

    public void removeEdgeForward(int idFrom, int idTo) { adjMatrix[idFrom][idTo] = false; }
    public void removeEdge(int i, int j) {
      adjMatrix[i][j] = false;
      adjMatrix[j][i] = false;
    }
  
    public String toString() {
      var s = new StringBuilder();
      for (int i = 0; i < numVertices; i++) {
        s.append(i).append(":\t");
        for (boolean j : adjMatrix[i]) s.append(j ? 1 : 0).append(" ");
        s.append("\n");
      }
      return s.toString();
    }

    public void print() { System.out.println(this.toString()); }
    public Dictionary<Integer, String> getDictionary() { return vertexLookup; }

    public String getMatrixString() {
        var s = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            for (boolean j : adjMatrix[i]) s.append(j ? 1 : 0);
            s.append("\n");
        }
        return s.toString();
    }

    public String getMatrixRLEString() {
        var s = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            var sb = new StringBuilder();
            for (boolean j : adjMatrix[i]) sb.append(j ? "t" : "f");
            //s.append(RLE.encode(HelperFunctions.arrayListToString(  // Too slow and uses too much RAM to decode
            //        MTF.encode(HelperFunctions.stringToArrayList(sb.toString()),
            //               HelperFunctions.getSortedAlphabet(HelperFunctions.stringToArrayList(sb.toString()))))));
            s.append(RLE.encode(sb.toString()));  // Doesn't compress as small as when using MTF before but still good
            s.append("\n");
        }
        return s.toString();
    }

    public static boolean[][] decodeMatrixRLEString(String str) {
        var matrixStr = new ArrayList<String>();
        var sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == '\n') {
                //matrixStr.add(sb.toString());
                matrixStr.add(RLE.decode(sb.toString()));
                //String rleDec = RLE.decode(sb.toString());
                //matrixStr.add(HelperFunctions.arrayListToString(MTF.decode(HelperFunctions.stringToArrayList(rleDec),
                //        HelperFunctions.getSortedAlphabet(HelperFunctions.stringToArrayList(rleDec)))));
                sb = new StringBuilder();
            } else sb.append(c);
        }
        int numVerts = matrixStr.get(0).length();
        boolean[][] matrix = new boolean[numVerts][numVerts];
        for (int r = 0; r < matrixStr.size(); r++)
            for (int c = 0; c < matrixStr.get(r).length(); c++)
                matrix[r][c] = (matrixStr.get(r).charAt(c) == 't');
        return matrix;
    }

    public static AdjMatrix buildFromMatrixRLEString(String str, Dictionary<Integer, String> dictionary) {
        var matrix = decodeMatrixRLEString(str);
        return new AdjMatrix(matrix.length, matrix, dictionary);
    }

    public ArrayList<String> getMatrixStringArray() {
        var array = new ArrayList<String>();
        for (int i = 0; i < numVertices; i++) {
            var s = new StringBuilder();
            for (boolean j : adjMatrix[i]) s.append(j ? 1 : 0);
            array.add(s.toString());
        }
        return array;
    }

    public static AdjMatrix constructFromGraphML(String filepath, boolean undirected) {
        return DataConverters.getAdjMatrixFromGraph(DataConverters.buildGraphFromGraphML(filepath), undirected);
    }

    public String getDictionaryTable() {
        if (vertexLookup == null) return "";
        var s = new StringBuilder();
        for (int i = 0; i < numVertices; i++) s.append(vertexLookup.get(i)).append("\n");
        return s.toString();
    }

    public String sparsify(boolean print) {
        // Algorithm from https://userpages.umbc.edu/~cmarron/cs341.s19/projects/proj1.shtml
        int nnz = 0;
        for (int r = 0; r < numVertices; r++)
            for (int c = 0; c < numVertices; c++)
                nnz += (adjMatrix[r][c]) ? 1 : 0;
        var nz = new int[nnz];  // TODO: can be int[] if costs are added to edges
        for (int i = 0; i < nnz; i++) nz[i] = 1;  // TODO: swap with actual value of each non-zero element if costs added
        var re = new int[numVertices + 1];
        var ci = new int[nnz];
        re[0] = 0;
        int cnt = 0;
        for (int r = 0; r < numVertices; r++) {
            int runningsum = 0;
            for (int c = 0; c < numVertices; c++) {
                if (adjMatrix[r][c]) {
                    runningsum++;
                    ci[cnt++] = c;
                }
            }
            re[r + 1] = re[r] + runningsum;
        }
        if (print) {
            System.out.print("NZ:\n[ ");
            for (int i : nz) System.out.print(i + ", ");
            System.out.println("]");
            System.out.print("RE:\n[ ");
            for (int i : re) System.out.print(i + ", ");
            System.out.println("]");
            System.out.print("CI:\n[ ");
            for (int i : ci) System.out.print(i + ", ");
            System.out.println("]");
        }

        var sb = new StringBuilder();
        //sb.append(numVertices).append("\n");
        // TODO: NNZ is unnecessary for now
        //sb.append(nnz);  // TODO: uncomment below and remove this line if nz contains non-boolean values
        //for (int i : nz) sb.append(i + " ");
        //sb.setLength(sb.length() - 1);
        //sb.append("\n");
        for (int i : re) sb.append(i + " ");
        sb.setLength(sb.length() - 1);
        sb.append("\n");
        for (int i : ci) sb.append(i + " ");
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static void saveEncoded(ArrayList<String> RLEmatrix, ArrayList<String> dictionary, String filepath) {
        String filename = filepath.split("/")[filepath.split("/").length - 1];
        try {
            // TODO: maybe bitpack the matrix? Seems infeasible
            var hufmat = new HuffmanEncoder().encode(RLEmatrix);
            HuffmanIOHelper.writeHuffman(hufmat, FilePaths.AM_COMPRESSED_DIRECTORY + filename + FilePaths.AM_MATRIX_EXTENSION);
            var hufdict = new HuffmanEncoder().encode(dictionary);
            HuffmanIOHelper.writeHuffman(hufdict, FilePaths.AM_COMPRESSED_DIRECTORY + filename + FilePaths.AM_DICTIONARY_EXTENSION);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public static AdjMatrix readEncoded(String filepath) {
        String filename = filepath.split("/")[filepath.split("/").length - 1];
        int cnt = 0;
        Dictionary<Integer, String> dictionary  = new Hashtable<>();
        Dictionary<String, Integer> map  = new Hashtable<>();
        try {
            var hufdict = HuffmanIOHelper.readHuffman(FilePaths.AM_COMPRESSED_DIRECTORY + filename + FilePaths.AM_DICTIONARY_EXTENSION);
            var amudict = new HuffmanDecoder().decode(hufdict);
            var sb = new StringBuilder();
            for (String x : amudict) {
                if (x.equals("\n")) {
                    dictionary.put(cnt, sb.toString());
                    map.put(sb.toString(), cnt++);
                    sb = new StringBuilder();
                } else sb.append(x);
            }

            var hufmat = HuffmanIOHelper.readHuffman(FilePaths.AM_COMPRESSED_DIRECTORY + filename + FilePaths.AM_MATRIX_EXTENSION);
            var amumat = new HuffmanDecoder().decode(hufmat);
            /*boolean adjmat[][] = new boolean[cnt][cnt];
            int col = 0;
            int row = 0;
            for (String x : amumat) {
                if (x.equals("\n")) {
                    row++;
                    col = 0;
                } else {
                    adjmat[row][col] = x.equals("1");
                    col++;
                }
            }*/
            return new AdjMatrix(cnt, decodeMatrixRLEString(HelperFunctions.arrayListToString(amumat)), dictionary);
        } catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    public static void saveSparseEncoded(String sparse, ArrayList<String> dictionary, String filepath) {
        String filename = filepath.split("/")[filepath.split("/").length - 1];
        try {
            var hufdata = new HuffmanEncoder().encode((LZW.encode(sparse)));
            HuffmanIOHelper.writeHuffman(hufdata,FilePaths.SAM_COMPRESSED_DIRECTORY + filename + FilePaths.SAM_MATRIX_EXTENSION);
            var hufdict = new HuffmanEncoder().encode(dictionary);
            HuffmanIOHelper.writeHuffman(hufdict, FilePaths.SAM_COMPRESSED_DIRECTORY + filename + FilePaths.AM_DICTIONARY_EXTENSION);
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public static AdjMatrix readSparseEncoded(String filepath) {
        String filename = filepath.split("/")[filepath.split("/").length - 1];
        int cnt = 0;
        Dictionary<Integer, String> dictionary  = new Hashtable<>();
        Dictionary<String, Integer> map  = new Hashtable<>();
        try {
            var hufdict = HuffmanIOHelper.readHuffman(FilePaths.SAM_COMPRESSED_DIRECTORY + filename + FilePaths.AM_DICTIONARY_EXTENSION);
            var amudict = new HuffmanDecoder().decode(hufdict);
            var sb = new StringBuilder();
            for (String x : amudict) {
                if (x.equals("\n")) {
                    dictionary.put(cnt, sb.toString());
                    map.put(sb.toString(), cnt++);
                    sb = new StringBuilder();
                } else sb.append(x);
            }
            var hufmat = HuffmanIOHelper.readHuffman(FilePaths.SAM_COMPRESSED_DIRECTORY + filename + FilePaths.SAM_MATRIX_EXTENSION);
            var encoded = new ArrayList<Integer>();
            var lzwfile = new HuffmanDecoder().decode(hufmat);
            for (String x : lzwfile) encoded.add(Integer.parseInt(x));
            //var amumat = new HuffmanDecoder().decode(hufmat);
            String sparse = LZW.decode(encoded);
            return new AdjMatrix(sparse, dictionary);
        } catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    public void saveAsGraphML(String filepath, boolean directed) {
        try(FileWriter fw = new FileWriter(filepath)) {
            // Add header with XML Schema Reference
            fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            fw.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");
            fw.write(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            fw.write(" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns");
            fw.write(" http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n");
            fw.write("  <graph edgedefault=\"" + ((!directed) ? "un" : "") + "directed\">\n");

            // Write out nodes and edges
            for (int i = 0; i < numVertices; i++) fw.write("    <node id=\"" + vertexLookup.get(i) + "\"/>\n");
            for (int r = 0; r < numVertices; r++)
                for (int c = 0; c < numVertices; c++)
                    if (adjMatrix[r][c]) fw.write("    <edge source=\"" + vertexLookup.get(r) + "\" target=\"" + vertexLookup.get(c) + "\"/>\n");
            // TODO: Add "data" elements to preserve vertex data and edge weights

            // Close XML
            fw.write("  </graph>\n");
            fw.write("</graphml>");
        } catch (Exception e) { e.printStackTrace(); }
    }

  }