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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import logic.Node;
import display.Start;
import display.components.TreeFrame;

/*
 * Allows user to choose location to save a bracketed representation of the displayed tree.
 * The saved trees are NOT exactly in PTB format. See Node.getStringRepresentation() for more info.
 */
public class SaveTreeAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private static Preferences prefs;

	public SaveTreeAction(String str, String toolTip, Icon icon) {
		super(str, icon);
		prefs = Preferences.userNodeForPackage(SaveTreeAction.class);
		putValue(SHORT_DESCRIPTION, toolTip);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, Start.getCommandKey()));
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
	}
	
	public void actionPerformed(ActionEvent e) {
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
		String maybeLastPath = prefs.get("LAST_ST_DIR", currentPath);
		if(new File(maybeLastPath).exists() == false) {
			maybeLastPath = currentPath;
		}
		
		JFileChooser jfc = new JFileChooser(maybeLastPath);
	    jfc.setDialogTitle("Save Tree");
		jfc.setAcceptAllFileFilterUsed(false);
	    jfc.setSelectedFile(new File("tree-1"));
	    jfc.setFileFilter(new PTBFileFilter());
	    
	    
		int result = jfc.showSaveDialog(TreeFrame.getInstance());
		if (result == JFileChooser.APPROVE_OPTION) {
			String path = jfc.getSelectedFile().getPath();
			prefs.put("LAST_ST_DIR", new File(path).getParentFile().getPath());
			if(path.endsWith(".mrg") == false) {
				path += ".mrg";
			}
			try {
				Node root = Start.getRoot();
				Writer w = new FileWriter(path);
				w.write(root.getStringRepresentation());
				w.write("\n");
				w.close();
			} 
			catch (IOException e1) {
				JOptionPane.showMessageDialog(TreeFrame.getInstance(), e1.getMessage());
				e1.printStackTrace();
			}
		}
	}
}
