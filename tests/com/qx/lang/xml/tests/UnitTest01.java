package com.qx.lang.xml.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.qx.lang.xml.handler.XML_Context;

public class UnitTest01 {

	public static void main(String[] args) throws FileNotFoundException {
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input/test.xml"))));
		XML_Context context = new XML_Context(TestClass.class);
		Object object = context.deserialize(reader);
		System.out.println("done");
		
	}

}
