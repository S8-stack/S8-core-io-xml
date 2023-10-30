package com.s8.core.io.xml;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class XML_Syntax {
	
	public static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	public static final char START_OF_TAG = '<';
	
	public static final char END_OF_TAG = '>';
	
	public static final char TAG_END_MARKER = '/';
	
	public static final char WHITE_SPACE = ' ';

	public static final char ATTRIBUTE_DEFINITION = '=';
	
	
	public static final char MAPPING_SEPARATOR = '_';

	public static final char[] KEY_CHARS = {
			START_OF_TAG,
			END_OF_TAG,
			TAG_END_MARKER,
			WHITE_SPACE,
			ATTRIBUTE_DEFINITION
			};

	
	public static boolean isSchemaAttribute(String name) {
		if(name.startsWith("xml")) {
			return true;
		}
		if(name.startsWith("xs")) {
			return true;
		}
		return false;
	}
}
