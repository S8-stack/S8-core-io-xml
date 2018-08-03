package com.qx.lang.xml.parser;

public class XML_ParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public int line;
	
	public int column;
	
	public XML_ParsingException(String message, int line, int column) {
		super(message);
		this.line = line;
		this.column = column;
	}
	
	@Override
	public String toString(){
		return getMessage()+" at line:"+line+", column: "+column;
	}

}
