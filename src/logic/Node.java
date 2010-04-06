//ShowTree Tree Visualization System
//Copyright (C) 2009 Yuvi Masory
//
//This program is free software; you can redistribute it and/or
//modify it under the terms of the GNU General Public License
//as published by the Free Software Foundation, version 3 only.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program; if not, write to the Free Software
//Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

package logic;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import logic.treeIterators.PostorderIterator;

/**
 * Represents a Node in a tree. Has fields for all the values the various tree positioning
 * algorithms use. There is no explicit "Tree" class. A tree is referenced by its root Node.
 * 
 * Please note that the children are supposed to be a set with a sequence defined on them.
 * In other words, if there cannot be "gaps" in the children's position. If there is just one child
 * it is inherently the "first" child, an only child cannot be reresented a s second child (i.e. the "right" one),
 * only the first (i.e., the "left" one).
 * It is up to the programmer to maintain this convention and not allow null elements in the children ArrayList
 */
public class Node{

	private static final String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	
	private Node parent;
	private ArrayList<Node> children;
	private String label; //cannot be null
	private int height; //length of longest path to a leaf
	private int depth; //length of path to root
	private double xPos; //given by positioning algorithms, not the ultimate location on TreePane
	private double yPos; //given by positioning algorithms, not the ultimate location on TreePane
	private Rectangle graphicalRectangle; //ultimate location on TreePane, other than left-right translation

	// used by iterators
	private boolean visited;

	// used by WS79_1 and multiple functions using copied code
	private int status;

	// used by WS79_2 & W_BJL04
	private double modifier;

	// used by W_BJL04
	private Node thread;
	private Node ancestor;
	private double prelim;
	private double shift;
	private double change;
	private int number;
	
	public Node(boolean makeLabel) {
		children = new ArrayList<Node>();
		
		if(makeLabel) {
			label = "<";
			int labLen = new Random().nextInt(10);
			labLen++;
			for(int i = 0; i < labLen; i++) {
				label += numbers[i % numbers.length];
			}
			label += ">";
		}
		else {
			label = "";
		}
		
		ancestor = this;
		height = -1;
		depth = -1;
	}
	
	/**
	 * Makes a _sort of_ Penn Treebank format represenation of the tree rooted as this Node
	 * There are three differences:
	 * (1) Emtpy string labels are represented by @_EMPTYSTRING_@
	 * (2) Round parens are represented by @_LRB_@ and @_RRB_@
	 */
	//IMPROVE this can be made prettier with PTB-style indentation and line separation, although IterateThroughTreeTestFile crucially assumes one tree per line
	public String getStringRepresentation() {
		initializeStatus(this);
		String output = "";
		setStatus(0);
		Node current = this;
		while(current != null) {
			if(current.getStatus() == 0) {
				current.setStatus(1);
				
				//first visit
				if(current.isLeaf() && current.getParent() != null && current.getParent().degree() == 1) {
					output += " ";
				}
				else {
					output += "(";
				}
				String processedLabel = current.getLabel();
				if(current.getLabel().equals("")) {
					processedLabel = "@_EMPTYSTRING_@";
				}
				if(processedLabel.contains(new StringBuffer("(")) || processedLabel.contains(new StringBuffer(")"))) {
					processedLabel = processedLabel.replaceAll("\\(", "@_LRB_@");
					processedLabel = processedLabel.replaceAll("\\)", "@_RRB_@");
				}
				output += processedLabel;
			}
			else if(1 <= current.getStatus() && current.getStatus() <= current.degree()) {
				current.setStatus(current.getStatus() + 1);				
				current = current.getChildren().get(current.getStatus() - 2);
			}
			else{				
				//backtracking
				if(current.isLeaf() && current.getParent() != null && current.getParent().degree() == 1) {
					output += " ";
				}
				else {
					output += ")";
				}
				
				current = current.getParent();
			}
		}
		return output;
	}
	
	private void initializeStatus(Node n) {
		n.setStatus(0);
		for(int i = 0; i < n.degree(); i++) {
			initializeStatus(n.getChildren().get(i));
		}
	}
	
	/**
	 * assigns the height value to every node in the tree
	 * runs in theta(n) where n is the number of nodes in the caller's tree
	 * assumed to be called on the root
	 */
	public void assignHeights() {
		assignHeightsHelper(this);
	}

	private int assignHeightsHelper(Node n) {
		if(n.getChildren().size() == 0) {
			n.setHeight(0);
			return 0;
		}
		int i = 0;
		for(Node child : n.getChildren()) {
			i = Math.max(i, assignHeightsHelper(child));
		}
		n.setHeight(i + 1);
		return i + 1;
	}

	/**
	 * assigns the depth value to every node in the caller's tree
	 * runs in theta(n) where n is the number of nodes in the caller's tree
	 * assumed to be called on the root
	 */
	public void assignDepths() {
		assignDepthsHelper(this, 0);
	}
	
	private void assignDepthsHelper(Node n, int curDepth) {
		n.setDepth(curDepth);
		for(Node child : n.getChildren()) {
			assignDepthsHelper(child, curDepth + 1);
		}
	}
	
	/**
	 * determines whether the subtree rooted at this is binary
	 */
	public boolean isBinary() {
		PostorderIterator iter = new PostorderIterator(this);
		while(iter.hasNext()) {
			if(iter.next().degree() > 2) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @return true if the current Node has a sibling to the left
	 */
	public boolean hasLeftSibling() {
		if(parent == null) {
			return false;
		}
		else {
			ArrayList<Node> siblings = parent.getChildren();
			if(siblings.get(0) == this) {
				return false;
			}
			else {
				return true;
			}
		}
	}

	/**
	 * @return true if the current Node has a sibling to the right
	 */
	public boolean hasRightSibling() {
		if(parent == null) {
			return false;
		}
		else {
			ArrayList<Node> siblings = parent.getChildren();
			if(siblings.get(siblings.size() - 1) == this) {
				return false;
			}
			else {
				return true;
			}
		}
	}

	/**
	 * @return the current Node's left sibling, or null if there isn't one
	 */
	public Node getLeftSibling() {
		if(!hasLeftSibling()) {
			return null;
		}
		else {
			ArrayList<Node> siblings = parent.getChildren();
			int myIndex = -1;
			for(int i = 0; i < siblings.size(); i++){
				if(siblings.get(i) == this) {
					myIndex = i;
					break;
				}
			}
			return siblings.get(myIndex - 1);
		}
	}
	
	/**
	 * @return the current Node's right sibling, or null if there isn't one
	 */
	public Node getRightSibling() {
		if(!hasRightSibling()) {
			return null;
		}
		else {
			ArrayList<Node> siblings = parent.getChildren();
			int myIndex = -1;
			for(int i = 0; i < siblings.size(); i++){
				if(siblings.get(i) == this) {
					myIndex = i;
					break;
				}
			}
			return siblings.get(myIndex + 1);
		}
	}

	/**
	 * Does NOT use graphical rectangles, strictly x position.
	 * @return the width of the tree, meaning the difference between the largest and smallest x values in the tree
	 * 
	 */
	public double getEmbeddedWidth() {
		PostorderIterator iter = new PostorderIterator(this);
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		Node cur = null;
		while(iter.hasNext()) {
			cur = iter.next();
			double curX = cur.getX();
			if(curX < minX) {
				minX = curX;
			}
			if(curX > maxX) {
				maxX = curX;
			}
		}
		return maxX - minX;
	}
	

	/**
	 * Does NOT use graphical rectangles, strictly x position.
	 * @return the sum of the distances between siblings in the tree.
	 */
	public double getEmbeddedSiblingSpacing() {
		PostorderIterator iter = new PostorderIterator(this);
		double spacingSum = 0;
		Node cur;
		while(iter.hasNext()) {
			cur = iter.next();
			if(cur.hasRightSibling()) {
				spacingSum += (cur.getRightSibling().getX() - cur.getX());
			}
		}
		return spacingSum;
	}
	
	/**
	 * @return the degree of the current Node (number of children)
	 */
	public int degree() {
		return children.size();
	}
	
	/**
	 * @return true if the current Node has at least one child
	 */
	public boolean hasLeft() {
		return degree() > 0;
	}
	
	/**
	 * @return true if the current Node has at least two children
	 */
	public boolean hasRight() {
		return degree() > 1;
	}
	
	/**
	 * @return true if the the current Node has children, same as hasLeft()
	 */
	public boolean isInternal() {
		return degree() > 0;
	}
	
	/**
	 * @return true if the current Node has no children
	 */
	public boolean isLeaf() {
		return degree() == 0;
	}
	
	/**
	 * @return first child - must exist or NullPointerException results
	 */
	public Node getLeft() {
		return children.get(0);
	}
	
	/**
	 * @return second child - must exist or NullPointerException results
	 */
	public Node getRight() {
		return children.get(1);
	}
	
	
	
	
	
	
	
	
	
	




	
	public ArrayList<Node> getChildren() {
		return children;
	}
	
	public void setChildren(ArrayList<Node> childs) {
		children = childs;
	}
	
	public double getX() {
		return xPos;
	}
	
	public double getY() {
		return yPos;
	}
	
	public void setX(double x) {
		xPos = x;
	}
	
	public void setY(double y) {
		yPos = y;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node par) {
		parent = par;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String val) {
		label = val;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int stat) {
		status = stat;
	}
	
	/**
	 * warning: height may not be initialized
	 */
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int h) {
		height = h;
	}
	
	public void setDepth(int dep) {
		depth = dep;
	}
	
	/**
	 * warning: depth may not be initialized
	 */
	public int getDepth() {
		return depth;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void visit() {
		visited = true;
	}
	
	public void unVisit() {
		visited = false;
	}
	
	public Node getAncestor() {
		return ancestor;
	}
	
	public void setAncestor(Node n) {
		ancestor = n;
	}
	
	public Node getThread() {
		return thread;
	}
	
	public void setThread(Node n) {
		thread = n;
	}
	
	public double getPrelim() {
		return prelim;
	}
	
	public void setPrelim(double p) {
		prelim = p;
	}

	public void setModifier(double shiftAmount) {
		this.modifier = shiftAmount;
	}

	public double getModifier() {
		return modifier;
	}

	public void setGraphicalRectangle(Rectangle rect) {
		graphicalRectangle = rect;
	}
	
	public Rectangle getGraphicalRectangle() {
		return graphicalRectangle;
	}

	public void setShift(double shift) {
		this.shift = shift;
	}

	public double getShift() {
		return shift;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getChange() {
		return change;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	
	/**
	 * Returns string representation of Node, a bracketed representation.
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getStringRepresentation();
	}
}
