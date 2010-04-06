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

package logic.positioningAlgorithms;

import logic.Node;

/*
 * The interface that all positioning algorithms implement.
 * Note that PositioningAlgorithm do NOT actually draw anything, they merely give the nodes x and y coordinates.
 * The DrawTree actually does the drawing, and translates the x and y coordinates assigned here.
 */
public interface PositioningAlgorithm {

	public void embed(Node n, double s);
	
	/*
	 * @returns true if the algorithm can only position binary trees, false if it can position general ones
	 */
	public boolean isBinary();
	
	/*
	 * DrawTree will only draw the labels of trees if the positioner knows how to handle the accompanying
	 * problem of variable widths.
	 * @returns true if the positioning algorithm knows what to do with nodes that have variable width
	 */
	public boolean handlesNodeWidths();
}
