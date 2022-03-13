package com.s8.io.xml.parser;

public class XML_ParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int line;
	
	public int column;

	public XML_ParsingException(XML_StreamReader.Point point, String message) {
		super(message);
		this.line = point.line;
		this.column = point.column;
	}
	
	
	@Override
	public String toString(){
		return getMessage()+", at line:"+line+", column: "+column;
	}

	
}
