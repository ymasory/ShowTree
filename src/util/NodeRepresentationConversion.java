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

package util;

import logic.Node;
import edu.upenn.cis.ptb.PTBLabelImpl;
import edu.upenn.cis.ptb.PTBTreeNode;
import edu.upenn.cis.ptb.PTBTreeNodeImpl;

/*
 * Converts from Node to and from the PTB API's nodes 
 */
public class NodeRepresentationConversion {

	public static Node PDTBTreeNodeToNode(PTBTreeNode pRoot) {
		Node nRoot = new Node(false);
		PDTBTreeNodeToNodeHelper(pRoot, nRoot);
		return nRoot;
	}
	
	private static void PDTBTreeNodeToNodeHelper(PTBTreeNode p, Node n) {
		n.setLabel(p.getLabel().getRaw());
		for(int i = 0; i < p.getChildCount(); i++) {
			Node nChild = new Node(false);
			nChild.setParent(n);
			n.getChildren().add(nChild);
		}
		for(int i = 0; i < n.degree(); i++) {
			PDTBTreeNodeToNodeHelper((PTBTreeNode) p.getChildAt(i), n.getChildren().get(i));
		}
	}
	
	public static PTBTreeNodeImpl NodetoPDTBTreeNodeImpl(Node nRoot) {
		PTBTreeNodeImpl pRoot = new PTBTreeNodeImpl(new PTBLabelImpl("", "", 0, 0, ""));
		NodetoPDTBTreeNodeImplHelper(pRoot, nRoot);
		return pRoot;
	}
	
	private static void NodetoPDTBTreeNodeImplHelper(PTBTreeNode p, Node n) {
		p.setLabel(new PTBLabelImpl(n.getLabel(), "", 0, 0, n.getLabel()));
		for(int i = 0; i < n.degree(); i++) {
			PTBTreeNode pChild = new PTBTreeNodeImpl(new PTBLabelImpl("", "", 0, 0, ""));
			pChild.setParent(p);
			p.ptbAppendChild(pChild);
		}
		for(int i = 0; i < p.getChildCount(); i++) {
			NodetoPDTBTreeNodeImplHelper((PTBTreeNode) p.getChildAt(i), n.getChildren().get(i));
		}
	}
	
}
