package com.s8.io.xml.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.s8.core.io.xml.codebase.XML_Codebase;
import com.s8.core.io.xml.handler.type.XML_TypeCompilationException;
import com.s8.io.xml.demos.repo01.Wrapper;

public class XSD_DataPatternGenerator {
	
	public static void main(String[] args) throws XML_TypeCompilationException, IOException {
		XML_Codebase context = XML_Codebase.from(Wrapper.class);
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output/schemas/schema.xsl"))));
		context.xsd_writeSchema(writer);
		writer.close();	
	}
	
}
