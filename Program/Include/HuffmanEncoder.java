package Program.Include;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanEncoder {

	private ArrayList<String> msg;
	private ArrayList<String> alphabet;
	private HashMap<String, String> mappingTable;

	public Huffman encode(ArrayList<String> msg) throws Exception {
		this.msg = msg;
		mappingTable = new HashMap<>();
		createTable(buildTree(), "");
		StringBuilder bitString = new StringBuilder();
		for (String x : msg)
			bitString.append(mappingTable.get(x));
		return new Huffman(bitString.toString(), mappingTable);
	}

	private BinaryTreeNode buildTree() throws Exception {
		HashMap<String, Integer> freqMap = HelperFunctions.getFrequencies(msg);
		alphabet = HelperFunctions.getSortedAlphabet(msg);
		PriorityQueue<BinaryTreeNode> pq = new PriorityQueue<>();
		int id = 0;
		for (String c : alphabet) {
			BinaryTreeNode btNode = new BinaryTreeNode(id++, freqMap.get(c));
			pq.offer(btNode);
		}
		while (pq.size() > 1) {
			BinaryTreeNode min = pq.remove();
			BinaryTreeNode secondMin = pq.remove();
			BinaryTreeNode parent = new BinaryTreeNode(-1, min.value + secondMin.value);
			parent.left = min;
			parent.right = secondMin;
			pq.offer(parent);
		}
		return pq.remove();
	}

	private void createTable(BinaryTreeNode node, String encoding) {
		if (node.left == null && node.right == null)
			mappingTable.put(alphabet.get(node.id), encoding);
		else {
			if (node.left != null)
				createTable(node.left, encoding + "0");
			if (node.right != null)
				createTable(node.right, encoding + "1");
		}
	}
}
