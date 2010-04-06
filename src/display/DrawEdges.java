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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import logic.Node;

public class DrawEdges {
	
	/*
	 * Draws edges recursively, orthogonally. That means siblings have a horizontal above them, 
	 * and nodes (with children) have a small line coming out below them to meet the horizontal line
	 * over its children. Children have a longer line coming out above them to meet the horizontal line.
	 * @param n - the current Node whose edges are being drawn to its children
	 * @param g - the TreePane's graphics context
	 */
	public static void drawOrthogonalEdges(Node n, Graphics g) {
		ArrayList<Node> children = n.getChildren();
		for (Node child : children) {

			Rectangle curRect = n.getGraphicalRectangle();
			Rectangle childRect = child.getGraphicalRectangle();
			int bottomCenterX = (int) (curRect.getX() + curRect.getWidth()/2);
			int bottomCenterY = (int) (curRect.getY() + curRect.getHeight());
			int topCenterX = (int) (childRect.getX() + childRect.getWidth()/2);
			int topCenterY = (int) childRect.getY();

			int yDiff = topCenterY - bottomCenterY;
			int lessYDiff = yDiff / 4;
			int moreYDiff = (3 * yDiff)/4;
			
			g.setColor(Color.BLACK);			
			g.drawLine(bottomCenterX, bottomCenterY, bottomCenterX, bottomCenterY + lessYDiff);
			g.drawLine(topCenterX, topCenterY, topCenterX, topCenterY - moreYDiff);
			if(child.hasLeftSibling() == false) {
				Rectangle rightMostRect = child.getParent().getChildren().get(child.getParent().degree() - 1).getGraphicalRectangle();
				int rightTopCenterX = (int) (rightMostRect.getX() + rightMostRect.getWidth()/2);
				int rightTopCenterY = (int) (rightMostRect.getY());
				g.drawLine(topCenterX, topCenterY - moreYDiff, rightTopCenterX, rightTopCenterY - moreYDiff);
			}

			drawOrthogonalEdges(child, g);
		}
	}

	/*
	 * Draws edges recursively, from the bottom of lower level nodes to the top of higher level nodes,
	 * NOT from the center of Nodes' bounding rectangles.
	 * @param n - the current Node whose edges are being drawn to its children
	 * @param g - the TreePane's graphics context
	 */
	public static void drawStraightNodeToNodeEdges(Node n, Graphics g) {	
		ArrayList<Node> children = n.getChildren();
		for (Node child : children) {

			Rectangle curRect = n.getGraphicalRectangle();
			Rectangle childRect = child.getGraphicalRectangle();
			int bottomCenterX = (int) (curRect.getX() + curRect.getWidth()/2);
			int bottomCenterY = (int) (curRect.getY() + curRect.getHeight());
			int topCenterX = (int) (childRect.getX() + childRect.getWidth()/2);
			int topCenterY = (int) childRect.getY();

			g.setColor(Color.BLACK);			
			g.drawLine(bottomCenterX, bottomCenterY, topCenterX, topCenterY);

			drawStraightNodeToNodeEdges(child, g);
		}
	}
}
