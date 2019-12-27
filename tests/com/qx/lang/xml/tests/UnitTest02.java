package com.qx.lang.xml.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.qx.lang.xml.example.Wrapper;
import com.qx.level0.lang.xml.XML_Context;

public class UnitTest02 {

	public static void main(String[] args) throws Exception {
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input/test02.xml"))));
		XML_Context context = new XML_Context(Wrapper.class);
		context.setVerbosity(true);
		Object object = context.deserialize(reader);
		reader.close();
		
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output/test03.xml"))));
		context.serialize(object, writer);
		writer.close();
		
		System.out.println("done: "+object);
	}

}
