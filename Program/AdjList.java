package Program;
import java.io.FileWriter;
import java.util.*;

class GraphNode<T> implements Comparable<T> {
    private String id;
    private T data;
    private ArrayList<GraphNode<T>> neighbors;

    public GraphNode(String id, T data) {
        this.id = id;
        this.data = data;
        this.neighbors = new ArrayList<GraphNode<T>>();
    }

    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    public T getData() { return this.data; }
    public void setData(T data) { this.data = data; }
    public boolean hasNeighbor(GraphNode<T> node) { return this.neighbors.contains(node); }
    public void addNeighbor(GraphNode<T> node) { if (!this.neighbors.contains(node)) this.neighbors.add(node); }
    public void removeNeighbor(GraphNode<T> node) { this.neighbors.remove(node); }
    public ArrayList<GraphNode<T>> getNeighbors() { return this.neighbors; }
    public int compareTo(GraphNode<T> g) { return this.id.compareTo(g.id); }

    public String toString() {
        StringBuilder neighborArray = new StringBuilder("[ ");
        for (GraphNode<T> x : neighbors) neighborArray.append(x.id).append(" ");
        neighborArray.append("]");
        return "Id: " + this.id + ((this.data != null) ? "\tData: " + this.data : "") + "\tNeighbors: " + neighborArray.toString();
    }

    @Override
    public int compareTo(T o) {
        return this.compareTo((GraphNode<T>)o);
    }
}

public class AdjList<T> {
    private ArrayList<GraphNode<T>> vertices;

    private GraphNode<T> getVertex(String id) {
        for (GraphNode<T> vertex : this.vertices) {
            if (vertex.getId().equals(id)) return vertex;
        }
        return null;
    }

    private static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        ArrayList<T> newList = new ArrayList<T>();
        for (T element : list) {
            if (!newList.contains(element)) newList.add(element);
        }
        return newList;
    }
    
    public AdjList() { this.vertices = new ArrayList<GraphNode<T>>(); }
    public AdjList(ArrayList<GraphNode<T>> vertices) { this.vertices = vertices; }

    public static AdjList constructFromGraphML(String filepath, boolean undirected) {
        return DataConverters.getAdjListFromGraph(DataConverters.buildGraphFromGraphML(filepath), null, undirected);
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
            for (var vertex : vertices) fw.write("    <node id=\"" + vertex.getId() + "\"/>\n");
            for (var vertex : vertices)
                for (var neighbor : vertex.getNeighbors())
                    fw.write("    <edge source=\"" + vertex.getId() + "\" target=\"" + neighbor.getId() + "\"/>\n");
            // TODO: Add "data" elements to preserve vertex data and edge weights

            // Close XML
            fw.write("  </graph>\n");
            fw.write("</graphml>");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void addVertex(String id, T data) {
        if (contains(id)) return;
        this.vertices.add(new GraphNode<T>(id, data));
    }

    public void addEdgeForward(String idFrom, String idTo) {
        GraphNode<T> vertexA = getVertex(idFrom);
        GraphNode<T> vertexB = getVertex(idTo);
        if (vertexA == null || vertexB == null) return;
        vertexA.addNeighbor(vertexB);
    }

    public void addEdgeBackward(String idFrom, String idTo) {
        GraphNode<T> vertexA = getVertex(idFrom);
        GraphNode<T> vertexB = getVertex(idTo);
        if (vertexA == null || vertexB == null) return;
        vertexB.addNeighbor(vertexA);
    }

    public void addEdge(String idFrom, String idTo) {
        addEdgeForward(idFrom, idTo);
        addEdgeBackward(idFrom, idTo);
    }

    public void removeVertex(String id) {
        GraphNode<T> vertex = getVertex(id);
        if (vertex == null) return;
        vertices.remove(vertex);
    }
    
    public void removeEdge(String idFrom, String idTo) {
        GraphNode<T> vertexA = getVertex(idFrom);
        GraphNode<T> vertexB = getVertex(idTo);
        if (vertexA == null || vertexB == null) return;
        vertexA.removeNeighbor(vertexB);
        vertexB.removeNeighbor(vertexA);
    }

    public T getNodeData(String id) {
        GraphNode<T> node = getVertex(id);
        if (node != null) return node.getData();
        else return null;
    }

    public void setNodeData(String id, T data) {
        GraphNode<T> node = getVertex(id);
        if (node != null) node.setData(data);
    }

    public void print() { for (GraphNode<T> x : vertices) System.out.println(x.toString()); }
    public ArrayList<GraphNode<T>> getList() { return vertices; }
    public boolean contains(String vertexId) { return getVertex(vertexId) != null; }
    public void clear() { vertices.clear(); }
    public int size() { return vertices.size(); }
    public boolean isEmpty() { return (this.size() == 0); }
    public boolean pathExists(String idFrom, String idTo) { return distanceBetween(idFrom, idTo) > 0; }

    public int distanceBetween(String idFrom, String idTo) { //Dijkstra
        GraphNode<T> vertexA = getVertex(idFrom);
        GraphNode<T> vertexB = getVertex(idTo);
        if (vertexA == null || vertexB == null) return 0;
        ArrayList<GraphNode<T>> visited = new ArrayList<>();
        Queue<GraphNode<T>> queue = new Queue<GraphNode<T>>();
        int distance = 0;
        queue.enqueue(vertexA);
        while (!queue.isEmpty()) {
            GraphNode<T> current = queue.dequeue();
            visited.add(current);
            distance++;
            for (GraphNode<T> currentNeighbor : current.getNeighbors()) {
                if (currentNeighbor == vertexB) return distance;
                if (!visited.contains(currentNeighbor)) queue.enqueue(currentNeighbor);
            }
        }
        return 0;
    }

    public void depthFirstSearch(String idFrom, String idTo) { //DFS
        GraphNode<T> vertexA = getVertex(idFrom);
        GraphNode<T> vertexB = getVertex(idTo);
        if (vertexA == null || vertexB == null) return;
        ArrayList<GraphNode<T>> visited = new ArrayList<>();
        Stack<GraphNode<T>> stack = new Stack<>();
        stack.push(vertexA);
        boolean founder = false;
        while (!stack.isEmpty() && !founder) {
            GraphNode<T> current = stack.pop();
            visited.add(current);
            if (current.getNeighbors().contains(vertexB)) founder = true;
            for (GraphNode<T> currentNeighbor : current.getNeighbors()) {
                if (currentNeighbor == vertexB) founder = true;
                for (GraphNode<T> neighborFriends : currentNeighbor.getNeighbors())
                    if (!visited.contains(neighborFriends)) stack.push(neighborFriends);
                if (!visited.contains(currentNeighbor)) stack.push(currentNeighbor);
            }
        }
        System.out.print("DFS: ");
        for (int i = 0; i < visited.size(); i++) System.out.print(visited.get(i).getId() + "-->");
        System.out.println(idTo);
    }

    public AdjList<T> breadthFirstSearch(String idFrom, String idTo, boolean print) { //BFS
        GraphNode<T> vertexA = getVertex(idFrom);
        GraphNode<T> vertexB = getVertex(idTo);
        if (vertexA == null || vertexB == null) return null;
        ArrayList<GraphNode<T>> visited = new ArrayList<>();
        Queue<GraphNode<T>> queue = new Queue<GraphNode<T>>();
        queue.enqueue(vertexA);
        boolean founder = false;
        while (!queue.isEmpty()) { // && !founder
            GraphNode<T> current = queue.dequeue();
            visited.add(current);
            for (GraphNode<T> currentNeighbor : current.getNeighbors()) {
                if (currentNeighbor == vertexB) founder = true;
                if (!visited.contains(currentNeighbor)) queue.enqueue(currentNeighbor);
            }
        }
        if (print) {
            System.out.print("BFS: ");
            for (int i = 0; i < visited.size(); i++) System.out.print(visited.get(i).getId() + "-->");
            System.out.println(idTo);
        }
        //if (returnBFStree) {
            /*ArrayList<GraphNode<T>> BFSreduced = new ArrayList<>();
            System.out.println("Adding vertices");
            for (GraphNode<T> tGraphNode : visited) BFSreduced.add(new GraphNode<T>(tGraphNode.getId(), null));
            System.out.println("Adding edges");
            for (int i = 0; i < BFSreduced.size() - 1; i++) BFSreduced.get(i).addNeighbor(BFSreduced.get(i + 1));
            System.out.println("Filtering edges");
            for (int i = 0; i < BFSreduced.size(); i++) {
                for (int j = 1; j < BFSreduced.size(); j++) {
                    if (BFSreduced.get(i) != null && BFSreduced.get(j) != null)
                        if (BFSreduced.get(j).getId().equals(BFSreduced.get(i).getId()) && j != i) {
                            for (var neighbor : BFSreduced.get(j).getNeighbors()) {
                                BFSreduced.get(i).addNeighbor(neighbor);
                            }
                            if (j == BFSreduced.size() - 1 && BFSreduced.get(j) != null && BFSreduced.get(j-1) != null) BFSreduced.get(j - 1).addNeighbor(BFSreduced.get(j));
                            if (BFSreduced.get(j) != null) BFSreduced.set(j, null);
                        }
                }
            }
            BFSreduced.removeAll(Collections.singleton(null));
            return new AdjList<T>(BFSreduced);*/
        return new AdjList<T>(visited);
        //}
        //return null;
    }

    public AdjList<T> breadthFirstSearch(boolean print) {
        return breadthFirstSearch(vertices.get(0).getId(), vertices.get(vertices.size() / 2).getId(), print);
    }
}