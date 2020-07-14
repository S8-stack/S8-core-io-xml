package com.s8.lang.xml;

public class XML_Syntax {
	
	public static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	public static final char START_OF_TAG = '<';
	
	public static final char END_OF_TAG = '>';
	
	public static final char TAG_END_MARKER = '/';
	
	public static final char WHITE_SPACE = ' ';

	public static final char ATTRIBUTE_DEFINITION = '=';
	
	
	public static final String TYPE_KEYWORD = "Xt";

	public static final char[] KEY_CHARS = {
			START_OF_TAG,
			END_OF_TAG,
			TAG_END_MARKER,
			WHITE_SPACE,
			ATTRIBUTE_DEFINITION
			};

}
