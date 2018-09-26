package com.qx.lang.xml.parser;

import com.qx.lang.xml.XML_Context;

public abstract class ParsedElement {
	

	protected String fieldNameInParent;
	
	protected ParsedElement parent;
	
	/**
	 * 
	 */
	public XML_Context context;
	
	
	public ParsedElement(XML_Context context, ParsedElement parent, String fieldNameInParent) {
		super();
		this.context = context;
		this.parent = parent;
		this.fieldNameInParent = fieldNameInParent;
	}


	public abstract ParsedElement createField(String tag) throws XML_ParsingException;
	
	
	
	public abstract String getTag();
	
	public abstract void close() throws XML_ParsingException;

	public abstract void setValue(String value) throws XML_ParsingException;

	public abstract void setAttribute(String name, String value) throws XML_ParsingException;

	public abstract void setElement(String fieldName, Object object) throws XML_ParsingException ;
	
	
}
