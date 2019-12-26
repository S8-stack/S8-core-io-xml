package com.qx.level0.lang.xml.parser;

public class XML_ParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	

	public int line;
	
	public int column;

	public String filename;
	

	public XML_ParsingException(XML_StreamReader.Point point, String message) {
		super(message);
		this.filename = point.filename;
		this.line = point.line;
		this.column = point.column;
	}
	
	public XML_ParsingException(XML_StreamReader reader, String message) {
		super(message);
		this.filename = reader.filename;
		this.line = reader.line;
		this.column = reader.column;
	}
	
	@Override
	public String toString(){
		return getMessage()+" at line:"+line+", column: "+column+", in file: "+filename;
	}

	
}
