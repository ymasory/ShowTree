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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import edu.upenn.cis.ptb.PTBLoaderImpl;
import edu.upenn.cis.ptb.PTBTreeNode;

import logic.Node;
import logic.treeIterators.PreorderIterator;

public class Load {

	/*
	 * Parses input and returns an ArrayList of the trees extracted
	 * @param in - a Reader of the source of the trees
	 * @returns an ArrayList of Nodes (the roots of the trees)
	 */
	public static ArrayList<Node> loadPTBTrees(Reader in) throws IOException {
		PTBLoaderImpl loader = new PTBLoaderImpl();
		PTBTreeNode docPRoot = loader.load(in);
		Node docNRoot = NodeRepresentationConversion.PDTBTreeNodeToNode(docPRoot);

		ArrayList<Node> docNodes = new ArrayList<Node>();

		for(int i = 0; i < docNRoot.degree(); i++) {
			docNRoot.getChildren().get(i).setParent(null);
			docNodes.add(docNRoot.getChildren().get(i));
		}

		for(int i = 0; i < docNodes.size(); i++) {
			PreorderIterator iter = new PreorderIterator(docNodes.get(i));
			Node cur;
			while(iter.hasNext()) {
				cur = iter.next();
				if(cur.getLabel().equals("@_EMPTYSTRING_@")) {
					cur.setLabel("");
				}
				if(cur.getLabel().contains(new StringBuffer("@_LRB_@"))) {
					cur.setLabel(cur.getLabel().replaceAll("@_LRB_@", "("));
				}
				if(cur.getLabel().contains(new StringBuffer("@_RRB_@"))) {
					cur.setLabel(cur.getLabel().replaceAll("@_RRB_@", ")"));
				}
			}
		}
		return docNodes;
	}
}
