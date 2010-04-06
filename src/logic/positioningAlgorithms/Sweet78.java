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
 * Implements Appendix B from:
 * R.E. Sweet. Empirical estimates of program entropy. PhD thesis, Stanford
 * University, 1977.
 * 
 * Using pseudocode in:
 * J.S. Tilford. Tree drawing algorithms. Master�s thesis, University of 
 * Illinois at Urbana-Champaign, 1981. 
 */
public class Sweet78 implements PositioningAlgorithm {

	public void embed(Node n, double s) {
		n.assignHeights();
		n.assignDepths();
		double[] nextPosition = new double[n.getHeight() + 1]; 
		embedHelper(n, 0, 0, nextPosition, s);
	}
	
	public double embedHelper(Node n, double leftMost, int level, double[] nextPosition, double s) {
		double lPos = Math.max(leftMost, nextPosition[level]);	
		double rPos = 0;
		double position = 0;
		if(n.isLeaf()) {
			position = lPos;
		}
		else if(n.degree() == 1) {
			position = embedHelper(n.getLeft(), lPos, level + 1, nextPosition, s);
		}
		else if(n.degree() > 1) {
			lPos = embedHelper(n.getLeft(), lPos, level + 1, nextPosition, s);
			rPos = embedHelper(n.getRight(), 0, level + 1, nextPosition, s);
			position = Math.max(nextPosition[level], (lPos + rPos) / 2);
		}		
		n.setX(position);
		n.setY(Start.Y_SEPARATION * n.getDepth());
		nextPosition[level] = position + s;
		return position;
	}

	public boolean isBinary() {
		return true;
	}

	public boolean handlesNodeWidths() {
		return false;
	}

	public String toString() {
		return "2: Sweet78";
	}
}
