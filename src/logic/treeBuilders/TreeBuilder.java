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

import logic.Node;

/*
 * The abstract class for generating trees.
 */
public abstract class TreeBuilder {

	/*
	 * The main method, for making new trees.
	 * Note that the parameters are not necessarily respected, see respectsNodesNumAndArity().
	 * @param numNodes the desired number of nodes
	 * @param arity the desired maximum degree of nodes in the tree
	 * @returns the root of a new tree
	 */
	public abstract Node makeTree(int numNodes, int arity, boolean makeLabels);
	
	/*
	 * Convenience function for computing logarithms not in base-10.
	 * However, be warned that floating point errors can lead to unexpected floor/ceiling values.
	 * @param x the argument of the mathematical logarithm function
	 * @param b the base of the logarithm
	 * @returns log_b(x), with floating point errors
	 */
	protected double log(int x, int b) {
		return(Math.log(x)/Math.log(b));
	}
	
	/*
	 * @returns true if the TreeBuilder always returns binary trees, false if it returns general trees too
	 */
	public abstract boolean isBinary();
	
	/*
	 * @returns true only if the TreeBuilder cares what the user has entered for arity and number of nodes
	 */
	public abstract boolean respectsNodesNumAndArity();
	
	public abstract boolean acceptsUnboundedDegree();
	
	public abstract boolean readsPTBFiles();
}
