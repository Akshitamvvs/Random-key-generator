package com.smock.Node3;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;


import com.smock.util.CodeGenerator;
import com.smock.util.EncryptDecrypt;
import com.smock.util.ErrorManager;
import com.smock.util.MessageDisplay;


public class Node3 {
	private JFrame frame;
	private JTextField destTxt, msgTxt,genKey;
	private JLabel destLbl, msgLbl,keyGen,image;
	private Thread router21;

	public Node3() {

		frame = new JFrame("Router 3");
		frame.setLayout(null);
			
		keyGen = new JLabel("Hash Key");
		keyGen.setBounds(5, 35, 150, 25);
		frame.add(keyGen);

		genKey = new JTextField();
		genKey.setEditable(false);
		genKey.setBounds(150, 35, 200, 25);
		frame.add(genKey);

		destLbl = new JLabel("Destination");
		destLbl.setBounds(5, 115, 150, 25);
		frame.add(destLbl);

		destTxt = new JTextField();
		destTxt.setEditable(false);
		destTxt.setBounds(150, 115, 200, 25);
		frame.add(destTxt);

		msgLbl = new JLabel("Message");
		msgLbl.setBounds(5, 205, 150, 25);
		frame.add(msgLbl);

		msgTxt = new JTextField();
		msgTxt.setBounds(150, 205, 200, 25);
		msgTxt.setEditable(false);
		frame.add(msgTxt);

		image=new JLabel(new ImageIcon("images//globe.jpg"));
		image.setBounds(0, 0,563,300);
		frame.add(image);
		
		
		
		frame.setSize(560, 320);
		frame.setLocation(600, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		msgRouting21();
		router21.start();
		hub1();
	}

	public void msgRouting21() {
		router21 = new Thread(new Runnable() {
			public void run() {
				try {
					do {
						String msg, dest, rtr1, rtr2, key,sndr;
						ServerSocket rcvSkt = new ServerSocket(1389);
						Socket skt = rcvSkt.accept();
						ObjectInputStream rcvObj = new ObjectInputStream(skt.getInputStream());
						msg = (String) rcvObj.readObject();
						StringTokenizer strItr = new StringTokenizer(msg, "|",true);
						sndr=strItr.nextToken();
						strItr.nextToken();
						rtr1 = strItr.nextToken();
						strItr.nextToken();
						rtr2 = strItr.nextToken();
						strItr.nextToken();
						dest = strItr.nextToken();
						strItr.nextToken();
						msg = strItr.nextToken();
						strItr.nextToken();
						key=strItr.nextToken();
						EncryptDecrypt ED=new EncryptDecrypt();
						String str=ED.EncryptDecrypt1(msg);
						StringTokenizer st=new StringTokenizer(str,"|",true);
						String msge=st.nextToken();
						st.nextToken();
						String deMessage=st.nextToken();
						destTxt.setText(dest);
						msgTxt.setText(msge);
						CodeGenerator cod=new CodeGenerator();
						String codeKey=cod.codeCreate1(key);
						genKey.setText(codeKey);
						String keyGet=genKey.getText().toString();
						String optionMsg = "Enter your Hash key send message";
						String code = JOptionPane.showInputDialog(null, optionMsg,
								"Input Box", 1);
						if(keyGet.equals(code))
						{
							new MessageDisplay("Encrypt Data");
						forwardMsg1(sndr,rtr1, rtr2, dest, deMessage, code);
						
						}else{
							new ErrorManager("Invalid Key");
						}
								
						skt.close();
						rcvSkt.close();
					} while (true);
				} catch (Exception e) {
					new ErrorManager("Router 2 : " + e.toString());
				}
			}
		});

	}

	public void forwardMsg1(String sender,String router1, String router2, String destination,String message, String prKy) {
		try {

			Socket sendSkt = new Socket(destination, 8888);
			ObjectOutputStream sendObj = new ObjectOutputStream(sendSkt
					.getOutputStream());
			sendObj.flush();
			String packMsg = sender+"|"+router1 + "|" + router2 + "|" + message + "|" + prKy;
			sendObj.writeObject(packMsg);
			sendObj.close();
			sendSkt.close();
		} catch (Exception e) {
			new ErrorManager("Router 3: " + e.toString());
		}
	}
	public void hub1() {
		Thread router1 = new Thread(new Runnable() {
			public void run() {
				try {
					do {
						ServerSocket rcvSkt1 = new ServerSocket(3333);
						Socket skt1 = rcvSkt1.accept();
						rcvSkt1.close();
						skt1.close();
					} while (true);
				} catch (Exception e) {
					new ErrorManager("Router 3: "+e.toString());
					e.printStackTrace();
				}
			}
		});
		router1.start();
	}

}
