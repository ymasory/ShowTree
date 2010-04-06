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

import logic.Node;

public class Knuth71WithCentering implements PositioningAlgorithm {

	public void embed(Node n, double s) {
		new Knuth71().embed(n, s);
		new CenterParents().centerParents(n, s);
	}

	public boolean isBinary() {
		return true;
	}
	
	public String toString() {
		return "2: Knuth71 Centered";
	}

	public boolean handlesNodeWidths() {
		return false;
	}
}
