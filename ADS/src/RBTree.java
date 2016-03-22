import java.util.LinkedList;
import java.util.Queue;

public class RBTree {
	private Node root;
	
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private class Node {
		private int key;
		private int value;
		private boolean color;
		private int N;
		private Node left,right;
		
		public Node(int key,int value, boolean color, int N) {
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
	
	public int get(int key) {
		return get(root,key);
	}
	
	public int get(Node node,int key) {
		while(node != null) {
			if(node.key == key) return node.value;
			else if(node.key > key) node = node.left;
			else node = node.right;
		}
		return 0;
	}
	
	public boolean contains(int key) {
		return get(key) != 0;
	}
	
	private boolean isEmpty(){
		return root == null;
	}
	
 	//inserting the node into tree
	public void insert(int key, int val) {		
		root = insert(root,key,val);
		root.color = BLACK;
	}
	
	public void reduce(int key,int val) {
		if(get(key) == 0) return;
		reduce(root,key,val);
	}
	
	public void reduce(Node node,int key, int val) {
		while(node != null) {
			if(node.key == key) {
				node.value -= val;
				if(node.value <= 0) {
					delete(key);
				}
				return;
			} else if(node.key > key) 
				node = node.left;
			else 
				node = node.right;
		}
	}
	
	private Node insert(Node node,int key,int val) {
		if(node == null) return new Node(key,val,RED,1);
		
		if(node.key > key) 
			node.left = insert(node.left,key,val);
		else if(node.key < key) 
			node.right = insert(node.right,key,val);
		else 
			node.value = node.value + val;
		
		if(isRed(node.right) && !isRed(node.left)) node = rotateToLeft(node);
		if(isRed(node.left) && isRed(node.left.left)) node = rotateToRight(node);
		if(isRed(node.left) && isRed(node.right)) changeColors(node);
		
		node.N = size(node.right) + size(node.left) + 1; 
		return node;
	}
	
	private Node rotateToLeft(Node node) {
		Node x = node.right;
		node.right = x.left;
		
		x.left = node;
		x.color = x.left.color;
		x.left.color = RED;
		x.N = node.N;
		node.N = size(node.left) + size(node.right) + 1;
		return x;
	}
	
	private Node rotateToRight(Node node) {
		Node x = node.left;
		node.left = x.right;
		
		x.right = node;
		x.color = x.right.color;
		x.right.color = RED;
		x.N = node.N;
		node.N = size(node.left) + size(node.right) + 1;
		return x;
	}
	
	private void changeColors(Node node) {
		node.color = !node.color;
		node.left.color = !node.left.color;
		node.right.color = !node.right.color;
	}
	
	public void delete(int key) {
		if(!contains(key)) return;
		
		if(!(isRed(root.left) || isRed(root.right))) root.color = RED;
		
		root = delete(root,key);
		if(!isEmpty()) root.color = BLACK;
	}
	
	private Node delete(Node node, int key) {
		if(key < node.key) {
			if(!(isRed(node.left) || isRed(node.left.left)))
				node = moveRedNodeLeft(node);
			node.left = delete(node.left,key);
		} else {
			if(isRed(node.left))
				node = rotateToRight(node);
			
			if(key == node.key && node.right == null) 
				return null;
			
			if(!(isRed(node.right) || isRed(node.right.left)))
				node = moveRedNodeRight(node);
			
			if(key > node.key) {
				node.right = delete(node.right,key);
			} else {
				Node x = minNode(node.right);
				node.key = x.key;
				node.value = x.value;
				node.right = deleteMinNode(node.right);
			}
		}
		
		return balance(node);
		
	}
	
	private Node balance(Node node) {
		if(isRed(node.right)) node = rotateToLeft(node);
		if(isRed(node.left) && isRed(node.left.left)) node = rotateToRight(node);
		if(isRed(node.left) && isRed(node.right)) changeColors(node);
		
		node.N = size(node.left) + size(node.right) + 1;
		return node;
	}
	
	private Node moveRedNodeLeft(Node node) {
		changeColors(node);
		if(isRed(node.right.left)) {
			node.right = rotateToRight(node.right);
			node = rotateToLeft(node);
			changeColors(node);
		}
		return node;
	}
	
	private Node moveRedNodeRight(Node node) {
		changeColors(node);
		if(isRed(node.left.left)) {
			node = rotateToRight(node);
			changeColors(node);
		}
		return node;
	}
	
	private Node minNode(Node node) {
		if(node.left == null) return node;
		else return minNode(node.left);
	}
	
	private Node maxNode(Node node) {
		if(node.right == null) return node;
		else return maxNode(node.right);
	}
	
	private Node deleteMinNode(Node node) {
		if(node.left == null)
			return null;
		if(!(isRed(node.left) || isRed(node.left.left)))
			node = moveRedNodeLeft(node);
		
		node.left = deleteMinNode(node.left);
		
		return balance(node);
	}
	
	public int range(int low, int high) {
		if(high < low) return 0;
		return range(root,low,high);
	}
	
	private int range(Node node,int low, int high) { 
	  if (node == null) return 0;
	  int k = 0;
	  if(low < node.key) k = range(node.left,low,high);
	  if(low <= node.key && high >= node.key) k += node.value;
	  if(high > node.key) k += range(node.right,low,high);
	  
	  return k;
	} 
	
	public int[] previous(int key) {
		int[] result = new int[2];
		if(isEmpty()) return result;
		
		Node node = pred(root,key,null);
		if(node != null) {
			result[0] = node.key;
			result[1] = node.value;
		}
		
		return result;
		
	}
	
//	private Node previous(Node node,int key) {
//		if(node == null) return null;
//		if(key < node.key) 
//			return previous(node.left, key);
//		
//		Node tmp = previous(node.right,key);
//		if(tmp != null) return tmp;
//		
//		if(node.key == key) 
//			return null;
//		else 
//			return node;
//	}
	
	private Node pred(Node n, Integer x, Node p) {
	    if (n == null) {
	        return p;
	    }
	    
	    if (n.key > x) {
	        return pred(n.left, x, p);
	    } else if (x > n.key) {
	        return pred(n.right, x, n);
	    }
	    
	    if (null != n.left) {
	        return maxNode(n.left);
	    }
	    return p;
	}
	
//	public int[] next(int key) {
//		int[] result = new int[2];
//		if(isEmpty()) return result;
//		
//		Node node = next(root,key);
//		if(node == null) 
//			return result;
//		result[0] = node.key;
//		result[1] = node.value;
//		
//		return result;
//	}
//	
//	private Node next(Node node, int key) {
//		if(node == null) return null;
//		
//		if(key > node.key) 
//			return next(node.right, key);
//		
//		Node tmp = next(node.left,key);
//		if(tmp != null) 
//			return tmp;
//		
//		if(key != node.key) 
//			return node;
//		else
//			return null;
//	}
	
	public int[] next(int value) {
		int[] result = new int[2];
	    Node n = succ(root, value, null);
	    if (null != n) {
	       result[0] = n.key;
	       result[1] = n.value;
	    }
	    return result;
	}

	private Node succ(Node n, Integer x, Node p) {
	    if (n == null) {
	        return p;
	    }
	    
	    if (x < n.key) {
	        return succ(n.left, x, n);
	    } else if (x > n.key) {
	        return succ(n.right, x, p);
	    }
	    
	    if (null != n.right) {
	        return minNode(n.right);
	    }
	    return p;
	}
	
	 public boolean check() {
	        if (!isBST())            System.out.println("Not in symmetric order");
	        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
	      //  if (!isRankConsistent()) System.out.println("Ranks not consistent");
	        if (!is23())             System.out.println("Not a 2-3 tree");
	        if (!isBalanced())       System.out.println("Not balanced");
	        return isBST() && isSizeConsistent() && is23() && isBalanced();
	    }

	    // does this binary tree satisfy symmetric order?
	    // Note: this test also ensures that data structure is a binary tree since order is strict
	    private boolean isBST() {
	        return isBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
	    }

	    // is the tree rooted at x a BST with all keys strictly between min and max
	    // (if min or max is null, treat as empty constraint)
	    // Credit: Bob Dondero's elegant solution
	    private boolean isBST(Node x, int min, int max) {
	        if (x == null) return true;
	        if ( x.key <= min) return false;
	        if ( x.key >= max) return false;
	        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
	    } 
	    
	    private boolean isSizeConsistent() { return isSizeConsistent(root); }
	    private boolean isSizeConsistent(Node x) {
	        if (x == null) return true;
	        if (x.N != size(x.left) + size(x.right) + 1) return false;
	        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
	    } 

	    // Does the tree have no red right links, and at most one (left)
	    // red links in a row on any path?
	    private boolean is23() { return is23(root); }
	    private boolean is23(Node x) {
	        if (x == null) return true;
	        if (isRed(x.right)) return false;
	        if (x != root && isRed(x) && isRed(x.left))
	            return false;
	        return is23(x.left) && is23(x.right);
	    } 

	    // do all paths from root to leaf have same number of black edges?
	    private boolean isBalanced() { 
	        int black = 0;     // number of black links on path from root to min
	        Node x = root;
	        while (x != null) {
	            if (!isRed(x)) black++;
	            x = x.left;
	        }
	        return isBalanced(root, black);
	    }

	    // does every path from the root to a leaf have the given number of black links?
	    private boolean isBalanced(Node x, int black) {
	        if (x == null) return black == 0;
	        if (!isRed(x)) black--;
	        return isBalanced(x.left, black) && isBalanced(x.right, black);
	    } 

	    public Iterable<Integer> keys(int lo, int hi) {
	        Queue<Integer> queue = new LinkedList<Integer>();
	        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
	        keys(root, queue, lo, hi);
	        return queue;
	    } 

	    // add the keys between lo and hi in the subtree rooted at x
	    // to the queue
	    private void keys(Node x, Queue<Integer> queue, int lo, int hi) { 
	        if (x == null) return; 
	        if (lo < x.key) keys(x.left, queue, lo, hi); 
	        if (lo <= x.key && hi >= x.key) queue.add(x.key); 
	        if (hi > x.key) keys(x.right, queue, lo, hi); 
	    } 
}
