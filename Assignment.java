import java.util.ArrayList;
import java.util.HashMap;

import textbook.LinkedBinaryTree;
import textbook.LinkedQueue;
import textbook.Position;

public class Assignment {

	/**
	 * Convert an arithmetic expression (in prefix notation), to a binary tree
	 * 
	 * Binary operators are +, -, * (i.e. addition, subtraction, multiplication)
	 * Anything else is assumed to be a variable or numeric value
	 * 
	 * Example: "+ 2 15" will be a tree with root "+", left child "2" and right
	 * child "15" i.e. + 2 15
	 * 
	 * Example: "+ 2 - 4 5" will be a tree with root "+", left child "2", right
	 * child a subtree representing "- 4 5" i.e. + 2 - 4 5
	 * 
	 * This method runs in O(n) time
	 * 
	 * @param expression
	 *            - an arithmetic expression in prefix notation
	 * @return BinaryTree representing an expression expressed in prefix
	 *         notation
	 * @throws IllegalArgumentException
	 *             if expression was not a valid expression
	 */
	public static LinkedBinaryTree<String> prefix2tree(String expression) throws IllegalArgumentException {
		if (expression == null) {
			throw new IllegalArgumentException("Expression string was null");
		}
		// break up the expression string using spaces, into a queue
		LinkedQueue<String> tokens = new LinkedQueue<String>();
		for (String token : expression.split(" ")) {
			tokens.enqueue(token);
		}
		// recursively build the tree
		return prefix2tree(tokens);
	}
	
	/**
	 * Recursive helper method to build an tree representing an arithmetic
	 * expression in prefix notation, where the expression has already been
	 * broken up into a queue of tokens
	 * 
	 * @param tokens
	 * @return
	 * @throws IllegalArgumentException
	 *             if expression was not a valid expression
	 */
	private static LinkedBinaryTree<String> prefix2tree(LinkedQueue<String> tokens) throws IllegalArgumentException {
		LinkedBinaryTree<String> tree = new LinkedBinaryTree<String>();

		// use the next element of the queue to build the root
		if (tokens.isEmpty()) {
			throw new IllegalArgumentException("String was not a valid arithmetic expression in prefix notation");
		}
		String element = tokens.dequeue();
		tree.addRoot(element);

		// if the element is a binary operation, we need to build the left and
		// right subtrees
		if (element.equals("+") || element.equals("-") || element.equals("*")) {
			LinkedBinaryTree<String> left = prefix2tree(tokens);
			LinkedBinaryTree<String> right = prefix2tree(tokens);
			tree.attach(tree.root(), left, right);
		}
		// otherwise, assume it's a variable or a value, so it's a leaf (i.e.
		// nothing more to do)

		return tree;
	}
	
	/**
	 * Test to see if two trees are identical (every position in the tree stores the same value)
	 * 
	 * e.g. two trees representing "+ 1 2" are identical to each other, but not to a tree representing "+ 2 1"
	 * @param a
	 * @param b
	 * @return true if the trees have the same structure and values, false otherwise
	 */
	public static boolean equals(LinkedBinaryTree<String> a, LinkedBinaryTree<String> b) {
		return equals(a, b, a.root(), b.root());
	}
	
	//private helper method to check whether two subtrees are equal
	private static boolean equalsSubtree(LinkedBinaryTree<String> tree, Position<String> a, Position<String> b) {
		return equals(tree, tree, a, b);
	}

	/**
	 * Recursive helper method to compare two trees
	 * @param aTree one of the trees to compare
	 * @param bTree the other tree to compare
	 * @param aRoot a position in the first tree
	 * @param bRoot a position in the second tree (corresponding to a position in the first)
	 * @return true if the subtrees rooted at the given positions are identical
	 */
	private static boolean equals(LinkedBinaryTree<String> aTree, LinkedBinaryTree<String> bTree, Position<String> aRoot, Position<String> bRoot) {
		//if either of the positions is null, then they are the same only if they are both null
		if(aRoot == null || bRoot == null) {
			return (aRoot == null) && (bRoot == null);
		}
		//first check that the elements stored in the current positions are the same
		String a = aRoot.getElement();
		String b = bRoot.getElement();
		if((a==null && b==null) || a.equals(b)) {
			//then recursively check if the left subtrees are the same...
			boolean left = equals(aTree, bTree, aTree.left(aRoot), bTree.left(bRoot));
			//...and if the right subtrees are the same
			boolean right = equals(aTree, bTree, aTree.right(aRoot), bTree.right(bRoot));
			//return true if they both matched
			return left && right;
		}
		else {
			return false;
		}
	}

	
	/**
	 * Given a tree, this method should output a string for the corresponding
	 * arithmetic expression in prefix notation, without (parenthesis) (also
	 * known as Polish notation)
	 * 
	 * Example: A tree with root "+", left child "2" and right child "15" would
	 * be "+ 2 15" Example: A tree with root "-", left child a subtree
	 * representing "(2+15)" and right child "4" would be "- + 2 15 4"
	 * 
	 * Ideally, this method should run in O(n) time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return prefix notation expression of the tree
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static String tree2prefix(LinkedBinaryTree<String> tree) throws IllegalArgumentException {
		
		if (!isArithmeticExpression(tree)) {
			throw new IllegalArgumentException();
		}
		
		ArrayList<String> list = new ArrayList<>();
		preOrderTraversal(tree.root(), list, tree);
		String listString = String.join(" ", list);
		
		return listString;
	}
	
	// private helper method to run a preOrder traversal and fill passed ArrayList with strings of each element
	private static void preOrderTraversal(Position<String> p, ArrayList<String> snapshot, LinkedBinaryTree<String> tree) {
		
		if (tree.isExternal(p)) {
			snapshot.add(p.getElement());
		}
		//because binary expression tree is proper, each node will have either 0 or 2 children
		else {
			snapshot.add(p.getElement());
			preOrderTraversal(tree.left(p), snapshot, tree);
			preOrderTraversal(tree.right(p), snapshot, tree);
		}
		
	}

	/**
	 * Given a tree, this method should output a string for the corresponding
	 * arithmetic expression in infix notation with parenthesis (i.e. the most
	 * commonly used notation).
	 * 
	 * Example: A tree with root "+", left child "2" and right child "15" would
	 * be "(2+15)"
	 * 
	 * Example: A tree with root "-", left child a subtree representing "(2+15)"
	 * and right child "4" would be "((2+15)-4)"
	 * 
	 * Optionally, you may leave out the outermost parenthesis, but it's fine to
	 * leave them on. (i.e. "2+15" and "(2+15)-4" would also be acceptable
	 * output for the examples above)
	 * 
	 * Ideally, this method should run in O(n) time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return infix notation expression of the tree
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static String tree2infix(LinkedBinaryTree<String> tree) throws IllegalArgumentException {
		
		if (!isArithmeticExpression(tree)) {
			throw new IllegalArgumentException();
		}
		
		ArrayList<String> list = new ArrayList<>();
		inOrderTraversal(tree.root(), list, tree);
		String listString = String.join("", list);
		
		return listString;
		
	}
	
	//private helper method to in order traversal and fill passed ArrayList with nodes' elements in inOrder, separated by parentheses where necessary
	private static void inOrderTraversal(Position<String> p, ArrayList<String> snapshot, LinkedBinaryTree<String> tree) {
		
		//leaf nodes (i.e. integers and variables don't get surrounded by parentheses
		if (tree.isExternal(p)) {
			snapshot.add(p.getElement());
		}
		
		//internal nodes (i.e. operators do get surrounded by parentheses)
		else {
			snapshot.add("(");
			inOrderTraversal(tree.left(p), snapshot, tree);
			snapshot.add(p.getElement());
			inOrderTraversal(tree.right(p), snapshot, tree);
			snapshot.add(")");
		}

	}

	/**
	 * Given a tree, this method should simplify any subtrees which can be
	 * evaluated to a single integer value.
	 * 
	 * Ideally, this method should run in O(n) time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return resulting binary tree after evaluating as many of the subtrees as
	 *         possible
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static LinkedBinaryTree<String> simplify(LinkedBinaryTree<String> tree) throws IllegalArgumentException {
		
		if (!isArithmeticExpression(tree)) {
			throw new IllegalArgumentException();
		}
		
		simplify(tree, tree.root());
		
		return tree;
	}
	
	public static void simplify(LinkedBinaryTree<String> tree, Position<String> p) {
		
		//current node is leaf, so return
		if (tree.isExternal(p)) {
			return;
		}
		
		simplify(tree, tree.left(p));
		simplify(tree, tree.right(p));
		
		//current node is internal
		try {
			int left = Integer.parseInt(tree.left(p).getElement());
			int right = Integer.parseInt(tree.right(p).getElement());
				
			if (p.getElement().equals("+")) {
				tree.set(p, Integer.toString(left + right));
			}
			else if (p.getElement().equals("-")) {
				tree.set(p, Integer.toString(left - right));
			}
			else if (p.getElement().equals("*")) {
				tree.set(p, Integer.toString(left * right));
			}
				
			//because both children are numbers, both children are leaves, and can be freely removed
			tree.remove(tree.left(p));
			tree.remove(tree.right(p));
				
		}catch(NumberFormatException e) {
			//do nothing, because one of the children of current node is a variable; nothing can be done to simplify
		}
			
	}

	/**
	 * This should do everything the simplify method does AND also apply the following rules:
	 *  * 1 x == x  i.e.  (1*x)==x
	 *  * x 1 == x        (x*1)==x
	 *  * 0 x == 0        (0*x)==0
	 *  * x 0 == 0        (x*0)==0
	 *  + 0 x == x        (0+x)==x
	 *  + x 0 == 0        (x+0)==x
	 *  - x 0 == x        (x-0)==x
	 *  - x x == 0        (x-x)==0
	 *  
	 *  Example: - * 1 x x == 0, in infix notation: ((1*x)-x) = (x-x) = 0
	 *  
	 * Ideally, this method should run in O(n) time (harder to achieve than for other methods!)
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return resulting binary tree after applying the simplifications
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression
	 */
	public static LinkedBinaryTree<String> simplifyFancy(LinkedBinaryTree<String> tree) throws IllegalArgumentException {
		
		if (!isArithmeticExpression(tree)) {
			throw new IllegalArgumentException();
		}
		
		simplifyFancy(tree, tree.root());
		return tree;
	}
	
	public static void simplifyFancy(LinkedBinaryTree<String> tree, Position<String> p) {
		
		//current node is leaf, so return
		if (tree.isExternal(p)) {
			return;
		}
				
		simplifyFancy(tree, tree.left(p));
		simplifyFancy(tree, tree.right(p));
		
		//current node is internal, so run simplify rules
		try {
			int left = Integer.parseInt(tree.left(p).getElement());
			int right = Integer.parseInt(tree.right(p).getElement());
								
			if (p.getElement().equals("+")) {
				tree.set(p, Integer.toString(left + right));
			}
			else if (p.getElement().equals("-")) {
				tree.set(p, Integer.toString(left - right));
			}
			else if (p.getElement().equals("*")) {
				tree.set(p, Integer.toString(left * right));
			}
								
			tree.remove(tree.left(p));
			tree.remove(tree.right(p));
								
			}catch(NumberFormatException e) {
				//do nothing, because one of the children of current node is a variable; nothing can be done to simplify
			}
		
		//next try fancy simplification rules
		//multiplication
		if (p.getElement().equals("*")) {
			if (tree.left(p).getElement().equals("1")) {
				tree.remove(tree.left(p));
				tree.remove(p);
			}
			
			else if (tree.right(p).getElement().equals("1")) {
				tree.remove(tree.right(p));
				tree.remove(p);
			}
			
			else if (tree.left(p).getElement().equals("0") || tree.right(p).getElement().equals("0")){
				tree.set(p, "0");
				removeSubtree(tree, tree.left(p));
				removeSubtree(tree, tree.right(p));
			}
		}
		
		//addition
		else if (p.getElement().equals("+")) {
			if (tree.left(p).getElement().equals("0")) {
				tree.remove(tree.left(p));
				tree.remove(p);
			}
			else if (tree.right(p).getElement().equals("0")) {
				tree.remove(tree.right(p));
				tree.remove(p);
			}
		}
		
		//subtraction
		else if (p.getElement().equals("-")) {
			if (tree.right(p).getElement().equals("0")) {
				tree.remove(tree.right(p));
				tree.remove(p);
			}
			
			else if (equalsSubtree(tree, tree.left(p), tree.right(p))) {
				tree.set(p, "0");
				removeSubtree(tree, tree.left(p));
				removeSubtree(tree, tree.right(p));
			}
		}
		
	}
	
	//private helper method to remove the entirety of a subtree's nodes
	private static void removeSubtree(LinkedBinaryTree<String> tree, Position<String> p) {
		
		if (p != null) {
		
			if (tree.left(p) != null) {
				removeSubtree(tree, tree.left(p));
			}
			if (tree.right(p) != null) {
				removeSubtree(tree, tree.right(p));
			}
			
			tree.remove(p);
		}
	}

	
	/**
	 * Given a tree, a variable label and a value, this should replace all
	 * instances of that variable in the tree with the given value
	 * 
	 * Ideally, this method should run in O(n) time (quite hard! O(n^2) is easier.)
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @param variable
	 *            - a variable label that might exist in the tree
	 * @param value
	 *            - an integer value that the variable represents
	 * @return Tree after replacing all instances of the specified variable with
	 *         it's numeric value
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression, or either of the other
	 *             arguments are null
	 */
	public static LinkedBinaryTree<String> substitute(LinkedBinaryTree<String> tree, String variable, int value)
			throws IllegalArgumentException {
		if(!isArithmeticExpression(tree) || variable == null) {
			throw new IllegalArgumentException();
		}
		
		inOrderTraversalReplace(tree.root(), tree, variable, value);
		return tree;
		
	}
	
	private static void inOrderTraversalReplace(Position<String> p, LinkedBinaryTree<String> tree, String variable, int value) {
		if (p != null) {
			inOrderTraversalReplace(tree.left(p), tree, variable, value);
			if (p.getElement().equals(variable)){
				tree.set(p, Integer.toString(value));
			}
			inOrderTraversalReplace(tree.right(p), tree, variable, value);
		}
	}

	/**
	 * Given a tree and a a map of variable labels to values, this should
	 * replace all instances of those variables in the tree with the
	 * corresponding given values
	 * 
	 * Ideally, this method should run in O(n) expected time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @param map
	 *            - a map of variable labels to integer values
	 * @return Tree after replacing all instances of variables which are keys in
	 *         the map, with their numeric values
	 * @throws IllegalArgumentException
	 *             if tree was not a valid expression, or map is null, or tries
	 *             to substitute a null into the tree
	 */
	public static LinkedBinaryTree<String> substitute(LinkedBinaryTree<String> tree, HashMap<String, Integer> map)
			throws IllegalArgumentException {
		if(!isArithmeticExpression(tree) || map == null) {
			throw new IllegalArgumentException();
		}
		
		inOrderTraversalReplace(tree.root(), tree, map);
		return tree;
	}
	
	private static void inOrderTraversalReplace(Position<String> p, LinkedBinaryTree<String> tree, HashMap<String, Integer> map)
			throws IllegalArgumentException{
		
		if (p != null) {
			inOrderTraversalReplace(tree.left(p), tree, map);
			if (map.containsKey(p.getElement())){
				//trying to change a variable to a null element throws IllegalArgumentException
				if (map.get(p.getElement()) == null){
					throw new IllegalArgumentException();
				}
				//else change the node's element to what the variable is mapped to
				tree.set(p, Integer.toString(map.get(p.getElement())));
			}
			inOrderTraversalReplace(tree.right(p), tree, map);
		}
	}

	/**
	 * Given a tree, identify if that tree represents a valid arithmetic
	 * expression (possibly with variables)
	 * 
	 * Ideally, this method should run in O(n) expected time
	 * 
	 * @param tree
	 *            - a tree representing an arithmetic expression
	 * @return true if the tree is not null and it obeys the structure of an
	 *              arithmetic expression. Otherwise, it returns false
	 */
	public static boolean isArithmeticExpression(LinkedBinaryTree<String> tree) {
		
		if (tree == null) {
			return false;
		}
		
		return inOrderTraversalCheck(tree.root(), tree);
	}
	
	private static boolean inOrderTraversalCheck(Position<String> p, LinkedBinaryTree<String> tree) {

		//null elements are not allowed
		if (p.getElement() == null) {
			return false;
		}
		
		//current node is a leaf (i.e. needs to be variable or number; not an operator)
		if (tree.isExternal(p)) {
			if (p.getElement().equals("+") || p.getElement().equals("-") || p.getElement().equals("*")) {
				return false;
			}
			
			//condition has been satisfied (not operator) and has hit base case (leaf)
			return true;
		}
		
		//current node is internal (i.e. needs to be an operator)
		else {
			//binary expression tree must be proper
			if (tree.numChildren(p) != 2) {
				return false;
			}
			//must be operator
			if (!p.getElement().equals("+") && !p.getElement().equals("-") && !p.getElement().equals("*")) {
				return false;
			}
		}
		
		//current node is internal, so apply recursion on both left and right subtrees
		return (inOrderTraversalCheck(tree.left(p), tree) && inOrderTraversalCheck(tree.right(p), tree));
		
	}

}