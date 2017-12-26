package com.smock.util;

import java.awt.Dimension;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class FileChoose extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFileChooser chooser;
	String selectedFile;

	public String fileSelectionTool() {
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Title");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		//    
		if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) {
			selectedFile = chooser.getSelectedFile().toString();
		} else {
			new ErrorManager("You have not selected any file");
		}
		return selectedFile;
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 200);

	}
}