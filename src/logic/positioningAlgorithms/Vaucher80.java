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
 * Implements:
 * J.G. Vaucher. Pretty-printing of trees. Software: Practice and Experience,
 * 10(7):553�561, 1980.
 * 
 * Using pseudocode in:
 * J.S. Tilford. Tree drawing algorithms. Master�s thesis, University of 
 * Illinois at Urbana-Champaign, 1981. 
 */
public class Vaucher80 implements PositioningAlgorithm {

	public void embed(Node n, double s) {
		n.assignHeights();
		n.assignDepths();
		double[] nextPosition = new double[n.getHeight() + 1]; 
		embedHelper(n, 0, 0, nextPosition, s);
	}
	
	private double embedHelper(Node n, double place, int level, double[] nextPosition, double s) {
		place = Math.max(place, nextPosition[level]);
		double lPos = 0;
		double rPos = 0;
		if(n.degree() == 1) {
			lPos = embedHelper(n.getLeft(), place, level + 1, nextPosition, s);
		}
		if(n.degree() > 1) {
			lPos = embedHelper(n.getLeft(), place - s/2, level + 1, nextPosition, s);
			rPos = embedHelper(n.getRight(), place + s/2, level + 1, nextPosition, s);
		}

		double position = 0;
		if(n.isLeaf()) {
			position = place;
		}
		else if(n.degree() == 1) {		
			position = lPos;
		}
		else if(n.degree() > 1) {
			position = (lPos + rPos)/2;
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
		return "2: Vaucher80";
	}
}
