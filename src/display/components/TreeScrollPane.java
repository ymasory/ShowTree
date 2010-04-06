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

package display.components;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/*
 * The TreeFrame's content pane. It is used to store a TreePane (a JPanel) where the drawing is actually done.
 */
//IMPROVE bottom scrollbar only appears when needed on F's computer
//IMPROVE scrolling sideways with mouse would be nice
public class TreeScrollPane extends JScrollPane {
	
	private static TreeScrollPane instance;
	
	private static final long serialVersionUID = 1L;

	private TreeScrollPane() {
		setViewportView(TreePane.getInstance());
		setPreferredSize(new Dimension(600,0));
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getVerticalScrollBar().setUnitIncrement(60);
		getHorizontalScrollBar().setUnitIncrement(60);
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), 
				BorderFactory.createLoweredBevelBorder()));
	}
	
	public static TreeScrollPane getInstance() {
		if (instance == null) {
			instance = new TreeScrollPane();
		}
		return instance;
	}
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}