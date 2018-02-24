package com.qx.lang.xml.parser;

import com.qx.lang.xml.handler.XML_Context;

public abstract class ElementBuilder {
	

	protected String fieldName;
	
	protected ElementBuilder parent;
	
	
	/**
	 * 
	 */
	public XML_Context context;
	
	

	
	public ElementBuilder(XML_Context context, ElementBuilder parent, String fieldName) {
		super();
		this.context = context;
		this.parent = parent;
		this.fieldName = fieldName;
	}


	public abstract ElementBuilder createField(String tag) throws Exception;
	
	
	public abstract void appendElement(String fieldName, Object object) throws Exception;
	
	
	public abstract void close() throws Exception;
	
	
}
