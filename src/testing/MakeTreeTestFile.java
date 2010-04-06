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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import logic.Node;
import logic.treeBuilders.OrganicTreeBuilder;

public class MakeTreeTestFile {
	
	private static String outputFilePath = "/some/path";
	private static int arity = 10;
	private static int numTrees = 2000;

	public static void main(String[] args) throws IOException {
		FileWriter fw = new FileWriter(new File(outputFilePath));
		OrganicTreeBuilder builder = new OrganicTreeBuilder();
		Node newTree = null;
		for(int i = 1; i <= numTrees; i++) {
			System.out.println("now on tree " + i);
			long startTime = Calendar.getInstance().getTimeInMillis();
			newTree = builder.makeTree(i, arity, false);
			long endTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("that took " + (double)(endTime - startTime)/(double)i + " milliseconds per node");
			fw.write(newTree.getStringRepresentation() + "\n");
		}
		fw.close();
	}
}
