package rbbst;

/*
 *author: Chennkesava Ijjada  
 */

public class RBBST {
	//root of redblack tree
	private Node root;
	
	//boolean color flags
	private static final boolean Red = true;
	private static final boolean Black = false;
	
	public RBBST() {}
	
//	void buildFromSorted (int size, Iterator<EventCounter.Event> it) {
//        this.size = size;
//        root = buildFromSorted(0, 0, size-1, computeRedLevel(size), it);
//    }
	
	public void buildTree(int n, int[] keys, int[] values) {
		int rLevel = (int) Math.ceil(Math.log(n+1)/Math.log(2)) ;
		root = createRBTree(0, n-1, 0,rLevel, keys, values);
		
	}
	
	private Node createRBTree(int low,int high, int currLevel,int Rlevel,int[] keys, int[] values) {
		if(low > high) return null;
		
		int mid = low + (high-low)/2;
		Node node = new Node(keys[mid],values[mid],Black);
		node.left = createRBTree(low,mid-1,currLevel+1,Rlevel,keys,values);
		node.right = createRBTree(mid+1,high,currLevel+1,Rlevel,keys,values);
		
		if(currLevel == Rlevel) node.setColor(Red);
		return node;
	}
	
	//RedNode check
	public boolean isRed(Node node) {
		return node != null ? node.getColor() == Red : false;
	}
	
	//get value for a given key in RBTree, returns value if key exists else 0
	public int get(int key) {
		Node node = root;
		while(node != null) {
			if(node.getKey() == key) 
				return node.getValue();
			else if(node.getKey() > key) 
				node = node.left;
			else 
				node = node.right;
		}
		return 0;
	}
	
	//check whether given key exists in RBTree
	public boolean contains(int key) {
		return get(key) != 0;
	}
	
	//check whether RBTree is empty
	private boolean isEmpty(){
		return root == null;
	}
	
	//logic for reducing the count for given key
	public void reduce(int key,int val) {
		if(get(key) == 0) return;
		Node node = root;
		while(node != null) {
			if(node.getKey() == key) {
				int k = node.getValue() - val;
				node.setValue(k);
				if(k <= 0) {
					delete(key);
				}
				return;
			} else if(node.getKey() > key) 
				node = node.left;
			else 
				node = node.right;
		}
	}
		
 	//inserting a new key,value into RBTree
	public void insert(int key, int val) {		
		root = insert(root,key,val);
		root.setColor(Black);
	}
	
	//inserting key,val into RBTree with rotations and balancing logic
	private Node insert(Node node,int key,int val) {
		if(node == null) return new Node(key,val,Red);
		
		if(node.getKey() > key) 
			node.left = insert(node.left,key,val);
		else if(node.getKey() < key) 
			node.right = insert(node.right,key,val);
		else 
			node.setValue(node.getValue() + val);
		
		if(isRed(node.right) && !isRed(node.left)) 
			node = rotateToLeft(node);
		if(isRed(node.left) && isRed(node.left.left)) 
			node = rotateToRight(node);
		if(isRed(node.left) && isRed(node.right)) 
			changeColors(node);
		
		//node.setN(size(node.right) + size(node.left) + 1); 
		return node;
	}
	
	//logic for leftRotation
	private Node rotateToLeft(Node node) {
		Node tmp = node.right;
		node.right = tmp.left;
		
		tmp.left = node;
		tmp.setColor(tmp.left.getColor());
		tmp.left.setColor(Red);
		return tmp;
	}
	
	//logic for rightRotation
	private Node rotateToRight(Node node) {
		Node tmp = node.left;
		node.left = tmp.right;
		
		tmp.right = node;
		tmp.setColor(tmp.right.getColor());
		tmp.right.setColor(Red);
		return tmp;
	}
	
	//logic for changing the color of the given node
	private void changeColors(Node node) {
		node.setColor(!node.getColor());
		node.left.setColor(!node.left.getColor());
		node.right.setColor(!node.right.getColor());
	}
	
	//for deleting a given key from RBTree
	public void delete(int key) {
		if(!contains(key)) return;
		
		if(!(isRed(root.left) || isRed(root.right))) 
			root.setColor(Red);
		
		root = delete(root,key);
		if(!isEmpty()) root.setColor(Black);
	}
	
	//logic for deletion of key with node removal handling 
	private Node delete(Node node, int key) {
		if(key < node.getKey()) {
			if(!(isRed(node.left) || isRed(node.left.left)))
				node = moveRedNodeLeft(node);
			node.left = delete(node.left,key);
		} else {
			if(isRed(node.left))
				node = rotateToRight(node);
			
			if(key == node.getKey() && node.right == null) 
				return null;
			
			if(!(isRed(node.right) || isRed(node.right.left)))
				node = moveRedNodeRight(node);
			
			if(key > node.getKey()) {
				node.right = delete(node.right,key);
			} else {
				Node tmp = minNode(node.right);
				node.setKey(tmp.getKey());
				node.setValue(tmp.getValue());
				node.right = deleteMinNode(node.right);
			}
		}
		
		return balance(node);
	}
	
	//logic for balancing of node colors and rotation
	private Node balance(Node node) {
		if(isRed(node.right)) 
			node = rotateToLeft(node);
		
		if(isRed(node.left) && isRed(node.left.left)) 
			node = rotateToRight(node);
		
		if(isRed(node.left) && isRed(node.right)) 
			changeColors(node);
		
		return node;
	}
	
	//logic for moving a red node to left
	private Node moveRedNodeLeft(Node node) {
		changeColors(node);
		if(isRed(node.right.left)) {
			node.right = rotateToRight(node.right);
			node = rotateToLeft(node);
			changeColors(node);
		}
		return node;
	}
	
	//logic for moving a red node to right
	private Node moveRedNodeRight(Node node) {
		changeColors(node);
		if(isRed(node.left.left)) {
			node = rotateToRight(node);
			changeColors(node);
		}
		return node;
	}
	
	//returns the node with minimum key under given specified node
	private Node minNode(Node node) {
		if(node.left == null) 
			return node;
		else 
			return minNode(node.left);
	}
	
	//returns the node with maximum key under given specified node
	private Node maxNode(Node node) {
		if(node.right == null) 
			return node;
		else 
			return maxNode(node.right);
	}
	
	//deletes the node with minimum Key and return the new root
	private Node deleteMinNode(Node node) {
		if(node.left == null)
			return null;
		if(!(isRed(node.left) || isRed(node.left.left)))
			node = moveRedNodeLeft(node);
		
		node.left = deleteMinNode(node.left);
		
		return balance(node);
	}
	
	//returns the total count of values for a given range of keys low and high
	public int range(int low, int high) {
		if(high < low) 
			return 0;
		
		return range(root,low,high);
	}
	
	private int range(Node node,int low, int high) { 
	  if (node == null) return 0;
	  int k = 0;
	  if(low < node.getKey()) k = range(node.left,low,high);
	  if(low <= node.getKey() && high >= node.getKey()) k += node.getValue();
	  if(high > node.getKey()) k += range(node.right,low,high);
	  
	  return k;
	} 
	
	// returns node with largest key before the given key as array with [key,value]
	public int[] previous(int key) {
		int[] result = new int[2];
		Node node = previous(root,key,null);
		if(node != null) {
			result[0] = node.getKey();
			result[1] = node.getValue();
		}
		
		return result;
	}
	
	// supporting method for above previous function
	private Node previous(Node node, int key, Node p) {
	    if (node == null) return p;
	    
	    if (node.getKey() > key) {
	        return previous(node.left, key, p);
	    } else if (node.getKey() < key) {
	        return previous(node.right, key, node);
	    }
	    
	    if (node.left != null) {
	        return maxNode(node.left);
	    }
	    
	    return p;
	}
	
	// returns node with smallest key after the given key as array with [key,value]
	public int[] next(int value) {
		int[] result = new int[2];
	    Node node = next(root, value, null);
	    if (node != null) {
	       result[0] = node.getKey();
	       result[1] = node.getValue();
	    }
	    
	    return result;
	}
	
	//supporting method for above next method
	private Node next(Node node, int key, Node p) {
	    if (node == null) return p;
	    
	    if (node.getKey() > key) {
	        return next(node.left, key, node);
	    } else if (node.getKey() < key) {
	        return next(node.right, key, p);
	    }
	    
	    if (node.right != null) {
	        return minNode(node.right);
	    }
	    return p;
	}
	
}

//Node class and its properties
class Node {
	private int key;
	private int value;
	private boolean color;
	Node left,right;
	
	//initializing a node with a key,value,color and size N
	public Node(int key,int value, boolean color) {
		this.key = key;
		this.value = value;
		this.color = color;
	}
	
	//get node's key
	public int getKey() {
		return key;
	}
	
	//set node's key
	public void setKey(int key) {
		this.key = key;
	}
	
	//get the value of the node
	public int getValue() {
		return value;
	}
	
	//set the value for the node
	public void setValue(int val) {
		this.value = val;
	}
	
	//get the color of the node
	public boolean getColor() {
		return color;
	}
	
	//set color for the node
	public void setColor(boolean color) {
		this.color = color;
	}
} 

