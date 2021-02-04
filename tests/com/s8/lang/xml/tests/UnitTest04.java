package com.s8.lang.xml.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.s8.lang.xml.handler.XML_Context;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;
import com.s8.lang.xml.tests.example02.MyTrain;

public class UnitTest04 {


	public static void main(String[] args) throws Exception {
		generateDTD();
		read();
	}

	public static void generateDTD() throws XML_TypeCompilationException, IOException {
		XML_Context context = new XML_Context(new Class<?>[] { MyTrain.class });
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("data/xml/input/train.dtd"))));
		context.xsd_writeSchema(writer);
		writer.close();
	}

	public static void read() throws Exception {

		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/xml/input/test-train.xml"))));
		XML_Context context = new XML_Context(new Class<?>[] { MyTrain.class });

		context.setVerbosity(true);
		Object object = context.deserialize(reader);
		reader.close();

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("data/xml/output/gen-train.xml"))));
		context.serialize(object, writer);
		writer.close();

		System.out.println("done: "+object);
	}


}
