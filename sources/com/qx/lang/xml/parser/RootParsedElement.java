package com.qx.lang.xml.parser;

import com.qx.lang.xml.context.XML_Context;

public class RootParsedElement extends ParsedElement {

	public static final String ROOT_FIELD_NAME = "root";
	
	private Object object;
	
	public RootParsedElement(XML_Context context) {
		super(context, null, null);
	}

	@Override
	public ParsedElement createField(String tag) throws Exception {
		String[] fragments = tag.split(":");
		if(fragments.length!=2){
			throw new Exception("Illegal tag formatting for root element");
		}
		String fieldName = fragments[0];
		if(!fieldName.equals(ROOT_FIELD_NAME)){
			throw new Exception("Illegal field name");
		}
		String typeName = fragments[1];
		
		return new ObjectParsedElement(context, this, "root", typeName);
	}



	@Override
	public void close() throws Exception {
	}


	@Override
	public String getTag() {
		return "<over>";
	}

	@Override
	public void setValue(String value) throws Exception {
		throw new Exception("Illegal value setting on root builder");
	}

	@Override
	public void setAttribute(String name, String value) throws Exception {
		throw new Exception("Illegal attribute setting on root builder");
	}

	@Override
	public void setElement(String fieldName, Object object) throws Exception {
		if(!fieldName.equals(ROOT_FIELD_NAME)){
			throw new Exception("Illegal field name");
		}
		this.object = object;
	}
	
	public Object getObject(){
		return object;
	}

}
