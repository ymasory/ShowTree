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

package testing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import util.Load;

import logic.Node;

/*
 * Confirmed that my loading functions can (1) read in entire PTB, then (2) save them, then (3) reopen them,
 * then (4) resave them, and (2) will match (4). 
 */
public class ConfirmOpenAndSave {

	private static String ptbRoot = "/some/path";
	
	public static void main(String[] args) throws IOException {		
		File ptbDir = new File(ptbRoot);
		for(File ptbSubDir: ptbDir.listFiles()) {
			System.out.println("now on: " + ptbSubDir.getName());
			if(ptbSubDir.isDirectory()) {
				for(File ptbFile: ptbSubDir.listFiles()) {
					if(ptbFile.isFile()) {
						if(ptbFile.getPath().endsWith(".mrg")) {							
							ArrayList<Node> docNodes = Load.loadPTBTrees(new FileReader(ptbFile));
							for(int i = 0; i < docNodes.size(); i++) {
								test(docNodes.get(i), ptbFile, i);
							}
						}
					}
				}
			}
		}
	}
	
	private static void test(Node n, File file, int treeNum) throws IOException {
		String myRepr = n.getStringRepresentation();
		Node nodeFromMyRepr = Load.loadPTBTrees(new StringReader(myRepr)).get(0);
		String resavedRepr = nodeFromMyRepr.getStringRepresentation();
		if(myRepr.equals(resavedRepr) == false) {
			System.out.println("Resave unsuccessful: " + file.getPath() + " " + "tree: " + treeNum);
		}
	}
}
