package com.qx.lang.xml.parser;

import java.util.HashMap;
import java.util.Map;

import com.qx.lang.xml.XML_Context;

public class MapParsedElement extends ParsedElement {


	@SuppressWarnings("rawtypes")
	private Map map;

	public MapParsedElement(XML_Context context, ObjectParsedElement parent, String fieldName) {
		super(context, parent, fieldName);
		map = new HashMap<>();
	}

	@Override
	public ParsedElement createField(String tag) throws XML_ParsingException {

		String[] fragments = tag.split(":");
		String fieldName = fragments[0];
		String typeName = fragments[1];

		return new ObjectParsedElement(context, this, fieldName, typeName);
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
