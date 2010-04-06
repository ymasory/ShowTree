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
import logic.treeIterators.PostorderIterator;

/*
 * Class for code re-use of function used multiple times.
 */
public class CenterParents {

	/*
	 * Traverses the tree post-order, centering parents first and last child.
	 */
	public void centerParents(Node n, double s) {
		Node cur;
		PostorderIterator pIter = new PostorderIterator(n);
		while(pIter.hasNext()) {
			cur = pIter.next();
			if(cur.isLeaf()) {
				continue;
			}
			double sumX = 0;
			//this isn't the only possible notion of centering in the nary case
			for(Node child : cur.getChildren()) {
				sumX += child.getX();
			}
			cur.setX(sumX/cur.degree());
		}
	}
}
