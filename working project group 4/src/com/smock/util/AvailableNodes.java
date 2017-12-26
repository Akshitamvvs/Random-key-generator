package com.smock.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class AvailableNodes {
	public String command;
	public Process p2;
	public BufferedReader input;
	public String result;
	public ArrayList<String> listNode;
	public StringBuffer strbuf;
	public int cnt = 1;
	public int cntLen = 0;

	public ArrayList<String> addAvailNode() {
		command = "net view";
		try {
				p2 = Runtime.getRuntime().exec(command);
				input = new BufferedReader(new InputStreamReader(p2.getInputStream()));
				listNode = new ArrayList<String>();
				strbuf = new StringBuffer();
				while ((result = input.readLine()) != null) {
					cnt++;
					if (cnt > 4) {
						strbuf.append(result);
						if (strbuf.charAt(0) == '\\') {
							for (int i = 0; i < strbuf.capacity(); i++) {
								if (strbuf.charAt(i) == (' ')) {
									cntLen = i;
									break;
								}
							}
							String temp = strbuf.substring(2, cntLen);
							listNode.add(temp);
							strbuf.delete(0, strbuf.length());
						}
					}
				}

		} catch (Exception e) {
			System.out.println("Error " + e);
		}
		return listNode;
	}
	public static void main(String args[])
	{
		AvailableNodes s=new AvailableNodes();
		ArrayList<String> array=new ArrayList<String>();
		array=s.addAvailNode();
		System.out.println(array);
		
	}

}
