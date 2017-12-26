package com.smock.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ErrorManager {
	public ErrorManager(String ErrMsg)
	{
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame,ErrMsg);
	}

}
