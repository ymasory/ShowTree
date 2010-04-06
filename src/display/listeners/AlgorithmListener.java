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

package display.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import display.Start;
import display.components.TreeMenu;
import display.components.TreePane;

/*
 * Listener is triggered when the user selects a tree positioning algorithm from the drop-down menu.
 */
public class AlgorithmListener implements ActionListener {
	
	private static AlgorithmListener instance;
	
	private AlgorithmListener() {
	}
	
    public static AlgorithmListener getInstance() {
        if (instance == null) {
            instance = new AlgorithmListener();
        }
        return instance;
    }
	
    /*
     * If the user changes the currently selected PositioningAlgorithm, the program will try to redisplay
     * the current tree using the new algorithm. However, if the tree is incompatible with the new display
     * algorithm (say the tree is general and the algorithm is binary-only), the TreePane will simply be blanked
     * and the current tree set to null.
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
	public void actionPerformed(ActionEvent arg0) {
			TreeMenu.getInstance().correctOptions();
			if(Start.getRoot() == null) {
				return;
			}
			else {
				if(Start.getRoot().isBinary() || ! TreeMenu.getInstance().getPositioningAlgorithm().isBinary()) {
					TreeMenu.getInstance().getPositioningAlgorithm().embed(Start.getRoot(), Start.getMinXSeparation());
					TreePane.getInstance().displayTree(TreeMenu.getInstance().getPositioningAlgorithm().handlesNodeWidths());
				}
				else {
					TreePane.getInstance().unDisplayTree();
				}
			}			
		}
}