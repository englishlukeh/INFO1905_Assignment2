import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import textbook.LinkedBinaryTree;
import textbook.Position;

public class TestAssignment {
	
	// Set up JUnit to be able to check for expected exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// Some simple testing of prefix2tree
	@Test(timeout = 100)
	public void testPrefix2tree() {
		
		LinkedBinaryTree<String> tree;

		tree = Assignment.prefix2tree("hi");
		assertEquals(1, tree.size());
		assertEquals("hi", tree.root().getElement());

		tree = Assignment.prefix2tree("+ 5 10");
		assertEquals(3, tree.size());
		assertEquals("+", tree.root().getElement());
		assertEquals("5", tree.left(tree.root()).getElement());
		assertEquals("10", tree.right(tree.root()).getElement());
		
		tree = Assignment.prefix2tree("- 5 10");
		assertEquals(3, tree.size());
		assertEquals("-", tree.root().getElement());
		assertEquals("5", tree.left(tree.root()).getElement());
		assertEquals("10", tree.right(tree.root()).getElement());
		
		tree = Assignment.prefix2tree("* 5 10");
		assertEquals(3, tree.size());
		assertEquals("*", tree.root().getElement());
		assertEquals("5", tree.left(tree.root()).getElement());
		assertEquals("10", tree.right(tree.root()).getElement());
				
		tree = Assignment.prefix2tree("+ 5 - 4 3");
		assertEquals(5, tree.size());
		assertEquals("+", tree.root().getElement());
		assertEquals("5", tree.left(tree.root()).getElement());
		assertEquals("-", tree.right(tree.root()).getElement());
		assertEquals("4", tree.left(tree.right(tree.root())).getElement());
		assertEquals("3", tree.right(tree.right(tree.root())).getElement());
		
		thrown.expect(IllegalArgumentException.class);
		tree = Assignment.prefix2tree("+ 5 - 4");
	}
	
	// example of using the Assignment.equals method to check that "- x + 1 2" simplifies to "- x 3"
	@Test(timeout = 100)
	public void testSimplify1() {
		LinkedBinaryTree<String> tree = Assignment.prefix2tree("- x + 1 2");
		tree = Assignment.simplify(tree);
		LinkedBinaryTree<String> expected = Assignment.prefix2tree("- x 3");
		assertTrue(Assignment.equals(tree, expected));
	}
	
	//note the assumption that the given prefix2Tree subroutine is fully functional
	@Test(timeout = 100)
	public void testTree2Prefix_Tree2Infix() {
		LinkedBinaryTree<String> tree;
		String prefix;
		String infix;
		String convertedPrefix;
		String convertedInfix;
		
		//tests based on the examples in the document
		{
			prefix = "1";
			infix = "1";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
		
			prefix = "x";
			infix = "x";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
		
			prefix = "+ 1 2";
			infix = "(1+2)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
		
			prefix = "+ 1 - 2 3";
			infix = "(1+(2-3))";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
		
			prefix = "* - 1 + b 3 d";
			infix = "((1-(b+3))*d)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
		}
		
		//testing expression trees consisting of only variables
		{
			prefix = "a";
			infix = "a";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "+ a b";
			infix = "(a+b)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "+ + + + a b c d e";
			infix = "((((a+b)+c)+d)+e)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "+ a * b - c d";
			infix = "(a+(b*(c-d)))";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
		}
				
		//testing expression tree consisting of only numerical constants
		{
			prefix = "001";
			infix = "001";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "99999999";
			infix = "99999999";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "+ 1 5";
			infix = "(1+5)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "+ + + + 2 4 6 8 10";
			infix = "((((2+4)+6)+8)+10)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "+ 6 * 3 - 9 1000";
			infix = "(6+(3*(9-1000)))";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
		}
		
		//testing mix of numeric constants and variables
		{
			prefix = "+ a 20";
			infix = "(a+20)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "- 9000000000 bacd";
			infix = "(9000000000-bacd)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "+ - a FDKLDFSJKFDSJ 123233293";
			infix = "((a-FDKLDFSJKFDSJ)+123233293)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "+ + + + a 2 JHHF XsS 102";
			infix = "((((a+2)+JHHF)+XsS)+102)";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
			
			prefix = "+ a * 9000 - c 2123SASD";
			infix = "(a+(9000*(c-2123SASD)))";
			tree = Assignment.prefix2tree(prefix);
			convertedPrefix = Assignment.tree2prefix(tree);
			convertedInfix = Assignment.tree2infix(tree);
			assertTrue(convertedPrefix.equals(prefix));
			assertTrue(convertedInfix.equals(infix));
		}
	}
	
	@Test(timeout = 100)
	public void testSimplify() {
		LinkedBinaryTree<String> tree;
		LinkedBinaryTree<String> simplifiedTree;
		
		//tests based on the examples in the document
		{
			tree = Assignment.prefix2tree("- + 2 15 4");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("13");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("- + 2 15 c");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("- 17 c");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("- - 2 2 c");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("- 0 c");
			assertTrue(Assignment.equals(tree, simplifiedTree));
		}
		
		//testing expression tree consisting of only numerical constants
		{
			tree = Assignment.prefix2tree("+ 2 + 4 6");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("12");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ 0002 + 4 6");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("12");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ -1 1");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("0");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("* 0 + - 4 6 * 8 6");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("0");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ * 9 8 - 6 + 14 8");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("56");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ 0 * * * 99 99 99 99");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("96059601");
			assertTrue(Assignment.equals(tree, simplifiedTree));
		}
		
		//testing expression tree consisting of only variables
		{
			tree = Assignment.prefix2tree("+ A B");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("+ A B");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("- hello + test test");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("- hello + test test");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("* + dog cat - elephant wolf");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("* + dog cat - elephant wolf");
			assertTrue(Assignment.equals(tree, simplifiedTree));
		}
		
		//testing expression tree consisting of mix of variables and numeric constants
		{
			tree = Assignment.prefix2tree("+ A 2");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("+ A 2");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ 8 * three + 2 4");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("+ 8 * three 6");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ * 9 8 + +a -b");
			tree = Assignment.simplify(tree);
			simplifiedTree = Assignment.prefix2tree("+ 72 + +a -b");
			assertTrue(Assignment.equals(tree, simplifiedTree));
		}
		
	}
	
	@Test(timeout = 100)
	public void testSimplifyFancy() {
		LinkedBinaryTree<String> tree;
		LinkedBinaryTree<String> simplifiedTree;
		
		//tests based on the examples in the document
		{
			tree = Assignment.prefix2tree("* 1 a");
			tree = Assignment.simplifyFancy(tree);
			
			simplifiedTree = Assignment.prefix2tree("a");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			
			tree = Assignment.prefix2tree("+ - 2 2 c");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("c");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ - + 1 1 2 c");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("c");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("- * 1 c + c 0");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("0");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("- * 1 c + c 1");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("- c + c 1");
			assertTrue(Assignment.equals(tree, simplifiedTree));
		}
		
		//testing expression tree consisting only of numerical constants (tests copied from simplify testing
		//routine since their results should be equal)
		{
			tree = Assignment.prefix2tree("+ 2 + 4 6");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("12");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ 0002 + 4 6");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("12");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ -1 1");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("0");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("* 0 + - 4 6 * 8 6");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("0");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ * 9 8 - 6 + 14 8");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("56");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ 0 * * * 99 99 99 99");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("96059601");
			assertTrue(Assignment.equals(tree, simplifiedTree));
		}
		
		//testing expression tree consisting only of variables
		{
			tree = Assignment.prefix2tree("+ a b");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("+ a b");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("- + a b + a b");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("0");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			// does not simplify since commutation functionality has not been added
			tree = Assignment.prefix2tree("- + a b + b a");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("- + a b + b a");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ a - + b a + b a");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("a");
			assertTrue(Assignment.equals(tree, simplifiedTree));
		}
		
		//testing expression tree consisting of mix of numeric constants and variables
		{
			tree = Assignment.prefix2tree("+ + -10 5 - * a 1 - a 0");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("-5");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("* * a 1 * bd + 0 0");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("0");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ * a a + a b");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("+ * a a + a b");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
			tree = Assignment.prefix2tree("+ abcd - 10 * 5 - 1 a");
			tree = Assignment.simplifyFancy(tree);
			simplifiedTree = Assignment.prefix2tree("+ abcd - 10 * 5 - 1 a");
			assertTrue(Assignment.equals(tree, simplifiedTree));
			
		}
		
	}
	
	@Test(timeout = 100)
	public void testSubstitute() {
		LinkedBinaryTree<String> tree;
		LinkedBinaryTree<String> substitutedTree;
		
		//testing expression tree based on the examples in the document
		{
			tree = Assignment.prefix2tree("- 1 c");
			Assignment.substitute(tree, "c", 5);
			substitutedTree = Assignment.prefix2tree("- 1 5");
			assertTrue(Assignment.equals(tree,  substitutedTree));
			
			tree = Assignment.prefix2tree("- 1 b");
			Assignment.substitute(tree, "c", 5);
			substitutedTree = Assignment.prefix2tree("- 1 b");
			assertTrue(Assignment.equals(tree,  substitutedTree));
			
			tree = Assignment.prefix2tree("+ c - c c");
			Assignment.substitute(tree, "c", 5);
			substitutedTree = Assignment.prefix2tree("+ 5 - 5 5");
			assertTrue(Assignment.equals(tree,  substitutedTree));
		}
		
		//testing additional expression trees
		{
			tree = Assignment.prefix2tree("- a + b * a c");
			Assignment.substitute(tree, "a", 10);
			Assignment.substitute(tree, "b", 2);
			Assignment.substitute(tree, "c", 8);
			substitutedTree = Assignment.prefix2tree("- 10 + 2 * 10 8");
			assertTrue(Assignment.equals(tree,  substitutedTree));
			
			tree = Assignment.prefix2tree("+ + + + + + a a b b c c d");
			Assignment.substitute(tree, "a", 10);
			Assignment.substitute(tree, "b", 2);
			Assignment.substitute(tree, "c", 8);
			Assignment.substitute(tree, "d", 2);
			substitutedTree = Assignment.prefix2tree("+ + + + + + 10 10 2 2 8 8 2");
			assertTrue(Assignment.equals(tree,  substitutedTree));
			
			tree = Assignment.prefix2tree("+ alpha beta");
			Assignment.substitute(tree, "a", 10);
			Assignment.substitute(tree, "b", 2);
			Assignment.substitute(tree, "c", 8);
			substitutedTree = Assignment.prefix2tree("+ alpha beta");
			assertTrue(Assignment.equals(tree,  substitutedTree));
		}
	}
	
	@Test(timeout = 100)
	public void testSubstituteMap() {
		LinkedBinaryTree<String> tree;
		LinkedBinaryTree<String> substitutedTree;
		HashMap<String, Integer> map;
		
		//testing expression tree based on examples in document
		{
			tree = Assignment.prefix2tree("+ c - a b");
			map = new HashMap<>();
			map.put("a", 1);
			map.put("b", 5);
			map.put("c", 3);
			Assignment.substitute(tree, map);
			substitutedTree = Assignment.prefix2tree("+ 3 - 1 5");
			assertTrue(Assignment.equals(tree, substitutedTree));
		}
		
		//testing additional expression trees
		{
			tree = Assignment.prefix2tree("- a + b * a c");
			map = new HashMap<>();
			map.put("a", 10);
			map.put("b", 2);
			map.put("c", 8);
			tree = Assignment.substitute(tree, map);
			substitutedTree = Assignment.prefix2tree("- 10 + 2 * 10 8");
			assertTrue(Assignment.equals(tree,  substitutedTree));
			
			tree = Assignment.prefix2tree("+ + + + + + a a b b c c d");
			map = new HashMap<>();
			map.put("a", 10);
			map.put("b", 2);
			map.put("c", 8);
			map.put("d", 2);
			tree = Assignment.substitute(tree, map);
			substitutedTree = Assignment.prefix2tree("+ + + + + + 10 10 2 2 8 8 2");
			assertTrue(Assignment.equals(tree,  substitutedTree));
			
			tree = Assignment.prefix2tree("+ alpha beta");
			Assignment.substitute(tree, "a", 10);
			Assignment.substitute(tree, "b", 2);
			Assignment.substitute(tree, "c", 8);
			tree = Assignment.substitute(tree, map);
			substitutedTree = Assignment.prefix2tree("+ alpha beta");
			assertTrue(Assignment.equals(tree,  substitutedTree));
			
			tree = Assignment.prefix2tree("a");
			map = new HashMap<>();
			map.put("a", null);
			try {
				Assignment.substitute(tree, map);
				fail("Should have thrown IllegalArgumentException");
			}catch (Exception e) {
				//do nothing
			}
		}
	}
	
	@Test(timeout = 100)
	public void testIsArithmeticExpression() {
		//note testing this subroutine, as a result of all prior subroutines calling this function, tests all functions' IllegalArgumentException throwing capabilities
		LinkedBinaryTree<String> tree;
		
		//testing expression tree based on examples in the document
		{
			tree = new LinkedBinaryTree<>();
			tree.addRoot("-");
			tree.addLeft(tree.root(), "1");
			assertFalse(Assignment.isArithmeticExpression(tree));
			
			tree = new LinkedBinaryTree<>();
			tree.addRoot("-");
			tree.addRight(tree.root(), "1");
			assertFalse(Assignment.isArithmeticExpression(tree));
			
			tree = new LinkedBinaryTree<>();
			tree.addRoot("+");
			tree.addLeft(tree.root(), "1");
			tree.addRight(tree.root(), "+");
			assertFalse(Assignment.isArithmeticExpression(tree));
		}
		
		//testing additional expression trees
		{
			tree = new LinkedBinaryTree<>();
			tree.addRoot("1");
			tree.addLeft(tree.root(), "1");
			assertFalse(Assignment.isArithmeticExpression(tree));
			
			tree = new LinkedBinaryTree<>();
			tree.addRoot("-");
			assertFalse(Assignment.isArithmeticExpression(tree));
			
			tree = new LinkedBinaryTree<>();
			tree.addRoot("-");
			Position<String> p = tree.addLeft(tree.root(), "+");
			tree.addLeft(p, "1");
			tree.addRight(tree.root(), "9");
			assertFalse(Assignment.isArithmeticExpression(tree));
			
			tree = new LinkedBinaryTree<>();
			tree.addRoot("12390");
			assertTrue(Assignment.isArithmeticExpression(tree));
			
			tree = new LinkedBinaryTree<>();
			tree.addRoot("");
			assertTrue(Assignment.isArithmeticExpression(tree));
		}
	}
	
	@Test(timeout = 100)
	public void testMixOfOperations() {
		LinkedBinaryTree<String> tree;
		LinkedBinaryTree<String> finishedTree;
		HashMap<String, Integer> map;
		
		tree = Assignment.prefix2tree("+ c - a b");
		map = new HashMap<>();
		map.put("a", 1);
		map.put("b", 5);
		map.put("c", 3);
		tree = Assignment.substitute(tree, map);
		tree = Assignment.simplify(tree);
		finishedTree = Assignment.prefix2tree("-1");
		assertTrue(Assignment.equals(tree, finishedTree));
		
		tree = Assignment.prefix2tree("+ - d a * b c");
		map = new HashMap<>();
		map.put("a", 1000);
		map.put("b", 234);
		map.put("c", 1);
		map.put("d", 0);
		tree = Assignment.substitute(tree, map);
		tree = Assignment.simplify(tree);
		finishedTree = Assignment.prefix2tree("-766");
		assertTrue(Assignment.equals(tree, finishedTree));
		
		tree = Assignment.prefix2tree("* 0 + a b");
		Assignment.simplifyFancy(tree);
		System.out.println(Assignment.tree2infix(tree));
		
	}
	
	

}