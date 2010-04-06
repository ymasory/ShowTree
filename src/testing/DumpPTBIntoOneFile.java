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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import util.Load;

import logic.Node;

public class DumpPTBIntoOneFile {

	private static String ptbRoot = "/some/path";
	private static String outputFilePath = "/some/path";
	private static FileWriter fw;
	
	public static void main(String[] args) throws IOException {
		fw = new FileWriter(new File(outputFilePath));
		
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
		fw.write("\n");
		fw.close();
	}
	
	private static void test(Node n, File file, int treeNum) throws IOException {
		fw.write(n.getStringRepresentation() + "\n");
	}
}
