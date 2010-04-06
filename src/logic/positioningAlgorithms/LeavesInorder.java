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
import logic.treeIterators.InorderIterator;

public class LeavesInorder implements PositioningAlgorithm {

	public void embed(Node n, double s) {
		n.assignDepths();
		InorderIterator iter = new InorderIterator(n, true);
		ArrayList<Node> leaves = new ArrayList<Node>();
		Node cur;
		while(iter.hasNext()) {
			cur = iter.next();
			cur.setY(Start.Y_SEPARATION * cur.getDepth());
			if(cur.isLeaf()) {
				leaves.add(cur);
			}
		}
		for(int i = 0; i < leaves.size(); i++) {
			leaves.get(i).setX(s * i);
		}
		new CenterParents().centerParents(n, s);
	}

	public boolean isBinary() {
		return false;
	}

	public String toString() {
		return "n: Leaves Inorder";
	}

	public boolean handlesNodeWidths() {
		return false;
	}
}
