package com.s8.io.xml.tests;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.s8.io.xml.codebase.XML_Codebase;
import com.s8.io.xml.demos.repo01.Wrapper;

public class UnitTest03 {

	public static void main(String[] args) throws Exception {
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input/test02.xml"))));
		XML_Codebase context = XML_Codebase.from( Wrapper.class );
		
		context.setVerbosity(true);
		Object object = context.deserialize(reader);
		reader.close();
		
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("input/schema.xsl"))));
		context.xsd_writeSchema(writer);
		writer.close();
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output/test03.xml"))));
		context.serialize(object, writer);
		writer.close();
		
		System.out.println("done: "+object);
	}
	
	
	


}
