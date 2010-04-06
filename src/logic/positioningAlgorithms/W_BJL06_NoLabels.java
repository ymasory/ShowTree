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
import logic.treeIterators.PreorderIterator;

/*
 * Implements:
 * C. Buchheim, M. Junger, and S. Leipert. Drawing rooted trees in linear 
 * time. Software: Practice and Experience, 36(6):651�665, 2006.
 * 
 * Original (quadratic time) algorithm due to:
 * J.Q. Walker II. A node-positioning algorithm for general trees. Software: 
 * Practice and Experience, 20(7):685�705, 1990.
 */
//FIXME a separate NoLabels class should not be needed, fix all issues surrounding displaying and not displaying labels
public class W_BJL06_NoLabels implements PositioningAlgorithm {
	
	// use of global defaultAncestor different from BJL
	Node defaultAncestor;
	
	public void embed(Node n, double s) {
		n.assignDepths();
		initializeTree(n);
		initializeNumber(n);
		//assigned x/y positions will be meaningless, this is done for dimensions only
		firstWalk(n, s);
		secondWalk(n, -n.getPrelim());
	}

	
	private void initializeTree(Node n) {
		n.setModifier(0);
		n.setThread(null);
		n.setAncestor(n);
		n.setChange(0);
		n.setShift(0);
		for(int i = 0; i < n.degree(); i++) {
			initializeTree(n.getChildren().get(i));
		}
	}
	
	/*
	 * Initializes the nodes' number field to depth
	 */
	private void initializeNumber(Node n) {
		PreorderIterator iter = new PreorderIterator(n);
		Node cur;
		while(iter.hasNext()) {
			cur = iter.next();
			cur.setNumber(cur.getDepth());
		}
	}
	
	private void firstWalk(Node v, double s) {
		if(v.isLeaf()) {
			v.setPrelim(0);
			if(v.hasLeftSibling()) {
				Node w = v.getLeftSibling();
				v.setPrelim(w.getPrelim() + s);
			}
		}
		else {
			defaultAncestor = v.getChildren().get(0);
			for(int i = 0; i < v.degree(); i++)	{
				Node w = v.getChildren().get(i);
				firstWalk(w, s);
				apportion(w, s);
			}
			executeShifts(v);

			double midpoint = 0.5 * (v.getChildren().get(0).getPrelim() + v.getChildren().get(v.degree() - 1).getPrelim());
			
			if(v.hasLeftSibling()) {
				Node w = v.getLeftSibling();
				v.setPrelim(w.getPrelim() + s);
				v.setModifier(v.getPrelim() - midpoint);
			}
			else {
				v.setPrelim(midpoint);
			}
		}
	}

	private void apportion(Node v, double s) {
		Node v_inside_right = null;
		Node v_outside_right = null;
		Node v_inside_left = null;
		Node v_outside_left = null;
		
		double s_inside_right = 0;
		double s_outside_right = 0;
		double s_inside_left = 0;
		double s_outside_left = 0;
		
		if(v.hasLeftSibling()) {
			Node w = v.getLeftSibling();
			
			v_inside_right = v;
			v_outside_right = v;
			v_inside_left = w;
			v_outside_left = v_inside_right.getParent().getChildren().get(0);
			
			s_inside_right = v_inside_right.getModifier();
			s_outside_right = v_outside_right.getModifier();
			s_inside_left = v_inside_left.getModifier();
			s_outside_left = v_outside_left.getModifier();
			
			while(nextRight(v_inside_left) != null && nextLeft(v_inside_right) != null) {
				v_inside_left = nextRight(v_inside_left);
				v_inside_right = nextLeft(v_inside_right);
				v_outside_left = nextLeft(v_outside_left);
				v_outside_right = nextRight(v_outside_right);
				
				v_outside_right.setAncestor(v);
				
				double shift = (v_inside_left.getPrelim() + s_inside_left) 
				- (v_inside_right.getPrelim() + s_inside_right)
				+ s;
				
				if (shift > 0)	{
					moveSubtree(ancestor(v_inside_left, v), v, shift);
					s_inside_right += shift;
					s_outside_right += shift;
				}
				s_inside_left += v_inside_left.getModifier();
				s_inside_right += v_inside_right.getModifier();
				s_outside_left += v_outside_left.getModifier();
				s_outside_right += v_outside_right.getModifier();
			}
			//BJL has these next two conditionals one block in
			if(nextRight(v_inside_left) != null && nextRight(v_outside_right) == null) {
				v_outside_right.setThread(nextRight(v_inside_left));
				v_outside_right.setModifier(v_outside_right.getModifier() + s_inside_left - s_outside_right);
			}
			if (nextLeft(v_inside_right) != null && nextLeft(v_outside_left) == null)	{
				v_outside_left.setThread(nextLeft(v_inside_right));
				v_outside_left.setModifier(v_outside_left.getModifier() + s_inside_right - s_outside_left);
				defaultAncestor = v;
			}
		}
	}

	private Node nextLeft(Node v) {
		if (v.isLeaf() == false) {
			return v.getChildren().get(0);
		}
		else {
			return v.getThread();
		}
	}

	private Node nextRight(Node v) {
		if (v.isLeaf() == false)	{
			return v.getChildren().get(v.degree() - 1);
		}
		else {
			return v.getThread();
		}
	}
	
	private void moveSubtree(Node w_left, Node w_right, double shift) {
		// different from BJL
		double subtrees = Math.max(1, w_right.getNumber() - w_left.getNumber());
		
		w_right.setChange(w_right.getChange() - shift/subtrees);		
		w_right.setShift(w_right.getShift() + shift);		
		w_left.setChange(w_left.getChange() + shift/subtrees);
		w_right.setPrelim(w_right.getPrelim() + shift);		
		w_right.setModifier(w_right.getModifier() + shift);
	}
	
	private void executeShifts(Node v) {
		double shift = 0;
		double change = 0;
		for(int i = v.degree() - 1; i >= 0; i--) {
			Node w = v.getChildren().get(i);			
			w.setPrelim(w.getPrelim() + shift);
			w.setModifier(w.getModifier() + shift);
			change += w.getChange();
			shift += w.getShift() + change;
		}
	}

	private Node ancestor(Node v_inside_left, Node v) {
		if (v_inside_left.getAncestor().getParent() == v.getParent() && v_inside_left != v) {
			return v_inside_left.getAncestor();
		}
		else {
			return defaultAncestor;
		}
	}

	private void secondWalk(Node v, double m) {
		v.setX(v.getPrelim() + m);
		v.setY(v.getDepth() * Start.Y_SEPARATION);		
		for(int i = 0; i < v.degree(); i++) {
			secondWalk(v.getChildren().get(i), m + v.getModifier());
		}
	}

	public boolean isBinary() {
		return false;
	}

	public boolean handlesNodeWidths() {
		return false;
	}

	public String toString() {
		return "n: W_BJL06 NL";
	}
}
