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

package logic.treeBuilders;

import java.util.LinkedList;

import logic.Node;

/*
 * Makes a complete tree with the provided number of nodes and maximum degree.
 */
public class CompleteTreeBuilder extends TreeBuilder {

	int numNodes;
	int curNodes;
	
	public Node makeTree(int numNodes, int arity, boolean makeLabels) {
		curNodes = 0;
		this.numNodes = numNodes;
		//need to compute exact arbitrary base logarithms, related to unnecessary empty looping and extra arity == 1 conditional
		int numLevels = (int)Math.ceil(log((numNodes * (arity - 1) + 1), arity) - 1) + 1;
		if(arity == 1) {
			numLevels = numNodes;
		}
		
		LinkedList<Node> q = new LinkedList<Node>();
		Node root = new Node(makeLabels);
		curNodes++;
		q.offer(root);
		
		Node parent;
		Node child;
		for(int i = 0; i < numLevels - 1; i++) {
			parent = q.poll();
			while(parent != null) {
				//unnecessary looping on final level of tree once numNodes has been reached might be eliminated
				for(int j = 0; j < arity && curNodes < numNodes; j++) {
					child = new Node(makeLabels);
					parent.getChildren().add(child);
					child.setParent(parent);
					curNodes++;
					q.offer(child);
				}
				parent = q.poll();
			}
		}
		
		return root;
	}
	
	
	public boolean isBinary() {
		return false;
	}
	
	public String toString() {
		return "Complete Tree";
	}


	@Override
	public boolean respectsNodesNumAndArity() {
		return true;
	}


	@Override
	public boolean acceptsUnboundedDegree() {
		return false;
	}


	@Override
	public boolean readsPTBFiles() {
		return false;
	}
}
