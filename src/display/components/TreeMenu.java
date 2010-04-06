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

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logic.positioningAlgorithms.*;
import logic.treeBuilders.CompleteTreeBuilder;
import logic.treeBuilders.OrganicTreeBuilder;
import logic.treeBuilders.PTBTreeBuilder;
import logic.treeBuilders.TreeBuilder;
import display.Start;
import display.actions.AboutAction;
import display.actions.CaptureScreenshotAction;
import display.actions.ExitAction;
import display.actions.OpenTreeAction;
import display.actions.SaveImageAction;
import display.actions.SaveTreeAction;
import display.actions.ShowGridAction;
import display.actions.ShowNodeBoundsAction;
import display.actions.ShowNodeFieldAction;
import display.listeners.AlgorithmListener;
import display.listeners.GenerateButtonListener;
import display.listeners.TreeBuilderListener;

/*
 * The menu bar.
 */
public class TreeMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;

	private static TreeMenu instance;
	
	private JButton jbGenerate;
	private JComboBox jcbAlgorithm;
	private JComboBox jcbTree;
	private LabeledTextField ltfNumNodes;
	private LabeledTextField ltfArity;
	private LabeledTextField ltfMinSeparation;
	
	private TreeMenu() {
		initMenu();
		initAlgorithmMenu();
		initTreeBuilderMenu();
		initLTFs();
		initGenerate();
	}
	
    public static TreeMenu getInstance() {
        if (instance == null) {
            instance = new TreeMenu();
        }
        return instance;
    }
	
    /*
     * Adds components to the TreeMenu.
     */
	private void initMenu() {
        JMenu jmMenu = new JMenu("Menu");
        JMenu jmDebug = new JMenu("Debug");
        JMenu jmAppear = new JMenu("Appearance");

        JMenuItem jmiAbout = new JMenuItem(
                new AboutAction("About", null, null));
        JMenuItem jmiOpenTree = new JMenuItem(
                new OpenTreeAction("Open Tree", null, null));
        JMenuItem jmiSaveTree = new JMenuItem(
                new SaveTreeAction("Save Tree", null, null));
        JMenuItem jmiSaveImage = new JMenuItem(
                new SaveImageAction("Save Image", null, null));
        JMenuItem jmiScreenshot = new JMenuItem(
                new CaptureScreenshotAction("Save Screenshot", null, null));
        JMenuItem jmiExit = new JMenuItem(
                new ExitAction("Exit", null, null));
        
        jmMenu.add(jmiAbout);
        jmMenu.addSeparator();
        jmMenu.add(jmiOpenTree);
        jmMenu.add(jmiSaveTree);
        jmMenu.add(jmiSaveImage);
        jmMenu.add(jmiScreenshot);
        jmMenu.addSeparator();
        jmMenu.add(jmAppear);
        jmMenu.add(jmDebug);
        jmMenu.addSeparator();
        jmMenu.add(jmiExit);
        

        JCheckBoxMenuItem jmiGrid = new JCheckBoxMenuItem(
        		new ShowGridAction("Show Grid", null, null));
        JCheckBoxMenuItem jmiNodeBounds = new JCheckBoxMenuItem(
        		new ShowNodeBoundsAction("Show Node Bounds", null, null));
        JMenuItem jmiShiftSums = new JMenuItem(
        		new ShowNodeFieldAction("Show Node Field", null, null));
        jmDebug.add(jmiGrid);
        jmDebug.add(jmiNodeBounds);
        jmDebug.add(jmiShiftSums);
        
        add(jmMenu);
	}
	
	/*
	 * Adds TreeBuilders to the TreeBuilder JComboBox
	 */
	private void initTreeBuilderMenu() {
		jcbTree = new JComboBox();
		jcbTree.addItem(new OrganicTreeBuilder());
		jcbTree.addItem(new PTBTreeBuilder());
		jcbTree.addItem(new CompleteTreeBuilder());
		jcbTree.addActionListener(new TreeBuilderListener());
		add(jcbTree);
	}
	
	/*
	 * Adds PositioningAlgorithm to the PositioningAlgorithm JComboBox
	 * NOTE: The first algorithm must be n-ary, to work with OpenTreeAction.
	 */
	private void initAlgorithmMenu() {
        jcbAlgorithm = new JComboBox();
//        jcbAlgorithm.addItem(new LevelSlide());
        jcbAlgorithm.addItem(new W_BJL06_NoLabels());
        jcbAlgorithm.addItem(new W_BJL06());
        jcbAlgorithm.addItem(new LeavesInorder());
        jcbAlgorithm.addItem(new WS79_Narrowest());
        jcbAlgorithm.addItem(new Vaucher80());
        jcbAlgorithm.addItem(new WS79_Tidy());
        jcbAlgorithm.addItem(new Sweet78());
        jcbAlgorithm.addItem(new Knuth71WithCentering());
        jcbAlgorithm.addItem(new Knuth71());
        jcbAlgorithm.addActionListener(AlgorithmListener.getInstance());
        add(jcbAlgorithm);
	}
	
	/*
	 * Initializes displayed numNodes, arity, and minSeparation to default values.
	 */
	private void initLTFs() {
		ltfNumNodes = new LabeledTextField(" Nodes: ", Start.DEFAULT_NUM_NODES, 3);
		ltfArity = new LabeledTextField(" Arity: ", Start.DEFAULT_ARITY, 2);
		ltfMinSeparation = new LabeledTextField(" Spacing: ", Start.DEFAULT_MIN_X_SEPARAION, 2);
		add(ltfNumNodes);
		add(ltfArity);
		add(ltfMinSeparation);
	}
	
	/*
	 * Initializes Generate button.
	 */
	public void initGenerate() {
        jbGenerate = new JButton("Generate");
        jbGenerate.addActionListener(new GenerateButtonListener());
        add(jbGenerate);
	}
	
	/*
	 * Selects the first PositioningAlgorithm (which is the default) in the positioning algorithms JComboBox.
	 */
	public void restoreFirstAlgorithm() {
		jcbAlgorithm.setSelectedIndex(0);
	}
	
	/*
	 * @returns the first PositioningAlgorithm (which is the default) in the positioning algorithms JComboBox.
	 */
	public PositioningAlgorithm getFirstAlgorithm() {
		return (PositioningAlgorithm) jcbAlgorithm.getItemAt(0);
	}

	/*
	 * Corrects numNodes, arity, and minSeparation LabeledTextField entries to make sure they are compatible
	 * and reasonable. For example, makes sure the arity is 2 and disables the user's ability to change arity
	 * if a binary PositioningAlgorithm is selected. This function is called after the user makes any changes
	 * to items on the TreeMenu.
	 */
	public void correctOptions() {
		PositioningAlgorithm alg = TreeMenu.getInstance().getPositioningAlgorithm();
		TreeBuilder builder = TreeMenu.getInstance().getTreeBuilder();
		if(builder.respectsNodesNumAndArity() == false) {
			TreeMenu.getInstance().setDeadOptions();
		}
		else {
			if(alg.isBinary() == false && builder.isBinary() == false) {
				TreeMenu.getInstance().setGeneralOptions();
			}
			else {
				TreeMenu.getInstance().setBinaryOptions();
			}
		}
	}
	
	/*
	 * Sets TreeMenu options appropriate for a PositioningAlgorithm that can handle general trees.
	 */
	private void setGeneralOptions() {
		try {
			Integer.parseInt(ltfNumNodes.getText());
		}
		catch(NumberFormatException e) {
			ltfNumNodes.setText(Integer.toString(Start.DEFAULT_NUM_NODES));
		}
		try {
			Integer.parseInt(ltfArity.getText());
		}
		catch(NumberFormatException e) {
			ltfArity.setText(Integer.toString(Start.DEFAULT_ARITY));
		}
		ltfArity.setAllEnabled(true);
		ltfNumNodes.setAllEnabled(true);
		ensureXSeparationIsInt();
	}

	
	/*
	 * Sets TreeMenu options appropriate for a PositioningAlgorithm that can handle only binary trees.
	 */
	private void setBinaryOptions() {
		try {
			Integer.parseInt(ltfNumNodes.getText());
		}
		catch(NumberFormatException e) {
			ltfNumNodes.setText(Integer.toString(Start.DEFAULT_NUM_NODES));
		}
		ltfArity.setAllEnabled(false);
		ltfArity.setText("2");
		ltfNumNodes.setAllEnabled(true);
		ensureXSeparationIsInt();
	}
	
	/*
	 * Disables arity and numNodes LabeledTextFields, used for TreeBuilders that ignore user input on
	 * those fields.
	 */
	private void setDeadOptions() {
		ltfArity.setAllEnabled(false);
		ltfArity.setText("");
		ltfNumNodes.setAllEnabled(false);
		ltfNumNodes.setText("");
		ensureXSeparationIsInt();
	}
	
	/*
	 * Makes sure the currently displayed minSeparation value the user entered is an integer.
	 * Resets it to default minSeparation if not.
	 */
	private void ensureXSeparationIsInt() {
		ltfMinSeparation.setAllEnabled(true);
		try {
			Integer.parseInt(ltfMinSeparation.getText().trim());
		}
		catch (java.lang.NumberFormatException ex){
			ltfMinSeparation.setText(Integer.toString(Start.DEFAULT_MIN_X_SEPARAION));
		}
		
	}
	
	/*
	 * @returns the currently displayed Display Algorithm
	 */
	public PositioningAlgorithm getPositioningAlgorithm() {
		return (PositioningAlgorithm)jcbAlgorithm.getSelectedItem();
	}
	
	/*
	 * @returns the currently displayed TreeBuilder
	 */
	public TreeBuilder getTreeBuilder() {
		return (TreeBuilder)jcbTree.getSelectedItem();
	}
	
	/*
	 * @returns the currently displayed minSeparation value, converted to an int
	 */
	public int getSelectedMinSeparation() {
		return Integer.parseInt(ltfMinSeparation.getText().trim());
	}
	
	/*
	 * @returns the currently displayed numNodes value, converted to an int
	 */
	public int getSelectedNumNodes() {
		return Integer.parseInt(ltfNumNodes.getText().trim());
	}
	
	/*
	 * @returns the currently displayed arity value, converted to an int
	 */
	public int getSelectedArity() {
		return Integer.parseInt(ltfArity.getText().trim());
	}
}
