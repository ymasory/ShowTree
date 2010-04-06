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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import display.Start;

import util.Load;

import logic.Node;
import logic.positioningAlgorithms.*;

public class CompareAllAlgorithms {

	private static String testTreesPath = Constants.binaryTreesFilePath;
	private static String outPath = Constants.outputDirPath + "/alg_comparison.txt";

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(testTreesPath)));
		FileWriter fw = new FileWriter(new File(outPath));
		String curLine = reader.readLine();

		ArrayList<PositioningAlgorithm> algs = new ArrayList<PositioningAlgorithm>();
		algs.add(new LevelSlide());
		algs.add(new W_BJL06_NoLabels());
		algs.add(new W_BJL06());
		algs.add(new LeavesInorder());
		algs.add(new WS79_Tidy());
		algs.add(new WS79_Narrowest());
		algs.add(new Vaucher80());
		algs.add(new Sweet78());
		algs.add(new Knuth71WithCentering());
		algs.add(new Knuth71());

		fw.write("num nodes\t");
		for(int i = 0; i < algs.size(); i++) {
			fw.write(algs.get(i).toString());
			fw.write("\t");
		}
		fw.write("\n");
		
		int numNodes = 1;
		//Parsing one line at a time for speed
		while(curLine != null) {

			ArrayList<Node> nodes = Load.loadPTBTrees(new StringReader(curLine));
			if(nodes.size() > 0) {
				Node root = nodes.get(0);

				//TEST
				System.out.println("nodes: " + numNodes);
				fw.write(numNodes + "\t");

				for(int i = 0; i < algs.size(); i++) {
					algs.get(i).embed(root, Start.DEFAULT_MIN_X_SEPARAION);
					fw.write(Double.toString(root.getEmbeddedWidth()));
					fw.write("\t");
				}

				fw.write("\n");
			}

			numNodes++;
			curLine = reader.readLine();
		}
		fw.write("\n");
		fw.close();
	}
}
