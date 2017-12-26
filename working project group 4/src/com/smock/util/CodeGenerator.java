        package com.smock.util;

import java.util.Random;

public class CodeGenerator {
	public String codeGen;
	public String codeCreate(){
		String codeValue[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","0"};
		Random rand = new Random();
		codeGen = codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		return codeGen;
	}
	public String codeCreate1(String str){
		String code1=str.substring(0,1).toUpperCase();
		String code2=str.substring(1,2).toUpperCase();
		String code3=str.substring(2,3).toUpperCase();
		String code4=str.substring(3,4).toUpperCase();
		String code5=str.substring(4,5).toUpperCase();
		String code6=str.substring(5,6).toUpperCase();                             
		
		String codeValue[] = {code1,code2,code3,code4,code5,code6};
		Random rand = new Random();
		codeGen = codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		codeGen = codeGen+codeValue[rand.nextInt(codeValue.length)];
		return codeGen;
	}

}
