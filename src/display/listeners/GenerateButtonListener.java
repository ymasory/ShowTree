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

package display.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import logic.Node;
import logic.positioningAlgorithms.PositioningAlgorithm;
import logic.treeBuilders.TreeBuilder;
import display.Start;
import display.components.TreeFrame;
import display.components.TreeMenu;
import display.components.TreePane;

/*
 * Listener is triggered when user clicks the generate button.
 */
public class GenerateButtonListener implements ActionListener {
	
	/*
	 * Processes the currently entered numNodes, arity, and minSeparation values.
	 * Attempts to create a tree on this basis using selected TreeBuilder and display using the 
	 * currently selected PositioningAlgorithm. This many not be possible if bad or incompatible values
	 * have been entered.
	 * (non-Javadoc) 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {
		TreeBuilder builder = TreeMenu.getInstance().getTreeBuilder();
		PositioningAlgorithm alg = TreeMenu.getInstance().getPositioningAlgorithm();
		
		int arity = 0;
		int numNodes = 0;
		if(builder.respectsNodesNumAndArity()) {
			arity = processArity();
			numNodes = processNumNodes();
			if(arity > -1) {
				Start.setArity(arity);
			}
			else {
				TreePane.getInstance().unDisplayTree();
				return;
			}
			if(numNodes < 1) {
				TreePane.getInstance().unDisplayTree();
				return;
			}
		}		

		int separation = processSeparation();
		if(separation > -1) {
			Start.setMinSeparation(separation);
		}
		else {
			TreePane.getInstance().unDisplayTree();
			return;
		}
		
		Node newTree = builder.makeTree(numNodes, arity, true);
		if(newTree.isBinary() == false && alg.isBinary() == true) {
			JOptionPane.showMessageDialog(TreeFrame.getInstance(), "Sorry, the next tree has higher arity than 2, and you have a binary algorithm selected.\n" +
					"Generate another tree or switch to a n-ary algorithm");
			TreePane.getInstance().unDisplayTree();
			return;
		}
		
		Start.setRoot(newTree);
		
		if(newTree != null) {
			alg.embed(Start.getRoot(), separation);
		}
		TreePane.getInstance().displayTree(TreeMenu.getInstance().getPositioningAlgorithm().handlesNodeWidths());
	}

	
	
	/*
	 * Makes sure arity is an integer. Non-positive arity represents unbounded degree, which
	 * has no meaning with the CompleteTree builder.
	 * If the value is good, saves it for use in current tree generation.
	 */
	private int processArity() {
		int arity = 0;
		try {
			arity = TreeMenu.getInstance().getSelectedArity();
		}
		catch(java.lang.NumberFormatException e) {
			JOptionPane.showMessageDialog(TreeFrame.getInstance(), "arity must be an integer!\nnon-positive integer represents unbounded arity");
			return -1;
		}
		if(!TreeMenu.getInstance().getTreeBuilder().acceptsUnboundedDegree())  {
			if(arity == 0) {
				JOptionPane.showMessageDialog(TreeFrame.getInstance(), 
						"Unbounded degree has no meaning with " + TreeMenu.getInstance().getTreeBuilder().toString() + "!");
				return -1;
			}
		}
		if(arity < 1) {
			arity = Integer.MAX_VALUE;
		}
		return arity;
	}

	/*
	 * Makes sure the currently displayed numNodes is a positive integer. 
	 * If the value is good, saves it for use in current tree generation.
	 */
	private int processNumNodes() {		
		int numNodes = 0;
		boolean badInput = false;
		try {
			numNodes = TreeMenu.getInstance().getSelectedNumNodes();
		}
		catch(java.lang.NumberFormatException e) {
			badInput = true;
		}
		if(numNodes < 1 || badInput) {
			JOptionPane.showMessageDialog(TreeFrame.getInstance(), "number of nodes must be a positive integer!");				
			return -1;
		}
		return numNodes;
	}

	/*
	 * Makes sure displayed minSeparation is a positive integer.
	 * If the value is good, saves it for use in current tree generation.
	 */
	private int processSeparation() {		
		int separation = 0;
		boolean badInput = false;
		try {
			separation = TreeMenu.getInstance().getSelectedMinSeparation();
		}
		catch(java.lang.NumberFormatException e) {
			badInput = true;
		}
		if(separation < 1 || badInput) {
			JOptionPane.showMessageDialog(TreeFrame.getInstance(), "spacing must be an integer >= 1");
			return -1;				
		}
		return separation;
	}
}
