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

package logic.positioningAlgorithms;

import java.util.ArrayList;

import display.Start;

import logic.Node;
import logic.treeIterators.PreorderIterator;

public class LevelSlide implements PositioningAlgorithm {

	public void embed(Node r, double s) {
		double xSep = s;
		double ySep = Start.Y_SEPARATION;
		
		r.assignDepths();
		r.assignHeights();
		
		PreorderIterator iter = new PreorderIterator(r);
		Node cur;
		while(iter.hasNext()) {
			cur = iter.next();
			cur.setY(0);
			cur.setX(0);
		}
		
		iter = new PreorderIterator(r);
		while(iter.hasNext()) {
			cur = iter.next();
			cur.setY(cur.getDepth() * ySep);
		}
		
		int maxHeight = r.getHeight();
		ArrayList<ArrayList<Node>> levels = new ArrayList<ArrayList<Node>>();
		for(int i = 0; i <= maxHeight; i++) {
			levels.add(new ArrayList<Node>());
		}
		
		iter = new PreorderIterator(r);
		while(iter.hasNext()) {
			cur = iter.next();
			levels.get(cur.getDepth()).add(cur);
		}
		
		for(int i = levels.size() - 1; i >= 0; i--) {
			double nextPos = 0;
			ArrayList<Node> level = levels.get(i);
			for(int j = 0; j < level.size(); j++) {
				cur = level.get(j);
				double pos = 0;
				// putting leaves at next position ensures there is enough area between internal nodes
				//  on a level to fit the leaves
				if(cur.isLeaf()) {
					cur.setX(nextPos);
				}
				else {
					if(cur.degree() == 1) {
						pos = cur.getLeft().getX();
						cur.setX(pos);
					}
					else {
						pos = (cur.getLeft().getX() + cur.getRight().getX())/2;
						cur.setX(pos);
					}
				}
				if(cur.getX() < nextPos) {
					double shift = nextPos - cur.getX();
					cur.setX(pos + shift);
					shiftLevelStartingAt(levels.get(i + 1), cur.getLeft(), shift);
				}
				nextPos = cur.getX() + xSep;
			}
			adjustLevel(level, xSep);
		}
	}
	
	/*
	 * The question really is, where do you put the leaves?
	 * If you do nothing at this point, sticking with the nextPos placement above, you are identical to WS79 Tidy
	 * In this approach we will shift the leaves left or right to its most closely related internal node
	 *
	 */
	//FIXME filling of adjacentLeafGroups does not work
	public void adjustLevel(ArrayList<Node> level, double xSep) {
		System.out.println("adjusting level: " + level.get(0).getDepth());
		ArrayList<ArrayList<Node>> adjacentLeafGroups = new ArrayList<ArrayList<Node>>();
		int lSize = level.size();
		boolean lastWasInternal = false;
		ArrayList<Node> curGroup = new ArrayList<Node>();
		adjacentLeafGroups.add(curGroup);
		for(int i = 0; i < lSize; i++) {
			Node cur = level.get(i);
			if(cur.isInternal()) {
				lastWasInternal = true;
				continue;
			}
			else {
				if(lastWasInternal == false) {
					curGroup.add(cur);
				}
				else {
					adjacentLeafGroups.add(curGroup);
					curGroup = new ArrayList<Node>();
					adjacentLeafGroups.add(curGroup);
				}				
				lastWasInternal = false;				
			}
		}
		for(int i = 0; i < adjacentLeafGroups.size(); i++) {
			adjustLeafGroup(adjacentLeafGroups.get(i));
		}
	}
	
	public void adjustLeafGroup(ArrayList<Node> group) {
		for(int i = 0; i < group.size(); i++) {
		}
	}
	
	
	
	
	
	
	
	
	
	// TODO this function can easily be made linear using the mod techniques of WS79
	public void shiftLevelStartingAt(ArrayList<Node> level, Node startNode, double shift) {
		int startNodeIndex = -1;
		for(int i = 0; i < level.size(); i++) {
			if(level.get(i) == startNode) {
				startNodeIndex = i;
			}
		}
		for(int i = startNodeIndex; i < level.size(); i++) {
			shiftTree(level.get(i), shift);
		}
	}
	
	public void shiftTree(Node n, double shift) {
		n.setX(n.getX() + shift);
		for(int i = 0; i < n.degree(); i++) {
			shiftTree(n.getChildren().get(i), shift);			
		}
	}

	
	
	
	
	
	
	public boolean handlesNodeWidths() {
		return false;
	}

	public boolean isBinary() {
		return true;
	}
	
	public String toString() {
		return "2: Level Slide";
	}
}
