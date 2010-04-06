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

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import display.Start;
import display.components.TreeFrame;

/*
 * Launches window that displays about.txt file.
 */
public class AboutAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public AboutAction(String str, String toolTip, Icon icon) {
		super(str, icon);
		putValue(SHORT_DESCRIPTION, toolTip);
	}

	public void actionPerformed(ActionEvent ae) {
		try {
			JFrame jf = new JFrame();
			JEditorPane jp = new JEditorPane();
			URL aboutURL = Start.class.getResource("/about.txt");
			jp.setPage(aboutURL);
			jp.setFont(new Font(null, Font.PLAIN, 14));
			jp.setEditable(false);
			JScrollPane jsc = new JScrollPane(jp);
			jf.add(jsc);
			jf.setBounds((int) (Toolkit.getDefaultToolkit().getScreenSize().width/2 - 300),
					(int) (Toolkit.getDefaultToolkit().getScreenSize().height/2 - 250),
					525,
					300);
			int xPos = (TreeFrame.getInstance().getWidth() - jf.getWidth())/2 + (int) TreeFrame.getInstance().getLocation().getX();
			int yPos = (TreeFrame.getInstance().getHeight() - jf.getHeight())/2 + (int) TreeFrame.getInstance().getLocation().getY();
			jf.setLocation(xPos, yPos);
			jf.setVisible(true);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();	
		}
	}
}