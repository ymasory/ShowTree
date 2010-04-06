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

package display.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import display.Start;
import display.components.TreePane;

/*
 * Switches on/off the display of internally used rectangles around drawings of Nodes, for debugging purposes.
 */
public class ShowNodeBoundsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public ShowNodeBoundsAction(String str, String toolTip, Icon icon) {
		super(str, icon);
		putValue(SHORT_DESCRIPTION, toolTip);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, Start.getCommandKey()));
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
	}

	public void actionPerformed(ActionEvent ae) {
		Start.setShowNodeBoundsOption(!Start.isShowNodeBounds());
		TreePane.getInstance().repaint();
	}

}
