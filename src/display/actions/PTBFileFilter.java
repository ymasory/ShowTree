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

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*
 * File filter that demands files have .mrg extension and actually be files (not directories).
 */
public class PTBFileFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		if(file.getName().endsWith(".mrg")) {
			return true;
		}
		if (file.isDirectory()) {
			return true;
		}
		return false;
	}
	public String getDescription() {
		return "PTB Format File (.mrg)";
	}
}