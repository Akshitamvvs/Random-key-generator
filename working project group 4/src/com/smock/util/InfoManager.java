package com.smock.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class InfoManager {
	
public InfoManager(String ErrMsg)
	{
		 JOptionPane.showMessageDialog(new JFrame(), ErrMsg, "Information",JOptionPane.INFORMATION_MESSAGE);
	}
}

