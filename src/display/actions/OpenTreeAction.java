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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import util.Load;

import logic.Node;
import display.Start;
import display.components.TreeFrame;
import display.components.TreeMenu;
import display.components.TreePane;

/*
 * Allows user to select a PTB-format file to read a tree from.
 * Substitutions are performed for empty string labels and round parens.
 * See Node.getStringRepresentation() for more info.
 */
//IMPROVE on F's computer selecting directories clears provided file names in jfilechoosers
public class OpenTreeAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static Preferences prefs;

	public OpenTreeAction(String str, String toolTip, Icon icon) {
		super(str, icon);
		prefs = Preferences.userNodeForPackage(OpenTreeAction.class);
		putValue(SHORT_DESCRIPTION, toolTip);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, Start.getCommandKey()));
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
	}

	public void actionPerformed(ActionEvent arg0) {

		String currentPath = null;
		try {
			currentPath = new File(".").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String maybeLastPath = prefs.get("LAST_OT_DIR", currentPath);
		if(new File(maybeLastPath).exists() == false) {
			maybeLastPath = currentPath;
		}
		JFileChooser jfc = new JFileChooser(maybeLastPath);
		jfc.setDialogTitle("Open PTB Format File");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setFileFilter(new PTBFileFilter());


		int result = jfc.showOpenDialog(TreeFrame.getInstance());
		if (result == JFileChooser.APPROVE_OPTION) {
			String path = jfc.getSelectedFile().getPath();
			prefs.put("LAST_OT_DIR", new File(path).getParentFile().getPath());
			try {
				ArrayList<Node> docNodes = Load.loadPTBTrees(new FileReader(new File(path)));
				if(docNodes.size() > 0) {
					Node firstTree = docNodes.get(0);
					Start.setRoot(firstTree);
					if(firstTree.isBinary() || TreeMenu.getInstance().getPositioningAlgorithm().isBinary() == false) {
						TreeMenu.getInstance().getPositioningAlgorithm().embed(Start.getRoot(), Start.getMinXSeparation());
					}
					else {
						TreeMenu.getInstance().restoreFirstAlgorithm();
						TreeMenu.getInstance().getFirstAlgorithm().embed(Start.getRoot(), Start.getMinXSeparation());
					}
					TreePane.getInstance().displayTree(TreeMenu.getInstance().getPositioningAlgorithm().handlesNodeWidths());
				}
				else {
					TreePane.getInstance().unDisplayTree();
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
				TreePane.getInstance().unDisplayTree();
			}
		}
	}
}
