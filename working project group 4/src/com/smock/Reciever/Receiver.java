package com.smock.Reciever;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.sql.*;


import com.smock.util.AvailableNodes;
import com.smock.util.CodeGenerator;
import com.smock.util.EncryptDecrypt;
import com.smock.util.ErrorManager;
import com.smock.util.ImagePanel;
import com.smock.util.MessageDisplay;
import com.smock.util.InfoManager;
//import com.smock.util.PathEvaluation;


public class Receiver {
	private JFrame frame;
	private JPanel recievePan;
	private JLabel message,image, sigLabel,KeyRe,sourDetails,hub1Details,rcvLabel,hub2Details;
	private JTextArea msgTxt;
	private JScrollPane scrollMsg;
	private JProgressBar signal;
	private JTextField keyRe;
	private JButton path;
	private JButton attackButton;
	private String sndr,msg, rtr1, rtr2, whoIs, key;

	private Thread rcvThread, signalT;
	private ArrayList<String> availNodes;
	private String netView[];
	private boolean itrFlg;
	private int signalMax = 100;

	public Receiver() {
		frame = new JFrame("Reciever");
		recievePan = new JPanel();
		recievePan.setLayout(null);
		//recievePan.setBackground(Color.ORANGE);
		
		image=new JLabel();
		image.setIcon(new ImageIcon("images\\fileview1.jpg"));
		image.setBounds(415, 60, 220,229);
		frame.add(image);

		message = new JLabel("MESSAGE RECIEVED");
		message.setBounds(5, 20, 150, 25);
		recievePan.add(message);
		KeyRe=new JLabel("Key");
		KeyRe.setBounds(165,20, 80,25);
		recievePan.add(KeyRe);
		keyRe=new JTextField();
		keyRe.setBounds(200,17, 200, 30);
		keyRe.setEditable(false);
		recievePan.add(keyRe);

		msgTxt = new JTextArea();
		msgTxt.setEditable(false);
		scrollMsg = new JScrollPane(msgTxt);
		scrollMsg.setBounds(5,75, 410, 200);
		recievePan.add(scrollMsg);

		sigLabel = new JLabel("Signal Strength");
		sigLabel.setBounds(5, 292, 150, 25);
		frame.add(sigLabel);
		signal = new JProgressBar();
		signal.setBounds(100, 300, 280, 10);
		frame.add(signal);
		
		attackButton = new JButton("<html><I>Path Display</I></html>");
		attackButton.setBounds(5, 265, 150, 25);
		attackButton.setVisible(false);
		recievePan.add(attackButton);
		attackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					attackWin();

				} catch (UnknownHostException e) {
					new ErrorManager(e.toString());
				}
			}
		});
		path=new JButton("Path Display");
		path.setBounds(5, 335, 150,40);
		path.setVisible(true);
		frame.add(path);
		path.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				
				try {
					attackWin();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});

		frame.add(recievePan);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(645, 420);
		frame.setLocation(600, 420);
		frame.setVisible(true);
		AvailableNodes nodes = new AvailableNodes();
		availNodes = nodes.addAvailNode();
		netView = (String[]) availNodes.toArray(new String[availNodes.size()]);
		signalMeter();
		msgReciever();
		rcvThread.start();
	}

	public void msgReciever() {
		rcvThread = new Thread(new Runnable() {
			public void run() {
				try {
					do {
						//JOptionPane.showMessageDialog(frame,"file is receiving");
						String rec = InetAddress.getLocalHost().getHostName();
						String sndr,msg, rtr1, rtr2, whoIs, key;
						ServerSocket rcvSkt = new ServerSocket(8888);
						Socket skt = rcvSkt.accept();
						whoIs = skt.getInetAddress().getHostName();
						ObjectInputStream rcvObj = new ObjectInputStream(skt
								.getInputStream());
						msg = (String) rcvObj.readObject();

						StringTokenizer strItr = new StringTokenizer(msg, "|",
								true);
						sndr=strItr.nextToken();
						strItr.nextToken();
						
						rtr1 = strItr.nextToken();
						strItr.nextToken();
						rtr2 = strItr.nextToken();
						strItr.nextToken();
						msg = strItr.nextToken();
						strItr.nextToken();
						//new sqlconn(msg,null);
						key = strItr.nextToken();
						EncryptDecrypt ED=new EncryptDecrypt();
						String str1=ED.EncryptDecrypt1(msg);
						StringTokenizer st=new StringTokenizer(str1,"|",true);
						String msge=st.nextToken();
						st.nextToken();
						String deMessage=st.nextToken();
						CodeGenerator cod=new CodeGenerator();
						String codeKey=cod.codeCreate1(key);
						keyRe.setText(codeKey);
						String str=keyRe.getText().toString();
						String optionMsg = "Enter Public key to recieve your message ";
						String code = JOptionPane.showInputDialog(null, optionMsg,
								"Input Box", 1);
						
						if (str.equalsIgnoreCase(code)) {
							String disMessage="Decrypt message";
							new MessageDisplay(disMessage);
							msgTxt.append(sndr+" >> "+deMessage + "\n");
						
							//new sql(sndr);
						} else {
							new ErrorManager("Sorry You lost your message");
						}
						skt.close();
						rcvSkt.close();
					} while (true);
				} catch (Exception e) {
					new ErrorManager("Please Try again later inside receiver");
					e.printStackTrace();
				}
			}
		});

	}
	
	public void attackWin() throws UnknownHostException {
		JFrame attackFrame = new JFrame("Path Details");
		ImagePanel panel = new ImagePanel(new ImageIcon("./images/Path.jpg")
				.getImage());
		panel.setLayout(null);

		JLabel sourDetails = new JLabel(InetAddress.getLocalHost().getHostName());
		sourDetails.setForeground(Color.WHITE);
		sourDetails.setBounds(50, 40, 300, 25);
		//sourDetails.setText(whoIs);

		JLabel hub1Details = new JLabel();
		hub1Details.setForeground(Color.BLUE);
		InetAddress addr1 = InetAddress.getByName(rtr1);
		hub1Details.setBounds(225, 40, 300, 25);
		//hub1Details.setText(addr1.getAddress().toString());
		hub1Details.setText(addr1.getLocalHost().toString());

		//InetAddress addr2 = InetAddress.getByName(InetAddress.getLocalHost().getHostName());
		JLabel hub2Details = new JLabel();
		hub2Details.setForeground(Color.BLUE);
		InetAddress addr2 = InetAddress.getByName(rtr2);
		hub2Details.setBounds(370, 40, 300, 25);
		//hub2Details.setText(addr2.getHostAddress().toString());
		hub2Details.setText(addr1.getLocalHost().toString());

		JLabel rcvLabel = new JLabel(InetAddress.getLocalHost().getHostName());
		rcvLabel.setForeground(Color.WHITE);
		rcvLabel.setBounds(550, 40, 300, 25);
		panel.add(sourDetails);
		panel.add(hub1Details);
		panel.add(hub2Details);
		panel.add(rcvLabel);

		attackFrame.add(panel);
		attackFrame.setSize(700, 200);
		attackFrame.setVisible(true);
	}
	/*public void sql()
	{
		try
		{
			System.out.print("hai");
			String str= msgTxt.getText();
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			
			Connection con=DriverManager.getConnection("jdbc:odbc:som");
			System.out.print("hai1");
			Statement st=con.createStatement();
			System.out.print("hai");
			String sql = "INSERT INTO rec(msg) VALUES('" + str + "')";
			System.out.print("hai122");
			int rs=st.executeUpdate(sql);
			System.out.print("ok");
			st.close();
			con.close();
					
		}
		catch(Exception se)
		{
			
		}
	}*/



	public void signalMeter() {
		signalT = new Thread(new Runnable() {
			public void run() {
				do {
					Random colR = new Random();
					int R = colR.nextInt(255);
					Random colG = new Random();
					int G = colG.nextInt(255);
					Random colB = new Random();
					int B = colB.nextInt(255);
					signal.setForeground(new Color(R,G,B));
					Random randSig = new Random();
					signalMax = randSig.nextInt(100);
					signal.setValue(signalMax);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (true);
			}
		});
		signalT.start();
	}
	
	/*public void recieveFile() {
		Thread filRcvTrd = new Thread(new Runnable() {
			public void run() {
				try {
					do {
						ServerSocket recieve = new ServerSocket(9000);
						Socket rcvSck = recieve.accept();
						ObjectInputStream rcvFile = new ObjectInputStream(
								rcvSck.getInputStream());
						String fileNme = (String) rcvFile.readObject();
						JOptionPane.showMessageDialog(frame,"file is receiving");
						File file = new File(".\\Recieved Files\\" + fileNme);
						FileOutputStream fileOut = new FileOutputStream(file);
						int buf;
						do {
							buf = rcvFile.read();
							fileOut.write(buf);
						} while (buf != -1);
						fileOut.close();
						rcvSck.close();
						recieve.close();
						new InfoManager("You have recieved a file");
						try {
							Socket sendSkt = new Socket(whoIs, 2121);
							ObjectOutputStream sendObj = new ObjectOutputStream(
									sendSkt.getOutputStream());
							sendObj.flush();
							msg = "Acknowledgment from "
									+ InetAddress.getLocalHost().getHostName()
									+ "\n I recieved your file";
							sendObj.writeObject(msg);
							sendObj.close();
							sendSkt.close();
						} catch (Exception e) {
							e.printStackTrace();
							new ErrorManager(e.toString());
						}
					} while (true);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		filRcvTrd.start();
	}*/


}
