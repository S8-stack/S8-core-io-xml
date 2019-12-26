package com.qx.level0.lang.xml.parser;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.parser2.XML_ParsingException;

public class OldRootParsedElement extends OldElementParsing {

	public static final String ROOT_FIELD_NAME = "root";
	
	private Object object;
	
	public OldRootParsedElement(XML_Context context) {
		super(context, null, null);
	}

	@Override
	public OldElementParsing createField(String tag) throws XML_ParsingException {
		String[] fragments = tag.split(":");
		if(fragments.length!=2){
			throw new XML_ParsingException("Illegal tag formatting for root element");
		}
		String fieldName = fragments[0];
		if(!fieldName.equals(ROOT_FIELD_NAME)){
			throw new XML_ParsingException("Illegal field name");
		}
		String typeName = fragments[1];
		
		return new ObjectParsing(context, this, "root", typeName);
	}



	@Override
	public void close() {
	}


	@Override
	public String getTag() {
		return "<over>";
	}

	@Override
	public void setValue(String value) throws XML_ParsingException {
		throw new XML_ParsingException("Illegal value setting on root builder");
	}

	@Override
	public void setAttribute(String name, String value) throws XML_ParsingException {
		throw new XML_ParsingException("Illegal attribute setting on root builder");
	}

	@Override
	public void setElement(String fieldName, Object object) throws XML_ParsingException {
		if(!fieldName.equals(ROOT_FIELD_NAME)){
			throw new XML_ParsingException("Illegal field name");
		}
		this.object = object;
	}
	
	public Object getObject(){
		return object;
	}

}
