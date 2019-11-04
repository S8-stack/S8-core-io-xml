package com.qx.level0.lang.xml.parser;

public class XML_ParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public int line;
	
	public int column;

	public String filename;
	
	
	public XML_ParsingException(String message) {
		super(message);
	}
	
	public XML_ParsingException(int line, int column, String message) {
		super(message);
	}
	
	@Override
	public String toString(){
		return getMessage()+" at line:"+line+", column: "+column+", in file: "+filename;
	}

	public void acquire(XML_StreamReader reader) {
		this.line = reader.line;
		this.column = reader.column;
	}
	
}
