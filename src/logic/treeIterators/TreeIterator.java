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

package logic.treeIterators;

import java.util.Iterator;

import logic.Node;

/*
 * Class to reuse initialization code
 */
public abstract class TreeIterator implements Iterator<Node> {
	
	protected TreeIterator(Node n) {
		initializeTree(n);
	}
	
	/*
	 * Unvisits every node in the tree so iterator works correctly
	 * @param n the root of the subtree to be unvisited
	 */
	protected void initializeTree(Node n) {
		n.unVisit();
		for(int i = 0; i < n.degree(); i++) {
			initializeTree(n.getChildren().get(i));
		}
	}
}
