package Program;
import java.io.*;
import java.util.*;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;
import static java.lang.System.out;

// Convert CSVs to GraphML format for uniformity
public class DataConverters {
    public static void CSVtoGML(String filepath, int fromCol, int toCol, boolean directed, boolean skipRowOne) {
        Set<String> vertices = new HashSet<String>();
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (skipRowOne) {
                    skipRowOne = false;
                    continue;
                }
                String[] values = line.split(",");
                records.add(values);
                for (int i = 0; i < values.length; i++) values[i] = (values[i].matches("[0-9]+")) ? values[i] : String.valueOf(values[i].hashCode());
            }
        } catch (Exception e) { e.printStackTrace(); }

        // Collect all vertices
        for (String[] record : records) {
            vertices.add(record[fromCol]);
            vertices.add(record[toCol]);
        }

        // Write out graphml file
        String outfilepath = filepath.substring(0, filepath.length() - 4) + ".graphml";
        try(FileWriter fw = new FileWriter(outfilepath)) {
            // Add header with XML Schema Reference
            fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            fw.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"");
            fw.write(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
            fw.write(" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns");
            fw.write(" http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n");
            fw.write("  <graph edgedefault=\"" + ((!directed) ? "un" : "") + "directed\">\n");

            // Write out nodes and edges
            for (String vertex : vertices) fw.write("    <node id=\"" + vertex + "\"/>\n");
            for (String[] record : records) fw.write("    <edge source=\"" + record[fromCol] + "\" target=\"" + record[toCol] + "\"/>\n");
            // TODO: Add "data" elements to preserve vertex data and edge weights

            // Close XML
            fw.write("  </graph>\n");
            fw.write("</graphml>");
		} catch (Exception e) { e.printStackTrace(); }
    }

    public static TinkerGraph buildGraphFromGraphML(String filepath) {
        var graph = new TinkerGraph();
        GraphMLReader reader = new GraphMLReader(graph);
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(filepath));
            reader.inputGraph(is);
            is.close();
            // TODO: graphs are always read in as directed regardless of edgedefault property
        } catch (Exception e) { e.printStackTrace(); }
        return graph;
    }

    public static void printTinkerGraph(TinkerGraph graph) {
        Iterable<Vertex> vertices = graph.getVertices();
        for (Vertex vertex : vertices) {
            Iterable<Edge> edges = vertex.getEdges(Direction.IN);
            for (Edge edge : edges) {
                Vertex inVertex = edge.getVertex(Direction.IN);
                Vertex outVertex = edge.getVertex(Direction.OUT);
                String idin = (String)inVertex.getId();
                String idout = (String)outVertex.getId();
                out.println("Edge from " + idin + " to " + idout);
            }
        }
    }

    public static <T> AdjList<T> getAdjListFromGraph(TinkerGraph graph, T value, boolean undirected) {
        Iterable<Vertex> vertices = graph.getVertices();
        var adjList = new AdjList<T>();
        for (Vertex vertex : vertices) {
            adjList.addVertex((String)vertex.getId(), value);
            Iterable<Edge> edges = vertex.getEdges(Direction.BOTH);
            for (Edge edge : edges) {
                Vertex inVertex = edge.getVertex(Direction.IN);
                Vertex outVertex = edge.getVertex(Direction.OUT);
                String idin = (String)inVertex.getId();
                String idout = (String)outVertex.getId();
                adjList.addEdgeForward(idin, idout);
                if (undirected) adjList.addEdgeBackward(idin, idout);
            }
        }
        return adjList;
    }

    public static AdjMatrix getAdjMatrixFromGraph(TinkerGraph graph, boolean undirected) {
        return new AdjMatrix(graph, undirected);
    }
}
