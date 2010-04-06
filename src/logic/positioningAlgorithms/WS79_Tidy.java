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
import logic.treeIterators.PostorderIterator;

/*
 * Implements Algorithm 3 from:
 * C.Wetherell and A. Shannon. Tidy drawings of trees. IEEE Transactions
 *   on Software Engineering, 7(2):223-228, 1979
 * WS call "height" what I call "depth"
 */
public class WS79_Tidy implements PositioningAlgorithm {

	public void embed(Node n, double s) {
		n.assignDepths();
		n.assignHeights();
		double[] shiftingUnits = new double[n.getHeight() + 1];
		double[] nextPosition = new double[n.getHeight() + 1]; 

		// first pass
		PostorderIterator iter = new PostorderIterator(n);
		Node cur;
		while(iter.hasNext()) {			
			cur = iter.next();			
			int level = cur.getDepth();
			cur.setY(Start.Y_SEPARATION * level);
			double provisionalX = 0;
			if(cur.isLeaf()) {
				provisionalX = nextPosition[level];
			}
			else if(cur.degree() == 1) {
				provisionalX = cur.getLeft().getX();
			}
			else if(cur.degree() > 1){
				provisionalX = (cur.getLeft().getX() + cur.getRight().getX())/2;
			}
			
			shiftingUnits[level] = Math.max(shiftingUnits[level], nextPosition[level] - provisionalX);
			
			if(cur.isLeaf()) {
				cur.setX(provisionalX);
			}
			else {
				cur.setX(provisionalX + shiftingUnits[level]);
			}
			nextPosition[level] = cur.getX() + s;
			cur.setModifier(shiftingUnits[level]);
		}
		
		// second pass
		shift(n, 0);
	}
	
	public void shift(Node n, double baseShift) {
		n.setX(n.getX() + baseShift);
		baseShift += n.getModifier();
		if(n.hasLeft()) {
			shift(n.getLeft(), baseShift);
		}
		if(n.hasRight()) {
			shift(n.getRight(), baseShift);
		}
	}
	
		
	public boolean isBinary() {
		return true;
	}

	public boolean handlesNodeWidths() {
		return false;
	}
	
	public String toString() {
		return "2: WS79 Tidy";
	}
}
