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
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import display.Start;

/*
 * Minimally tweaked JFrame
 */
//IMPROVE resizing upward impossible on F's computer
public class TreeFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	private  static TreeFrame instance;
	
	private TreeFrame() {
        setSize(new Dimension(Start.WIDTH, Start.HEIGHT));
        setTitle("ShowTree");
        setJMenuBar(TreeMenu.getInstance());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(getExtendedState());
        setContentPane(TreeScrollPane.getInstance());
        lockSize();
        TreeMenu.getInstance().correctOptions();
	}
	
    public static TreeFrame getInstance() {
        if (instance == null) {
            instance = new TreeFrame();
        }
        return instance;
    }
    
    /*
     * Ensures user cannot resize frame to be smaller than it is right now.
     * This is for the sake of correct layout of TreeMenu components.
     * @author http://forum.java.sun.com/thread.jspa?threadID=328852&messageID=1983147
     */
    private void lockSize() {                                                                                              
        final int origX = getSize().width;
        final int origY = getSize().height;
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                setSize(
                        (getWidth() < origX) ? origX : getWidth(),
                                (getHeight() < origY) ? origY : getHeight());
            }
        });
    }
}
