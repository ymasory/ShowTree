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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import display.DrawTree;
import display.Start;

/*
 * The component where all drawing is ultimately done.
 * paintComponent() and repaint() should NOT be called directly when a _new_ tree is being displayed
 * or the positioning algorithm changed. Use displayTree() instead, which takes as an argument whether
 * labels are to be drawn or not.
 */
public class TreePane extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static TreePane instance;
	
    private static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	private boolean respectsNodeWidths;
    
	private TreePane() {
		setFont(new Font("Serif", Font.PLAIN, 18));
		setLayout(null);
		setOpaque(true);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(0, 0));
	}
	
    public static TreePane getInstance() {
        if (instance == null) {
            instance = new TreePane();
        }
        return instance;
    }
    
    /*
     * @returns a Rectangle representeing the area of the TreePane that is NOT in the edge padding area (where
     * nothing should be drawn)
     */
    private Rectangle getUsableArea() {
    	return new Rectangle(
    			Start.PADDING, 
    			Start.PADDING, 
    			(int)(getSize().getWidth() - 2* Start.PADDING), 
    			(int)(getSize().getHeight() - 2 * Start.PADDING)
    			);
    }
	
    /*
     * Overrides paintComponent to set up rendering hints, which make the edges pretty and smooth,
     * not jagged.
     * (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(renderHints);
        DrawTree.paintTree(g, respectsNodeWidths);
        if(Start.isShowGrid() == true) {
        	drawBoundaries(g);
            drawGrid(g);
        }
    }

	public void displayTree(boolean handlesWidths) {
		respectsNodeWidths = handlesWidths;
		repaint();
	}
	
	public void unDisplayTree() {
		Start.setRoot(null);
		repaint();
	}
    
    /*
     * Draws grid on TreePane for debugging purposes.
     * The unit used is the currently saved (not the same as the displayed) minSeparation value.
     * Grid begins and is confined to the usable area (meaning not in the padded borders around the TreePane.
     */
    private void drawGrid(Graphics g) {
    	Rectangle rect = getUsableArea();
    	double width = rect.getWidth();
    	double height = rect.getHeight();
    	for(int i = Start.PADDING; i < width; i+=Start.getMinXSeparation()) {
    		g.drawLine(i, Start.PADDING, i, (int) height + Start.PADDING);
    	}
//    	for(int i = Start.PADDING; i < height; i+=Start.getMinXSeparation()) {
//    		g.drawLine(Start.PADDING, i, (int) width + Start.PADDING, i);
//    	}
    }
    
    /*
     * Draws rectangle around the usable area of the TreePane (meaning area not including padded borders
     * where nothing is drawn). This is really just to provide a line at the bottom and right, to give a 
     * visual cue that the grid ends deliberately.
     */
    private void drawBoundaries(Graphics g) {
      Rectangle rect = getUsableArea();
      g.drawRect(
      		(int)(rect.getX()), 
      		(int)(rect.getY()), 
      		(int)(rect.getWidth()),
      		(int)(rect.getHeight()));
    }
}
