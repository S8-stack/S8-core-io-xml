package com.qx.level0.lang.xml.parser;

import java.util.HashMap;
import java.util.Map;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.parser2.XML_ParsingException;

public class OldMapParsedElement extends OldElementParsing {


	@SuppressWarnings("rawtypes")
	private Map map;

	public OldMapParsedElement(XML_Context context, ObjectParsing parent, String fieldName) {
		super(context, parent, fieldName);
		map = new HashMap<>();
	}

	@Override
	public OldElementParsing createField(String tag) throws XML_ParsingException {

		String[] fragments = tag.split(":");
		String fieldName = fragments[0];
		String typeName = fragments[1];

		return new ObjectParsing(context, this, fieldName, typeName);
	}


	@SuppressWarnings("unchecked")
	// TODO type safety could be done externally...
	@Override
	public void setElement(String fieldName, Object object) {
		map.put(fieldName, object);
	}

	@Override
	public void close() throws XML_ParsingException {
		parent.setElement(fieldNameInParent, map);
	}

	@Override
	public String getTag() {
		return fieldNameInParent;
	}

	@Override
	public void setValue(String value) throws XML_ParsingException {
		throw new XML_ParsingException("Cannot set inner value in map");	
	}

	@Override
	public void setAttribute(String name, String value) throws XML_ParsingException {
		throw new XML_ParsingException("Cannot set attribute in map");	
	}

}
