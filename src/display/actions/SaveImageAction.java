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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import componentImage.SaveComponentImage;

import display.DrawTree;
import display.Start;
import display.components.TreeFrame;
import display.components.TreePane;

/*
 * Allows user to choose location to save an image of the current tree, nicely cropped to size.
 */
public class SaveImageAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static Preferences prefs;

	public SaveImageAction(String str, String toolTip, Icon icon) {
		super(str, icon);
		prefs = Preferences.userNodeForPackage(SaveImageAction.class);
		putValue(SHORT_DESCRIPTION, toolTip);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, Start.getCommandKey() | InputEvent.SHIFT_MASK));
	}

	public void actionPerformed(ActionEvent ae) {
		if(Start.getRoot()==null) {
			JOptionPane.showMessageDialog(TreeFrame.getInstance(), "Nothing to save!");
			return;
		}

		String currentPath = null;
		try {
			currentPath = new File(".").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String savedPath = prefs.get("LAST_SI_DIR", currentPath);
		if(new File(savedPath).exists() == false) {
			savedPath = currentPath;
		}

		Rectangle treeRect = DrawTree.computeTreeGraphicalRectangle(Start.getRoot(), TreePane.getInstance().getGraphics());
		Rectangle cropRect = new Rectangle((int) treeRect.getX() - Start.PADDING, (int) treeRect.getY() - Start.PADDING,
				(int) (treeRect.getWidth() + 2 * Start.PADDING), (int) (treeRect.getHeight() + 2 * Start.PADDING));
		System.out.println(treeRect);
		System.out.println(cropRect);
		System.out.println();
		String chosenPath= new SaveComponentImage().exportComponentImage(TreePane.getInstance(),
				cropRect,
				"Save Tree Image",
				"tree-1",
				savedPath,
				TreeFrame.getInstance());
		if(chosenPath != null) {
			prefs.put("LAST_SI_DIR", new File(chosenPath).getParentFile().getPath());
		}
	}
}