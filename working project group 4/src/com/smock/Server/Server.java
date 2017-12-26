package com.smock.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.smock.util.AvailableNodes;
import com.smock.util.CodeGenerator;
import com.smock.util.ErrorManager;



public class Server {
	private JFrame frame;
	private JLabel senderL, priKeyL, pubKeyL,image,images;
	private JTextField senderT, priKeyT, pubKeyT;
	private Thread ser, serChk;
	
	private ArrayList<String> availNodes;
	private String netView[];
	private boolean itrFlg=true;

	public Server() {

		frame = new JFrame("KeyGenarator");
		frame.setLayout(null);

		image = new JLabel();
		image.setIcon(new ImageIcon("images\\2.jpg"));
		image.setBounds(390,10, 251,234);
		frame.add(image);
		
		senderL = new JLabel("User Identified");
		senderL.setBounds(5, 15, 150,30);
		frame.add(senderL);

		senderT = new JTextField();
		senderT.setEditable(false);
		senderT.setBounds(150, 15, 200, 30);
		frame.add(senderT);

		priKeyL = new JLabel("Public Key");
		priKeyL.setBounds(5, 95, 150, 30);
		frame.add(priKeyL);

		priKeyT = new JTextField();
		priKeyT.setEditable(false);
		priKeyT.setBounds(150, 95, 200, 30);
		frame.add(priKeyT);

		pubKeyL = new JLabel("Private Key");
		pubKeyL.setBounds(5, 175, 150, 30);
		frame.add(pubKeyL);

		pubKeyT = new JTextField();
		pubKeyT.setEditable(false);
		pubKeyT.setBounds(150, 175, 200, 30);
		frame.add(pubKeyT);
		
		images = new JLabel();
		images.setIcon(new ImageIcon("images\\greenbackground.png"));
		images.setBounds(0,0,690,350);
		frame.add(images);
		
		frame.setSize(650, 290);
		frame.setLocation(600, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		//frame.setVisible(false);
		checkServer();
		serChk.start();
		responseKey();
		ser.start();
	}

	public void responseKey() {
		ser = new Thread(new Runnable() {
			public void run() {
				try {
					do {
						AvailableNodes nodes = new AvailableNodes();
						availNodes = nodes.addAvailNode();
						netView = (String[]) availNodes.toArray(new String[availNodes.size()]);
						String whoIs;
						ServerSocket rcvSkt1 = new ServerSocket(6655);
						Socket skt1 = rcvSkt1.accept();
						ObjectInputStream rcvObj1 = new ObjectInputStream(skt1
								.getInputStream());
						whoIs = (String) rcvObj1.readObject();
						senderT.setText(whoIs);
						for (int chkWho = 0; chkWho < netView.length; chkWho++) {
							if (whoIs.equalsIgnoreCase(netView[chkWho])) {
								itrFlg = false;
								break;
							}
						}
						if(itrFlg==false)
						{
							CodeGenerator get = new CodeGenerator();
							String pubKey = get.codeCreate();
							pubKeyT.setText(pubKey);
							String priKey = get.codeCreate();
							priKeyT.setText(priKey);
							sendKey(whoIs, pubKey, priKey);
						}
						else{
							new ErrorManager("Found Intruder : You dont have rights to communicate");
						}
						itrFlg = true;
						skt1.close();
						rcvSkt1.close();
					} while (true);
				} catch (Exception e) {
					new ErrorManager("Server : "+e.toString());
					e.printStackTrace();
				}
			}
		});
		
	}

	public void sendKey(String destination, String puKey, String prKey) {
		try {

			Socket sendSkt1 = new Socket(destination, 5501);
			ObjectOutputStream sendObj1 = new ObjectOutputStream(sendSkt1
					.getOutputStream());
			sendObj1.flush();
			String packMsg = puKey + "|" + prKey ;
			sendObj1.writeObject(packMsg);
			sendObj1.close();
			sendSkt1.close();
		} catch (Exception e) {
			new ErrorManager("Server : "+e.toString());
			e.printStackTrace();
		}
	}
	
	public void checkServer()
	{
		serChk = new Thread(new Runnable() {
		public void run() {
			try {
				do {
					ServerSocket rcvSkt1 = new ServerSocket(4444);
					Socket skt1 = rcvSkt1.accept();
					skt1.close();
					rcvSkt1.close();
				} while (true);
			} catch (Exception e) {
				new ErrorManager("Server : "+e.toString());
				e.printStackTrace();
			}
		}
	});
	}

}
