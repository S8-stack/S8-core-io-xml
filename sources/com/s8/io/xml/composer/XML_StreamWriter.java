package com.s8.io.xml.composer;

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
		writer.write(' ');
		writer.write(name);
		writer.write('=');
		writer.write('"');
		writer.write(value);
		writer.write('"');
	}
	
	public void startTag(String tag) throws IOException{
		writer.write('<'+tag);
	}
	
	public void endTag() throws IOException{
		writer.write('>');
	}
	
	
	public void appendOpeningTag(String tag) throws IOException{
		writer.write("<"+tag+">");
	}
	
	public void appendClosingTag(String tag) throws IOException{
		writer.write("</"+tag+">");
	}
	
	public void writeValueElement(String tag, String value) throws IOException{
		writer.write('<'+tag+'>');
		writer.write(value);
		writer.write("</"+tag+">");		
	}



	public void append(String string) throws IOException {
		writer.write(string);
	}



	public void close() throws IOException {
		writer.close();
	}
}
