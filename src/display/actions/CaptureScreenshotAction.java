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
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import componentImage.SaveComponentImage;

import display.components.TreeFrame;

/*
 * Allows user to choose location to save a screenshot of the program.
 * At this point does NOT capture title bar, and the color of JLabeledTextFields seems lighter.
 */
public class CaptureScreenshotAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static Preferences prefs;

	public CaptureScreenshotAction(String str, String toolTip, Icon icon) {
		super(str, icon);
		prefs = Preferences.userNodeForPackage(CaptureScreenshotAction.class);
		putValue(SHORT_DESCRIPTION, toolTip);
	}
	public void actionPerformed(ActionEvent arg0) {
		
		String currentPath = null;
		try {
			currentPath = new File(".").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String maybeLastPath = prefs.get("LAST_CS_DIR", currentPath);
		if(new File(maybeLastPath).exists() == false) {
			maybeLastPath = currentPath;
		}
		String chosenPath = new SaveComponentImage().exportComponentImage(TreeFrame.getInstance(),
				new Rectangle(0, 0, (int) TreeFrame.getInstance().getBounds().getWidth(), (int) TreeFrame.getInstance().getBounds().getHeight()),
				"Save Screenshot",
				"screenshot-1",
				maybeLastPath,
				TreeFrame.getInstance());
		if(chosenPath != null) {
			prefs.put("LAST_CS_DIR", new File(chosenPath).getParentFile().getPath());
		}
	}
}
