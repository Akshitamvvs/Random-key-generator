package com.smock.Server;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
public class ServerInit {
	private JWindow window;
	private JPanel splashPan;
	private JLabel imgLabel,image;
	private JProgressBar prg;
	private Thread splash;


	private int prgMin = 0, prgMax = 100;

	public ServerInit() {
		/*image=new JLabel();
		image.setIcon(new ImageIcon("images\\Welcome-Animated.gif"));
		image.setBounds(0, 0, 50, 50);
		image.setVisible(true);
		splashPan.add(image);*/

		window = new JWindow();
		splashPanel();
		window.add(splashPan);
		window.setLocation(270, 200);
		window.setSize(530, 325);
		window.setVisible(true);

	}

	public void splashPanel() {
		splashPan = new JPanel();
		//splashPan.setBackground(Color.blue);
		splashPan.setLayout(new BorderLayout());
		imgLabel = new JLabel(new ImageIcon("images\\Welcome-Animated.gif"));
		imgLabel.setBounds(prgMax, prgMax, prgMax, prgMax);
		splashPan.add(imgLabel, BorderLayout.CENTER);

		prg = new JProgressBar();
		splashPan.add(prg, BorderLayout.SOUTH);
		splash = new Thread(new Runnable() {
			public void run() {
				for (int prgSt = prgMin; prgSt <= prgMax; prgSt++) {
					prg.setValue(prgSt);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				splashPan.setVisible(false);
				window.setVisible(true);
				window.dispose();
				new Server();
			}
		});
		splash.start();
	}

}
