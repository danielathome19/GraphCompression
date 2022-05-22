package Program.Include;

public class BinaryTreeNode implements Comparable<BinaryTreeNode> {

	int id;
	int value;
	BinaryTreeNode left, right;

	BinaryTreeNode(int id, int value) {
		this.id = id;
		this.value = value;
		this.left = null;
		this.right = null;
	}

	@Override
	public int compareTo(BinaryTreeNode that) {
		return this.value - that.value;
	}
}
