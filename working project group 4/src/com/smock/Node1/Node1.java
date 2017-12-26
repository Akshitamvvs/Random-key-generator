package com.smock.Node1;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
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


public class Node1 {
	private JFrame frame;
	private JTextField forwardTxt, destTxt, msgTxt,keyTxt;
	private JLabel forwardLbl, destLbl, msgLbl,keyGen,image;
	private Thread router11;

public Node1()
{
		frame = new JFrame("Router 1");
		frame.setLayout(null);
		keyGen=new JLabel("Hash Key");
		keyGen.setBounds(5, 6, 150,25);
		frame.add(keyGen);
		
		keyTxt = new JTextField();
		keyTxt.setBounds(150, 6, 200, 25);
		keyTxt.setEditable(false);
		frame.add(keyTxt);
		
		forwardLbl = new JLabel("Forwarding To");
		forwardLbl.setBounds(5, 75, 150, 25);
		frame.add(forwardLbl);

		forwardTxt = new JTextField();
		forwardTxt.setBounds(150, 75, 200, 25);
		forwardTxt.setEditable(false);
		frame.add(forwardTxt);

		destLbl = new JLabel("Destination");
		destLbl.setBounds(5, 145, 150, 25);
		frame.add(destLbl);

		destTxt = new JTextField();
		destTxt.setEditable(false);
		destTxt.setBounds(150, 145, 200, 25);
		frame.add(destTxt);

		msgLbl = new JLabel("Message");
		msgLbl.setBounds(5,225, 150, 25);
		
		frame.add(msgLbl);

		msgTxt = new JTextField();
		msgTxt.setEditable(false);
		msgTxt.setBounds(150, 225, 200, 25);
		frame.add(msgTxt);

		image=new JLabel(new ImageIcon("images//globe.jpg"));
		image.setBounds(0, 0, 563, 300);
		frame.add(image);
		
		frame.setSize(560, 320);
		frame.setLocation(600, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		msgRouting11();
		router11.start();
		hub1();
	}

	public void msgRouting11() {
		router11 = new Thread(new Runnable() {
			public void run() {
				try {
					do {
						String sndr,rtr1S, rtr2S, rtr3S,msgS, destS, keyS;
					ServerSocket rcvSkt1 = new ServerSocket(8811);
						Socket skt1 = rcvSkt1.accept();
						ObjectInputStream rcvObj1 = new ObjectInputStream(skt1.getInputStream());
						msgS = (String) rcvObj1.readObject();
						StringTokenizer strItr = new StringTokenizer(msgS,"|",	true);
						sndr=strItr.nextToken();
						
						strItr.nextToken();
						rtr1S=strItr.nextToken();
						
						strItr.nextToken();
						rtr2S = strItr.nextToken();
						
						strItr.nextToken();
						destS = strItr.nextToken();
						
						strItr.nextToken();
						msgS = strItr.nextToken();
						
						
						
						destTxt.setText(destS);
						EncryptDecrypt ED=new EncryptDecrypt();
						String str=ED.EncryptDecrypt1(msgS);
						StringTokenizer st=new StringTokenizer(str,"|",true);
						String msge=st.nextToken();
						st.nextToken();
						String deMessage=st.nextToken();
						msgTxt.setText(msge);
						strItr.nextToken();
						keyS = strItr.nextToken();
						strItr.nextToken();
						rtr3S=strItr.nextToken();
						CodeGenerator cod=new CodeGenerator();
						String codeKey=cod.codeCreate1(keyS);
						Random st12=new Random();
						int s=st12.nextInt(3);
						if(s<2)
						{
							forwardTxt.setText(rtr2S);				
						keyTxt.setText(codeKey);
						String keyGet=keyTxt.getText().toString();
						String optionMsg = "Enter your Hash key send message";
						String code = JOptionPane.showInputDialog(null, optionMsg,"Input Box", 1);
						if(keyGet.equals(code))
						{
							new MessageDisplay("Encrypt Data");
						forwardMsg1(sndr,rtr1S, rtr2S, destS, deMessage, code);
					
					}else{
							new ErrorManager("Invalid Key");
						}
						}else{
														
							forwardTxt.setText(rtr3S);
							keyTxt.setText(codeKey);
							String keyGet=keyTxt.getText().toString();
							String optionMsg = "Enter your Hash key send message";
							String code = JOptionPane.showInputDialog(null, optionMsg,"Input Box", 1);
							if(keyGet.equals(code))
							{
								new MessageDisplay("Encrypt Data");
							forwardMsg12(sndr,rtr1S, rtr3S, destS, deMessage, code);
							
							}else{
								new ErrorManager("Invalid Key");
							}
							
						}
						skt1.close();
						rcvSkt1.close();
					} while (true);
				} catch (Exception e) {
					new ErrorManager("Router 1 : "+e.toString());
					e.printStackTrace();
				}
			}
		});
		
	}

	public void forwardMsg1(String sender,String router1, String router2, String destination, String message, String key) {
		try {

			Socket sendSkt1 = new Socket(router2, 1189);
			ObjectOutputStream sendObj1 = new ObjectOutputStream(sendSkt1
					.getOutputStream());
			sendObj1.flush();
			String packMsg =sender+"|"+ router1 + "|" + router2 + "|" + destination + "|" + message + "|" + key;
			sendObj1.writeObject(packMsg);
			sendObj1.close();
			sendSkt1.close();
		} catch (Exception e) {
			new ErrorManager("Router 1 : "+e.toString());
			e.printStackTrace();
		}
	}
	public void forwardMsg12(String sender,String router1, String router3, String destination, String message, String key) {
		try {

			Socket sendSkt1 = new Socket(router3,1389);
			ObjectOutputStream sendObj1 = new ObjectOutputStream(sendSkt1.getOutputStream());
			sendObj1.flush();
			String packMsg =sender+"|"+ router1 + "|" + router3 + "|" + destination + "|" + message + "|" + key;
			sendObj1.writeObject(packMsg);
			sendObj1.close();
			sendSkt1.close();
		} catch (Exception e) {
			new ErrorManager("Router 1 : "+e.toString());
			e.printStackTrace();
		}
	}
	public void hub1() {
		Thread router1 = new Thread(new Runnable() {
			public void run() {
				try {
					do {
						ServerSocket rcvSkt1 = new ServerSocket(1111);
						Socket skt1 = rcvSkt1.accept();
						rcvSkt1.close();
						skt1.close();
					} while (true);
				} catch (Exception e) {
					new ErrorManager("Router 1 : "+e.toString());
					e.printStackTrace();
				}
			}
		});
		router1.start();
	}

}
