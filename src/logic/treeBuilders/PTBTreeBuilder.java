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

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import util.Load;

import logic.Node;
import display.Start;
import display.components.TreeFrame;

/*
 * Class randomly chooses a tree from the files in the ptb directory.
 * If no directory is set, the user will be prompted to provide one. 
 */
public class PTBTreeBuilder extends TreeBuilder {

	/*
	 * Ignores the provided numNodes and arity
	 */
	public Node makeTree(int numNodes, int arity, boolean makeLabels) {
		String ptbDirPath = Start.getPtbDirPath();
		if(ptbDirPath == null) {
			JOptionPane.showMessageDialog(TreeFrame.getInstance(), "I have not been given a directory to find .mrg files!\n" +
					"Please select " + toString() + 
					" again and provide a good directory." +
					"or select a different Tree Builder");
			return null;
		}
		File ptbDir = new File(ptbDirPath);
		ArrayList<File> goodMRGs = new ArrayList<File>();
		for(File f: ptbDir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				if(name.endsWith(".mrg")) {
					return true;
				}
				return false;
			}	
		})) {
			goodMRGs.add(f);
		}
		if(goodMRGs.size() < 1) {
			JOptionPane.showMessageDialog(TreeFrame.getInstance(), "There are no .mrg files in " + ptbDir.getPath() + "! Please select " + toString() + " again and provide a good directory.");
			return null;
		}
		
		Random rand = new Random();
		File randMRG = goodMRGs.get(rand.nextInt(goodMRGs.size()));
		ArrayList<Node> docNodes = null;
		try {
			docNodes = Load.loadPTBTrees(new FileReader(randMRG));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(docNodes == null || docNodes.size() == 0) {
			JOptionPane.showMessageDialog(TreeFrame.getInstance(), "Can't read " + randMRG.getAbsolutePath());
			return null;
		}
		Node randTree = docNodes.get(rand.nextInt(docNodes.size()));
		
		return randTree;
	}
	
	public boolean isBinary() {
		return false;
	}

	public String toString() {
		return "Penn Treebank Tree";
	}

	@Override
	public boolean respectsNodesNumAndArity() {
		return false;
	}

	@Override
	public boolean acceptsUnboundedDegree() {
		return false;
	}

	@Override
	public boolean readsPTBFiles() {
		return true;
	}
}
