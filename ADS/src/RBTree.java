
public class RBTree<Key extends Comparable<Key>,Value> {
	private Node root;
	
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private class Node {
		private Key key;
		private Value value;
		private boolean color;
		private int N;
		private Node left,right;
		
		public Node(Key key,Value value, boolean color, int N) {
			this.key = key;
			this.value = value;
			this.color = color;
			this.N = N;
		}
	} 
	
	public RBTree() {}
	
	//return size of subtree rooted at Node node
	public int size(Node node) {
		return node != null ?  node.N :  0;
	}
	
	public boolean isRed(Node node) {
		return node != null ? node.color == RED : false;
	}
	
	public Value get(Key key) {
		if(key == null) throw new NullPointerException("key is null");
		
		return get(root,key);
	}
	
	public Value get(Node node,Key key) {
		while(node != null) {
			int compare = key.compareTo(node.key);
			if(compare == 0) return node.value;
			else if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	public boolean contains(Key key) {
		return get(key) != null;
	}
	
	//inserting the node into tree
	public void insert(Key key, Value val) {
		if(key == null) throw new NullPointerException("null key is provided");
		
		if(val == null) {
			delete(key);
			return;
		}
		
		root = insert(root,key,val);
		root.color = BLACK;
	}
	
	public Node insert(Node node,Key key,Value val) {
		if(node == null) return new Node(key,val,RED,1);
		
		int compare = key.compareTo(node.key);
		if(compare < 0) node.left = insert(node.left,key,val);
		else if(compare > 0) node.right = insert(node.right,key,val);
		else node.value = node.value + val;
		
	}
	
}
