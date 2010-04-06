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

import display.Start;
import logic.Node;
import logic.treeIterators.InorderIterator;

/*
 * Implements:
 * D.E. Knuth. Optimum binary search trees. Acta Informatica, 1(1):14– 
 *   25, 1971.
 */
public class Knuth71 implements PositioningAlgorithm {
	
	public void embed(Node n, double s) {
		n.assignDepths();
		InorderIterator iter = new InorderIterator(n, true);
		int nextNumber = 0;
		while(iter.hasNext()) {
			Node cur = iter.next();
			cur.setX(s/2 * nextNumber++);
			cur.setY(Start.Y_SEPARATION * cur.getDepth());
		}
	}

	public boolean isBinary() {
		return true;
	}
	
	public String toString() {
		return "2: Knuth71";
	}

	public boolean handlesNodeWidths() {
		return false;
	}
}

/*
 * This is how it appears in WS
 */
//	private final int FIRST_VISIT = 0;
//	private final int LEFT_VISIT = 1;
//	private final int RIGHT_VISIT = 2;
	
//	public void embed(Node n, int s) {
//		n.assignDepths();
//		n.assignHeights();
//		int next_number = 0;
//		n.setStatus(FIRST_VISIT);
//		Node current = n;
//		while(current != null) {
//			/* using a switch statement as in the paper is unwise here,
//			* as java's storage of the values for the switch is different 
//			* than the pseudocode anticipates */
//			int stat = current.getStatus();
//			if(stat == FIRST_VISIT){
//				current.setStatus(LEFT_VISIT);
//				if(current.getNumChildren() > 0){
//					current = current.getChildren().get(0);
//					current.setStatus(FIRST_VISIT);
//				}
//			}
//			else if(stat == LEFT_VISIT) {
//				//integer division will bias this display toward narrowness
//				current.setX(next_number * s/2);
//				next_number++;
//				current.setY(current.getDepth() * s);
//				current.setStatus(RIGHT_VISIT);
//				if(current.getNumChildren() > 1) {
//					current = current.getChildren().get(1);
//					current.setStatus(FIRST_VISIT);
//				}
//			}
//			else if(stat == RIGHT_VISIT){
//				current = current.getParent();
//			}
//		}
//	}
