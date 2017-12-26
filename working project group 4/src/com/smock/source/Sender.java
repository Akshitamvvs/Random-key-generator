package com.smock.source;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.smock.util.AvailableNodes;
import com.smock.util.EncryptDecrypt;
import com.smock.util.ErrorManager;
import com.smock.util.ImagePanel;
import com.smock.util.FileChoose;



public class Sender {
	private JFrame sendMessageFrame,frame;
	private JPanel sendPan, hubPan;
	private JLabel msgLabel,image, compNameLabel, ipAddrLabel, routerLabel, scannerLabel,pubKey;
	private JTextField sendMsgText, ipAddrText, fileLoc,pubKey1;
	private JComboBox compNameText;
	private JButton refButton, sendButton, openButton, sendFileButton;
	private JTextArea routerText;
	private JScrollPane routerScroll;
	private JProgressBar signal;
	private String destNetwork, netView[], packMsg, src, rtr1, rtr2,rtr3, dest, msg,	selectedHub, privateKey, publicKey,serverName;
	private int signalMax = 0;
	private ArrayList<String> availNodes;
	private Thread sndThread1, sndThread2, signalT;

	public Sender() 
	{
		Thread sender = new Thread(new Runnable() 
		{
			public void run() {
				AvailableNodes nodes = new AvailableNodes();
				availNodes = nodes.addAvailNode();
				netView = (String[]) availNodes.toArray(new String[availNodes.size()]);
				
				sendMessageFrame = new JFrame("Send Message");
				sendMessageFrame.setLayout(new BorderLayout());
				sendPan = new ImagePanel(new ImageIcon("./Images/networks.jpg").getImage());//new JPanel();
				sendPan.setBackground(Color.blue);
				sendPan.setLayout(null);
				sendPan.setBounds(0, 0, 560, 670);
				
				compNameLabel = new JLabel("Destination");
				compNameLabel.setForeground(Color.BLUE);
				compNameLabel.setBounds(2, 25, 100, 30);
				
				compNameText = new JComboBox(netView);
				compNameText.setBounds(110, 25, 250, 30);
	
				compNameText.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{
						dest = (String) compNameText.getSelectedItem();
						InetAddress addr=null;
						try 
						{
								addr = InetAddress.getByName(dest);
								
						} catch (UnknownHostException e1) 
						{
							
								new ErrorManager("Host Not Found, Check your destination");
						}
						//ipAddrText.setText(addr.getHostAddress().toString());
						serverrequest();
						signalMeter();
						refButton.setVisible(true);
					}
				});
				ipAddrLabel = new JLabel("Public Key");
				ipAddrLabel.setForeground(Color.BLUE);
				ipAddrLabel.setBounds(2, 105, 100, 30);
				ipAddrText = new JTextField();
				ipAddrText.setBounds(110, 105, 250, 30);
				ipAddrText.setEditable(false);
				pubKey=new JLabel("Private Key");
				pubKey.setForeground(Color.BLUE);
				pubKey.setBounds(2, 195, 100,30);
				pubKey1=new JTextField();
				pubKey1.setBounds(110, 195, 250, 30);
				
				msgLabel = new JLabel("Message");
				msgLabel.setForeground(Color.BLUE);
				msgLabel.setBounds(2, 530, 100, 30);
				sendMsgText = new JTextField();
				sendMsgText.setBounds(100, 530, 300, 30);
				sendButton = new JButton("SEND");
				sendButton.setBounds(10, 600, 150, 40);
				refButton = new JButton("SCAN Nodes");
				refButton.setBounds(200, 280, 150, 40);
				refButton.setVisible(false);
				routerLabel = new JLabel("Available Nodes");
				routerLabel.setForeground(Color.BLUE);
				routerLabel.setBounds(2, 250, 200,30);
				routerText = new JTextArea();
				routerText.setEditable(false);
				routerScroll = new JScrollPane(routerText);
				routerScroll.setBounds(2, 290, 595, 200);
				scannerLabel = new JLabel("Scanning Nodes");
				scannerLabel.setForeground(Color.BLUE);
				scannerLabel.setBounds(2, 342, 150, 25);
				scannerLabel.setVisible(false);
				sendPan.add(scannerLabel);
				signal = new JProgressBar();
				signal.setBounds(95, 350, 270, 10);
				signal.setVisible(false);
				sendPan.add(signal);
				sendPan.add(compNameLabel);
				sendPan.add(compNameText);
				sendPan.add(ipAddrLabel);
				sendPan.add(ipAddrText);
				sendPan.add(msgLabel);
				sendPan.add(sendMsgText);
				sendPan.add(sendButton);
				sendPan.add(refButton);
				sendPan.add(routerLabel);
				sendPan.add(routerScroll);
				sendPan.add(pubKey);
				sendPan.add(pubKey1);
				sendPan.setVisible(true);
				sendMsgText.requestFocus();
				image = new JLabel();
				image.setIcon(new ImageIcon("images\\path.jpg"));
				image.setText(" Download");
				//sendMessageFrame.add(image);
				image.setVisible(true);
				image.setBounds(280,0, 190, 170);
                        //-------------
                         /* fileLoc = new JTextField();
				fileLoc.setBounds(5, 350, 250, 25);
				fileLoc.setEditable(false);
				sendPan.add(fileLoc);*/
				/*openButton = new JButton("OPEN");
				openButton.setBounds(265, 350, 100, 25);
				sendPan.add(openButton);
				fileLoc.setVisible(true);
				openButton.setVisible(true);
				sendButton.setVisible(true);
				
				openButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						FileChoose sel = new FileChoose();
						fileLoc.setText(sel.fileSelectionTool());
					}
				});
				sendFileButton = new JButton("SEND FILE");
				sendFileButton.setBounds(5, 380, 100, 25);
				sendFileButton.setVisible(true);
				sendPan.add(sendFileButton);

				// SEND FILE
				sendFileButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						sendFile();
					}
				});*/
				
				// SEnd Button
				sendButton.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent ae) 
					{
						try
						{
							src = InetAddress.getLocalHost().getHostName();
							String optionMsg = "Enter your Public key send message";
							String code = JOptionPane.showInputDialog(null, optionMsg,
									"Input Box", 1);
							String str=ipAddrText.getText().toString();
							if (str.equalsIgnoreCase(code)) {
								//new SignEncode(puK);
								EncryptDecrypt ende=new EncryptDecrypt();
								dest = (String) compNameText.getSelectedItem();
								msg = sendMsgText.getText();
								//String str1=ende.EncryptDecrypt1(msg);
								msgSender();
							} else {
								new ErrorManager("Sorry Your key is not valid");
							}
							
						} catch (UnknownHostException e) 
						{
							e.printStackTrace();
						}
//							msgSender1();
//							sndThread1.start();
							
					}
				});
				
				// Refresh Button
				refButton.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent ae) 
					{
						refButton.setVisible(false);
						Thread scan = new Thread(new Runnable() 
						{
							public void run() 
							{
								scannerLabel.setVisible(true);
								signal.setVisible(true);
								for (int i = 0; i < netView.length; i++) 
								{
									try {
										Socket chkSkt = new Socket(netView[i],1111);
										InetAddress addr = InetAddress.getByName(netView[i]);

										routerText.append("Node 1  : "+ addr.getHostAddress().toString() + "\n");
										rtr1 = netView[i];
										chkSkt.close();
										break;
									} catch (Exception e1) 
									{
										
									}
								}
								for (int i = 0; i < netView.length; i++) 
								{
									try 
									{
										Socket chkSkt = new Socket(netView[i],2222);
										InetAddress addr = InetAddress.getByName(netView[i]);
										routerText.append("Node 2  : "+ addr.getHostAddress().toString() + "\n");
										rtr2 = netView[i];
										chkSkt.close();
										break;
									} catch (Exception e1) 
									{
										
									}
								}
								for (int i = 0; i < netView.length; i++) 
								{
									try 
									{
										Socket chkSkt = new Socket(netView[i],3333);
										InetAddress addr = InetAddress.getByName(netView[i]);
										routerText.append("Node 3  : "+ addr.getHostAddress().toString() + "\n");
										rtr3 = netView[i];
										chkSkt.close();
										break;
									} catch (Exception e1) 
									{
										
									}
								}
								
								signal.setVisible(false);
								scannerLabel.setVisible(false);
								
							
								
							}
						});
						scan.start();
					}
				});

				sendMessageFrame.add(sendPan, BorderLayout.CENTER);
				sendMessageFrame.setSize(605, 685);
				sendMessageFrame.setLocation(100, 100);
				sendMessageFrame.setVisible(true);
				sendMessageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
		sender.start();
	}

	public void msgSender() {
		Thread sndThread = new Thread(new Runnable() {
			public void run() {
				try {
					String sndr = InetAddress.getLocalHost().getHostName();
					Socket sendSkt = new Socket(rtr1, 8811);
					ObjectOutputStream sendObj = new ObjectOutputStream(sendSkt.getOutputStream());
					sendObj.flush();
					packMsg = sndr+"|"+rtr1 + "|" + rtr2 + "|" + dest + "|" + msg + "|"+ pubKey+"|"+rtr3;
					sendObj.writeObject(packMsg);
					sendObj.close();
					sendSkt.close();
					//new sqlconn(sndr,dest);
				} catch (Exception e) {
					e.printStackTrace();
					new ErrorManager(e.toString());
				}
			}
		});
		sndThread.start();
	}

	public void signalMeter() 
	{
		signalT = new Thread(new Runnable() 
		{
			public void run() 
			{
				do 
				{
					signal.setForeground(new Color(112, 248, 86));
					signalMax++;
					signal.setValue(signalMax);
					if (signalMax == 100)
						signalMax = 10;
					try 
					{
						Thread.sleep(1500);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
				} while (true);
			}
		});
		signalT.start();
	}

	

	
	public void serverrequest()
	{
	Thread splash = new Thread(new Runnable() {
			public void run() {
				for (int prgSt = 0; prgSt <= netView.length; prgSt++) {
					//prg.setValue(prgSt);
					try {
						Socket chkSkt = new Socket(netView[prgSt], 4444);
						serverName = netView[prgSt];
						chkSkt.close();
						requestkey();
						recieveKey();
						
						break;
					} catch (Exception e1) {

					}
				}
				
				
			}
		});
		splash.start();
	}
	public void requestkey(){
		Thread req = new Thread(new Runnable() {
			public void run() {
				try {
					
					Socket sendSkt = new Socket(serverName, 6655);
					ObjectOutputStream sendObj = new ObjectOutputStream(sendSkt
							.getOutputStream());
					sendObj.flush();
					String localHostName = InetAddress.getLocalHost().getHostName();
					sendObj.writeObject(localHostName);
					sendObj.close();
					sendSkt.close();
				} catch (Exception e) {
					e.printStackTrace();
					new ErrorManager(e.toString());
				}
			}
		});
		req.start();
	}
	
	public void recieveKey() {
		Thread rcvKey = new Thread(new Runnable() {
			public void run() {
				try {
					do {
						//JOptionPane.showMessageDialog(frame,"key generation");
						ServerSocket rcvSkt = new ServerSocket(5501);
						Socket skt = rcvSkt.accept();
						ObjectInputStream rcvObj = new ObjectInputStream(skt.getInputStream());
						String msg = (String) rcvObj.readObject();

						StringTokenizer strItr = new StringTokenizer(msg, "|",true);
						publicKey = strItr.nextToken();
						pubKey1.setText(publicKey);
						strItr.nextToken();
						privateKey = strItr.nextToken();
						ipAddrText.setText(privateKey);
						skt.close();
						rcvSkt.close();
					} while (true);
				} catch (Exception e) {
					new ErrorManager("Please Try again later receive key");
					e.printStackTrace();
				}
			}
		});
		rcvKey.start();
	}



/*public void sendFile() {
		Thread sndFT = new Thread(new Runnable() {
			public void run() {
				try {
					String sndr = InetAddress.getLocalHost().getHostName();
					String sndFileNme = fileLoc.getText();
					JOptionPane.showMessageDialog(frame,"file is SENDING");
					
					//StringTokenizer st = new StringTokenizer(sndFileNme, "\\",true);
					StringTokenizer st = new StringTokenizer(sndFileNme,"\\",true);
					
					while (st.hasMoreTokens()) {
						sndFileNme = st.nextToken();
					}//ServerSocket
					JOptionPane.showMessageDialog(frame,"file is Sendi1");
					//Socket conClient = new Socket((String) dest, 8761);
					dest = (String) compNameText.getSelectedItem();
					Socket conClient = new Socket((String)dest,9113);
								
					ObjectOutputStream sf = new ObjectOutputStream(conClient.getOutputStream());
					//JOptionPane.showMessageDialog(frame,"file is Sendi1");
					sf.writeObject(sndFileNme);
					sf.flush();
					String fileName = fileLoc.getText();
					File file = new File(fileName);
					FileInputStream fileIn1 = new FileInputStream(file);
					//byte[] buf1 = new byte[1024];
					byte[] buf1= new byte[(int) file.length()];
				//JOptionPane.showMessageDialog(frame,"file is Send22222222");
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					bis.read(buf1, 0, buf1.length);
									while ((fileIn1.read(buf1)) > 0) {
						sf.write(buf1);
					}
					//fileIn1.close();
									
					JOptionPane.showMessageDialog(frame,"End file");
									
					sf.close();
					conClient.close();
				
				} catch (UnknownHostException e) {
					new ErrorManager("You have selected invalid System");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame,e.toString());
					e.printStackTrace();
				}
			}
		});
		sndFT.start();
	}*/
}