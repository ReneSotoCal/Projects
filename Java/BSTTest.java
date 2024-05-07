
import java.util.ArrayList;

public class BSTTest {
	
	private static int index = 0;//index for the fillArray method
	
	public static void main(String[] args) {//Main Method
		
		// Declaring the different array sizes
//		int[] arr = new int[100];
//		int[] arr = new int[1000];
		int[] arr = new int[10000];
	
		//Populating the arrays
//		populateAsc(arr); // Ascending order
//		populateDesc(arr); // Descending order
//		populateRand(arr); // Random order
		fillArray(arr, 0, arr.length-1); //Special Order
		
		BST bst = populateBST(arr);// Instantiating and populating the Binary Search Tree

		//Print the Array size, Original Array, Sorted Array, Key Comparisons
		System.out.println("Array length:	" + arr.length);
		printOrigArr(arr);
		printBST(bst);
		System.out.println("Key Comparisons: " + BST.keyCounter);
	}
	
	//Populate the Binary Search Tree
	public static BST populateBST(int[] arr) {
	
		BST bst = new BST(); //Instantiating a binary search tree

		//Loop through the array to add a new node for each element to the BST
		for(int i = 0; i < arr.length; i++)			
			bst.treeInsert(bst.getRoot(), bst.new Node(arr[i]));
		
		return bst; //Returning the completed BST
	}
	
	//Printing the sorted array
	public static void printBST(BST bst) {
		
		//Sort the array using an inorder tree walk
		bst.inorderTreeWalk(bst.getRoot(), 0);
		System.out.print("Sorted Array:	");
		
		//Printing the first 20 elements of the sorted array
		for(int i = 0; i < 20; i++)
			System.out.print(bst.sortedArr.get(i) + " ");
		
		System.out.println();
	}
	
	//Populate an array with descending numbers
	public static void populateDesc(int arr[]) {
		int j = 0;
		for(int i = arr.length; i > 0; i--) {
			arr[j] = i;
			j++;
		}
	}
	
	//Populate an array with ascending numbers
	public static void populateAsc(int arr[]) {
		
		for(int i = 1; i <= arr.length; i ++) 
			arr[i-1] = i;
	}
	
	//Populate an array with random numbers from 1 - 100000
	public static void populateRand(int arr[]) {
		
		for(int i = 0; i < arr.length; i++) 
			arr[i] = 1 + (int)(Math.random() * 100000);
	}
	
	//Populate the array with the algorithm presented in the assignment
	public static void fillArray(int[] arr, int low, int high) {
		if (low <= high) {
			int mid = (low + high) / 2; 
			arr[index++] = mid + 1;
			fillArray(arr, low, mid - 1); 
			fillArray(arr, mid + 1, high);
		}
	}

	//Printing the first 20 elements of the array
	public static void printOrigArr(int[] arr) {
		
		System.out.print("Original Array:	");
		for(int i = 0; i < 20; i++) 
			System.out.print(arr[i] + " ");
		
		System.out.println();
	}
	
}

//Binary Search Tree Class
class BST {

	//Inner Node Class
	public class Node {
		
		//Declaring node elements
		private int key;
		private Node left, right, parent;
		
		//Public Constructor for Node
		public Node(int key) {
			setKey(key);
			setLeft(left);
			setRight(right);
			setParent(parent);
		}
		
		//Getters and Setters
		public void setKey(int key) {
			this.key = key;
		}
		
		public int getKey() {
			return this.key;
		}
		
		public void setLeft(Node left) {
			if(left != null)
				this.left = left;
			else
				this.left = null;
		}
		
		public Node getLeft() {
			return this.left;
		}
		
		public void setRight(Node right) {
			if(right != null)
				this.right = right;
			else
				this.right = null;
		}
		
		public Node getRight() {
			return this.right;
		}
		
		public void setParent(Node parent) {
			if(parent != null)
				this.parent = parent;
			else
				this.parent = null;
		}
		
		public Node getParent() {
			return this.parent;
		}
	}
	
	static int keyCounter = 0;//Key Comparison counter
	private Node root; // Root Node
	public ArrayList<Integer> sortedArr = new ArrayList<>(); //Array List to record the sorted array
	
	//No-arg Convenience Constructor
	public BST() {
		setRoot(null);//Instantiating the root node
	}
	
	//Getters and Setters
	public void setRoot(Node root) {
		this.root = root;
	}
	
	public Node getRoot() {
		return this.root;
	}
		
	//Inserting new nodes into the Binary Search Tree
	public void treeInsert(Node parent, Node newNode) {
		
		//If there is no root node, set the new node as the root node
		if(parent == null) {
			setRoot(newNode);
			return;
		}
		
		//if the new node's key is smaller than it's parent then traverse left 
		if(newNode.getKey() < parent.getKey()) {
			if(parent.getLeft() == null) {// If there is no left child set the new node as left child
				parent.setLeft(newNode);
				newNode.setParent(parent);
			} else
				treeInsert(parent.getLeft(), newNode); //Recursively call treeInsert with the left child as parent
		
		} else { //The new node's key is the same or bigger than it's parent, traverse right 
			if(parent.getRight() == null) {//Check for a right child
				parent.setRight(newNode);
				newNode.setParent(parent);
			} else //Traverse to the right child
				treeInsert(parent.getRight(), newNode);//Recursively call treeInsert with the right child as parent
			
		} keyCounter++;//Increment Key Comparisons by 1
	}
	
	//Traverse the tree from leftmost node to rightmost
	public void inorderTreeWalk(Node root, int counter) {
		
		//Limit the node traversal to 20 to prevent stack overflow in extreme cases
		if(root != null && counter < 20) {//If the BST is not empty traverse it
			
			inorderTreeWalk(root.getLeft(), counter+1);//Recursively walk down the left nodes
			sortedArr.add(root.getKey()); //Add the node's key into the sorted array
			inorderTreeWalk(root.getRight(), counter+1); //Recursively walk down the right nodes
			
		} 
	}
}