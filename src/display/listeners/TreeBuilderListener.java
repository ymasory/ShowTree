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
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import display.Start;
import display.components.TreeFrame;
import display.components.TreeMenu;

/*
 * Listener is triggered when the user selects a tree type from the drop-down menu.
 */
public class TreeBuilderListener implements ActionListener{

	private static Preferences prefs;

	public TreeBuilderListener() {
		prefs = Preferences.userNodeForPackage(TreeBuilderListener.class);		
	}

	/*
	 * If the user selecteds "Penn Treebank Tree" he/she will be prompted to choose path that
	 * contains PTB-format mrg files.
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	//IMPROVE PTB Builder doesn't remember when it has a good directory, need to split into a menu option
	public void actionPerformed(ActionEvent arg0) {
		if(TreeMenu.getInstance().getTreeBuilder().readsPTBFiles()) {
			TreeMenu.getInstance().correctOptions();
			
			String currentPath = null;
			try {
				currentPath = new File(".").getCanonicalPath();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String maybeLastPath = prefs.get("LAST_TB_DIR", currentPath);
			if(new File(maybeLastPath).exists() == false) {
				maybeLastPath = currentPath;
			}
			JFileChooser jfc = new JFileChooser(maybeLastPath);
			jfc.setDialogTitle("Select Directory Containing PTB Format .mrg Files");
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = jfc.showOpenDialog(TreeFrame.getInstance());
			if (result == JFileChooser.APPROVE_OPTION) {
				ArrayList<File> goodMRGs = new ArrayList<File>();
				String path = jfc.getSelectedFile().getPath();
				File ptbDir = new File(path);
				prefs.put("LAST_TB_DIR", ptbDir.getParentFile().getPath());
				for(File f: ptbDir.listFiles(new FilenameFilter(){
					public boolean accept(File dir, String name) {
						if(name.endsWith(".mrg")) {
							return true;
						}
						return false;
					}	
				})) {
					goodMRGs.add(f);
				}
				if(goodMRGs.size() < 1) {
					JOptionPane.showMessageDialog(TreeFrame.getInstance(), "Please provide a directory containing .mrg files!");
					Start.setPtbDirPath(path);
				}
				else {
					Start.setPtbDirPath(path);
				}
			}
		}
		TreeMenu.getInstance().correctOptions();
	}
}


