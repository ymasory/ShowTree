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

/*
 * Implements Algorithm 1 from:
 * C.Wetherell and A. Shannon. Tidy drawings of trees. IEEE Transactions
 *   on Software Engineering, 7(2):223-228, 1979
 * WS call "height" what I call "depth"
 */
public class WS79_Narrowest implements PositioningAlgorithm {
	
	private Node current;
	
	public void embed(Node n, double s) {
		initializeTree(n);
		n.assignDepths();
		n.assignHeights();
		int maxHeight = n.getHeight();
		int[] nextX = new int[maxHeight + 1];
		n.setStatus(0);
		current = n;
		while(current != null) {
			if(current.getStatus() == 0) {
				current.setX(nextX[current.getDepth()] * s);
				current.setY(current.getDepth() * Start.Y_SEPARATION);
				nextX[current.getDepth()] = nextX[current.getDepth()] + 1;
				current.setStatus(1);
			}
			else if(1 <= current.getStatus() && current.getStatus() <= current.degree()) {
				current.setStatus(current.getStatus() + 1);
				current = current.getChildren().get(current.getStatus() - 2);
			}
			else{
				current = current.getParent();
			}
		}
	}
	
	private void initializeTree(Node n) {
		n.setStatus(0);
		for(int i = 0; i < n.degree(); i++) {
			initializeTree(n.getChildren().get(i));
		}
	}
	
	public boolean isBinary() {
		return false;
	}

	public boolean handlesNodeWidths() {
		return false;
	}
	
	public String toString() {
		return "n: WS79 Narrowest";
	}
}
