package com.qx.lang.xml.composer;

import java.io.IOException;
import java.io.Writer;

public class XML_StreamWriter {

	private Writer writer;

	public XML_StreamWriter(Writer writer) {
		super();
		this.writer = writer;
	}
	
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @throws IOException
	 */
	public void writeAttribute(String name, String value) throws IOException{
		writer.write(name);
		writer.write('=');
		writer.write('"');
		writer.write(value);
		writer.write('"');
	}
	
}
