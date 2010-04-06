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

public class CompareTwoAlgorithms {
	private static final int numTests = 2000;
	public static final int arity = 2;
	
	public static final double xSep = 50;
	
	public static final boolean makeLabels = false;
	
	private static PositioningAlgorithm first = new LevelSlide();
	private static PositioningAlgorithm second = new W_BJL06();
	
	public static void main(String[] args) {
		Random rand = new Random();
		OrganicTreeBuilder build = new OrganicTreeBuilder();
		
		double firstWins = 0;
		double secondWins = 0;
		double ties = 0;
		
		Node cur;
		for(int i = 0; i < numTests; i++) {
			System.out.println("number of nodes " + (i + 1));
			
			int numNodes = rand.nextInt(i + 1) + 1;
			cur = build.makeTree(rand.nextInt(numNodes), arity, makeLabels);
			
			first.embed(cur, xSep);
			double firstWidth = cur.getEmbeddedWidth();
			
			second.embed(cur, xSep);
			double secondWidth = cur.getEmbeddedWidth();
			
			if(firstWidth == secondWidth) {
				ties++;
			}
			if(firstWidth < secondWidth) {
				firstWins++;
			}
			if(secondWidth < firstWidth) {
				secondWins++;
			}
		}
		System.out.println("I tested on " + numTests + " trees, incrementing from 1 node to " + numTests);
		System.out.println(first.toString() + " won " + firstWins/numTests * 100 + "%");
		System.out.println(second.toString() + " won " + secondWins/numTests * 100 + "%");
		System.out.println("They tied " + ties/numTests * 100 + "%");
		
	}	
}
