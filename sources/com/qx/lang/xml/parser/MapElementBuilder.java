package com.qx.lang.xml.parser;

import java.util.HashMap;
import java.util.Map;

import com.qx.lang.xml.handler.XML_Context;

public class MapElementBuilder extends ElementBuilder {


	@SuppressWarnings("rawtypes")
	private Map map;

	public MapElementBuilder(XML_Context context, ObjectElementBuilder parent, String fieldName) {
		super(context, parent, fieldName);
		map = new HashMap<>();
	}

	@Override
	public ElementBuilder createField(String tag) throws Exception {

		String[] fragments = tag.split(":");
		String fieldName = fragments[0];
		String typeName = fragments[1];

		return new ObjectElementBuilder(context, this, fieldName, typeName);
	}


	@SuppressWarnings("unchecked")
	// TODO type safety could be done externally...
	@Override
	public void setElement(String fieldName, Object object) throws Exception {
		map.put(fieldName, object);
	}

	@Override
	public void close() throws Exception {
		parent.setElement(fieldNameInParent, map);
	}

	@Override
	public String getTag() {
		return fieldNameInParent;
	}

	@Override
	public void setValue(String value) throws Exception {
		throw new Exception("Cannot set inner value in map");	
	}

	@Override
	public void setAttribute(String name, String value) throws Exception {
		throw new Exception("Cannot set attribute in map");	
	}

}
