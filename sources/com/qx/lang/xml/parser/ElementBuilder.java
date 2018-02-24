package com.qx.lang.xml.parser;

import com.qx.lang.xml.handler.XML_Context;

public abstract class ElementBuilder {
	

	protected String fieldNameInParent;
	
	protected ElementBuilder parent;
	
	/**
	 * 
	 */
	public XML_Context context;
	
	
	public ElementBuilder(XML_Context context, ElementBuilder parent, String fieldNameInParent) {
		super();
		this.context = context;
		this.parent = parent;
		this.fieldNameInParent = fieldNameInParent;
	}


	public abstract ElementBuilder createField(String tag) throws Exception;
	
	
	
	public abstract String getTag();
	
	public abstract void close() throws Exception;

	public abstract void setValue(String value) throws Exception;

	public abstract void setAttribute(String name, String value) throws Exception;

	public abstract void setElement(String fieldName, Object object) throws Exception;
	
	
}
