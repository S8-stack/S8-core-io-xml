package com.s8.core.io.xml.parser;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
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
