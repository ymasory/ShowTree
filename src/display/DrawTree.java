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

package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import logic.Node;
import logic.treeIterators.PreorderIterator;
import display.components.TreeMenu;
import display.components.TreePane;

/*
 * This class actually _draws_ the tree to the TreePane.
 * This means it calls one of the tree _positioning_ algorithms and then performs other tasks
 * needed for drawing, including translating the coordinates so the tree aligns to the left of the TreePane,
 * adding the padding so the trees aren't at the exact edge of the TreePane, drawing labels, drawing edges,
 * computing the area of the tree for the sake of the scroll bars, etc.
 * 
 * 
 * minYSeparation is required to be at least 25 to ensure nice display. Any smaller and the Nodes will
 * overlap (since they are always at least BOTTOM_PADDING) tall. This can cause edges to go up instead
 * of down. This is strictly a display limitation, the positioning algorithms work for any separation.
 */
public class DrawTree {

	/*
	 * For the sake of computeTreeGraphicalRectangle
	 * Not updated to null if someone directly access repaint() in TreePane, but then again it doesn't need to be,
	 * since you can't save an image of an empty screen, and only save image calls computeTreeGraphicalRectangle at this point
	 */
	private static boolean drawLabels;

	/*
	 * Tree drawing works by assigning each Node a rectangle of its ultimate size and position, up to translation.
	 * The tree is then translated so it is left-justified on Pane.
	 * Nodes are then recursively drawing, follows by edges.
	 * The size of the TreePane is then recomputed based on the size of the tree, for the sake of scroll-bar
	 * behavior.
	 * @param g - the TreePane's graphical context
	 */
	public static void paintTree(Graphics g, boolean drawLabs) {
		Node root = Start.getRoot();
		if(root == null){
			TreePane.getInstance().setPreferredSize(new Dimension(0, 0));
			TreePane.getInstance().revalidate();
		}
		else {
//			System.out.println(TreeMenu.getInstance().getPositioningAlgorithm().toString() + " width: " + Start.getRoot().getEmbeddedWidth());
//			System.out.println(TreeMenu.getInstance().getPositioningAlgorithm().toString() + " sibling spacing: " + Start.getRoot().getEmbeddedSiblingSpacing());
//			System.out.println();
			drawLabels = drawLabs;
			computeNodeRectangles(root, g);
			leftJustifyTree(root, g, drawLabs);
			drawNodes(root, g, drawLabs);
			//			DrawEdges.drawOrthogonalEdges(root, g);
			DrawEdges.drawStraightNodeToNodeEdges(root, g);
			if(Start.isShowNodeField()) {
				showNodeFieldHelper(root, g);
			}
			Rectangle treeRect = computeTreeGraphicalRectangle(root, g);
			TreePane.getInstance().setPreferredSize(
					new Dimension((int) (treeRect.getWidth() + treeRect.getX() + Start.PADDING), (int) (treeRect.getHeight() + treeRect.getY()) + Start.PADDING));
			TreePane.getInstance().revalidate();
		}
	}

	/*
	 * Translates the tree rectangles so it is left-justified on TreePanel.
	 * Does this by changing the Nodes' stored graphicalRectangle values.
	 * @param root - the root of the tree to be translated
	 * @param g - the TreePane's graphics context
	 */
	private static void leftJustifyTree(Node root, Graphics g, boolean drawLabels) {
		Rectangle treeRect = computeTreeGraphicalRectangle(root, g);
		int rightShift = 0;
		if(drawLabels) {
			rightShift = (0 - (int) treeRect.getX()) + Start.PADDING;
		}
		else {
			Rectangle leftMost = findLeftmostNode(root).getGraphicalRectangle();
			int midpoint = (int) (leftMost.getX() + leftMost.getWidth()/2);
			rightShift = (0 - midpoint + Start.PADDING);
		}
		int downShift = (0 - (int) treeRect.getY()) + Start.PADDING;
		PreorderIterator iter = new PreorderIterator(root);
		Node cur;
		int curX;
		int curY;
		int curWidth;
		int curHeight;
		while(iter.hasNext()) {
			cur = iter.next();
			Rectangle curRect = cur.getGraphicalRectangle();
			curX = (int) curRect.getX();
			curY = (int) curRect.getY();
			curWidth = (int) curRect.getWidth();
			curHeight = (int) curRect.getHeight();
			cur.setGraphicalRectangle(new Rectangle(curX + rightShift, curY + downShift, curWidth, curHeight));
			cur.setX(cur.getX() + rightShift);
			cur.setY(cur.getY() + downShift);
		}
	}

	/*
	 * Returns the most Node positioned farthest to the left in the tree, counting a Node's position
	 * as the CENTER of its bounding rectangle, not the origin of the rectangle.
	 * @param root - the root of the tree
	 */
	private static Node findLeftmostNode(Node root) {
		PreorderIterator iter = new PreorderIterator(root);
		Node leftMost = root;
		double minX = Integer.MAX_VALUE;
		Node cur;
		while(iter.hasNext()) {
			cur = iter.next();
			Rectangle labelRect = cur.getGraphicalRectangle();			
			double curMinX = labelRect.getX() + labelRect.getWidth()/2;			
			if(curMinX < minX) {
				minX = curMinX;
				leftMost = cur;
			}
		}
		return leftMost;
	}

	/*
	 * WARNING: Behavior is dependent upon static boolean drawLabels. If you are calling this outside of the GUI
	 * there is no way for you to tell whether the rectangle will account for labels being drawn or not!
	 * @param root - the root of the tree whose bounding rectangle is sought
	 * @param g - the TreePane's graphics context
	 * @returns Rectangle representing the currently displayed tree's position and dimensions
	 */
	public static Rectangle computeTreeGraphicalRectangle(Node root, Graphics g) {
		PreorderIterator iter = new PreorderIterator(root);
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		Node cur;
		while(iter.hasNext()) {
			cur = iter.next();

			Rectangle labelRect = cur.getGraphicalRectangle();	
			int curMinX = 0;
			int curMaxX = 0;
			int curMinY = 0;
			int curMaxY = 0;
			if(drawLabels) {
				curMinX = (int) labelRect.getX();
				curMaxX = (int) (labelRect.getX() + labelRect.getWidth());
				curMinY = (int) labelRect.getY();
				curMaxY = (int) (labelRect.getY() + labelRect.getHeight());
			}
			else {
				curMinX = (int) cur.getX();
				curMaxX = (int) cur.getX();
				curMinY = (int) cur.getY();
				curMaxY = (int) cur.getY();
			}

			if(curMinX < minX) {
				minX = curMinX;
			}
			if(curMaxX > maxX) {
				maxX = curMaxX;
			}
			if(curMinY < minY) {
				minY = curMinY;
			}
			if(curMaxY > maxY) {
				maxY = curMaxY;
			}
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	/*
	 * Recursively draws Nodes, which involves drawing their labels at the right place as to appear centered.
	 * @param n - the current Node whose label is being drawn
	 * @param g - the TreePane's graphics context
	 */
	private static void drawNodes(Node n, Graphics g, boolean drawLabels) {
		drawNode(n, g, drawLabels);
		ArrayList<Node> children = n.getChildren();
		for (Node child : children) {
			drawNodes(child, g, drawLabels);
		}
	}

	private static void drawNode(Node n, Graphics g, boolean drawLabel) {
		Rectangle nodeRect = n.getGraphicalRectangle();
		Point labelDrawPoint = new Point((int) nodeRect.getX(), (int) nodeRect.getY() + (int) nodeRect.getHeight() - Start.BOTTOM_NODE_PADDING);

		if(drawLabel) {
			g.setColor(Color.WHITE);
			g.fillRect((int) nodeRect.getX(), (int) nodeRect.getY(), (int) nodeRect.getWidth(), (int) nodeRect.getHeight());
		}

		if(Start.isShowNodeBounds()) {
			g.setColor(Color.RED);
			g.drawRect((int) nodeRect.getX(), (int) nodeRect.getY(), (int) nodeRect.getWidth(), (int) nodeRect.getHeight());	
		}

		if(n.getLabel() == "" || drawLabel == false) {
			g.setColor(Color.BLACK);
			double diameter = nodeRect.getHeight();
			g.drawOval((int) (nodeRect.getX() + nodeRect.getWidth()/2 - diameter/2), 
					(int) nodeRect.getY(), 
					(int) diameter, 
					(int) diameter);
		}
		else {
			g.setColor(Color.BLACK);
			g.drawString(n.getLabel(), (int) labelDrawPoint.getX(), (int) labelDrawPoint.getY());
		}
	}

	/*
	 * Recursively assigns each node its bounding Rectangle where it will be drawn (after translation) on the TreePane
	 * NOTE: the FontMetrics.getStringBounds() function does a poor job finding the exact bounds of a displayed
	 * String. It always gives some padding on the top. That is what BOTTOM_NODE_PADDING is for, to compensate by adding 
	 * some space to the bottom as well. This bottom padding however will also appear for empty string labels.
	 * @param n - the root of the tree
	 * @param g = the TreePane's graphics context
	 */
	public static void computeNodeRectangles(Node n, Graphics g) {
		PreorderIterator iter = new PreorderIterator(n);
		Node cur;
		while(iter.hasNext()) {
			cur = iter.next();
			cur.setGraphicalRectangle(computeNodeRectangle(cur, g));
		}
	}

	private static Rectangle computeNodeRectangle(Node n, Graphics g) {
		int x = (int) n.getX();
		int y = (int) n.getY();
		String label = n.getLabel();
		TreePane tp = TreePane.getInstance();
		FontMetrics fm = tp.getFontMetrics(tp.getFont());
		Rectangle2D labelRect = fm.getStringBounds(label, g);
		int xShift = (int) labelRect.getWidth() / 2;
		int yShift = (int) labelRect.getHeight() / 2;
		int rectX = x - xShift;
		int rectY = y - yShift;
		int rectWidth = ((int) labelRect.getWidth());
		int rectHeight = ((int) labelRect.getHeight() + Start.BOTTOM_NODE_PADDING);
		return new Rectangle(rectX, rectY, rectWidth, rectHeight);
	}

	private static void showNodeFieldHelper(Node n, Graphics g) {
		if(n.getModifier() != 0) {
			g.drawString(
					Double.toString(n.getModifier()), 
					(int) (n.getGraphicalRectangle().getX()),
					(int) (n.getGraphicalRectangle().getY())
			);
		}
		ArrayList<Node> children = n.getChildren();
		for (Node child : children) {
			showNodeFieldHelper(child, g);
		}
	}
}
