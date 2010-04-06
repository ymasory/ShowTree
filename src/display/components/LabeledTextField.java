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

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * A label next to a text field, so the label can identify the text field.
 * This is used for the arity, numNodes, and minSeparation text fields.
 */
public class LabeledTextField extends JPanel{

	private static final long serialVersionUID = 1L;

	private JTextField jtf;
	private JLabel jl;

	public LabeledTextField(String label, int defaultValue, int numColumns) {
        jtf = new JTextField(Integer.toString(defaultValue));
        jtf.setColumns(numColumns);
        jl = new JLabel(label);
        jl.setLabelFor(jtf);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(jl);
        add(jtf);
//        setMaximumSize(new Dimension(200,100));
	}

	/*
	 * Enables and disables (grays out) the internal components.
	 */
	public void setAllEnabled(boolean b) {
		setEnabled(b);
		jtf.setEnabled(b);
		jtf.setEditable(b);
		jl.setEnabled(b);
	}

	/*
	 * Sets the text in the JTextField
	 * @param string - the String to be used
	 */
	public void setText(String string) {
		jtf.setText(string);
	}

	/*
	 * @returns the text currently in the JTextField
	 */
	public String getText() {
		return jtf.getText();
	}
}
