package com.smock.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MessageDisplay {
	public MessageDisplay(String str)
	{
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame,str);
	}
}
