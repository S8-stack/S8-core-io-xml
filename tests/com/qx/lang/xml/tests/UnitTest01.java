package com.qx.lang.xml.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.qx.lang.xml.example.Wrapper;
import com.qx.lang.xml.handler.XML_Context;
import com.qx.lang.xml.parser.Parsing;
import com.qx.lang.xml.reader.XML_StreamReader;

public class UnitTest01 {

	public static void main(String[] args) throws Exception {
		Reader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input/test02.xml"))));
		XML_StreamReader reader = new XML_StreamReader(br);
		
		XML_Context context = new XML_Context(Wrapper.class);
		
		Object object = new Parsing(context, reader).parse();
		System.out.println("done: "+object);
	}

}
