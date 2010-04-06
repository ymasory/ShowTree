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

import java.util.Random;

import logic.Node;
import logic.positioningAlgorithms.*;
import logic.treeBuilders.OrganicTreeBuilder;

public class FindPathologicalCases {
	
	private static final int numTests = 1000000;
	private static final int fewestNodes = 20;
	private static final int maxNodes = fewestNodes;
	private static final int arity = 2;
	
	public static final double xSep = 50;
	
	public static final boolean makeLabels = false;
	
	private static PositioningAlgorithm winner = new W_BJL06();
	private static PositioningAlgorithm loser = new LevelSlide();
	
	public static void main(String[] args) {
		Random rand = new Random();
		OrganicTreeBuilder build = new OrganicTreeBuilder();
		
		double biggestSSDiffAmount = 0;
		Node biggestSSDiffTree = null;
		double biggestTWDiffAmount = 0;
		Node biggestTWDiffTree = null;
		
		Node cur;
		for(int i = 0; i < numTests; i++) {
			System.out.println("test number " + (i + 1));
			
			int numNodes = rand.nextInt(maxNodes - fewestNodes + 1) + fewestNodes;
			cur = build.makeTree(rand.nextInt(numNodes), arity, makeLabels);
			
			winner.embed(cur, xSep);
			double winnerTWVal = cur.getEmbeddedWidth();
			double winnerSSVal = cur.getEmbeddedSiblingSpacing();

			
			loser.embed(cur, xSep);
			double loserTWVal = cur.getEmbeddedWidth();
			double loserSSVal = cur.getEmbeddedSiblingSpacing();

			
			double diffTW = loserTWVal - winnerTWVal;
			double diffSS = loserSSVal - winnerSSVal;
			
			if(diffTW > biggestTWDiffAmount) {
				biggestTWDiffAmount = diffTW;
				biggestTWDiffTree = cur;
			}
			
			if(diffSS > biggestSSDiffAmount) {
				biggestSSDiffAmount = diffSS;
				biggestSSDiffTree = cur;
			}
		}
		
		System.out.println("biggest tree width diff: " + biggestTWDiffAmount);
		if(biggestTWDiffTree != null) {
			System.out.println(biggestTWDiffTree.getStringRepresentation());		
		}

		System.out.println("biggest sibling spacing diff: " + biggestSSDiffAmount);
		if(biggestSSDiffTree != null) {
			System.out.println(biggestSSDiffTree.getStringRepresentation());		
		}
	}	
}
