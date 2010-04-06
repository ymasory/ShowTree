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

import java.util.ArrayList;
import java.util.Random;

import logic.Node;

/*
 * Class for creating randomly generated trees.
 * The algorithm does NOT select the trees _uniformly_ at random.
 * Instead it loops from 1 to the provided numNodes. At each iteration of the looop it is
 * equally likely to attach a new Node to any of the existing Nodes who have not reached the provided max degree.
 */
public class OrganicTreeBuilder extends TreeBuilder {

	//stores nodes whose degree is less than arity (hence eligible for new children) 
	private ArrayList<Node> nodes;
	
	public OrganicTreeBuilder() {
	}
	
	public Node makeTree(int numNodes, int arity, boolean makeLabels) {
		nodes = new ArrayList<Node>();
		Node root = new Node(makeLabels);
		nodes.add(root);
		Random rand = new Random();
		for(int i = 0; i < numNodes - 1; i++) {
			Node child = new Node(makeLabels);
			Node parent = nodes.get(rand.nextInt(nodes.size()));
			child.setParent(parent);
			parent.getChildren().add(child);
			if(parent.degree() >= arity) {
				nodes.remove(parent);
			}
			nodes.add(child);
		}
		return root;
	}
	
	
	public boolean isBinary() {
		return false;
	}
	
	public String toString() {
		return "Organic Tree";
	}

	@Override
	public boolean respectsNodesNumAndArity() {
		return true;
	}

	@Override
	public boolean acceptsUnboundedDegree() {
		return true;
	}

	@Override
	public boolean readsPTBFiles() {
		return false;
	}

}
