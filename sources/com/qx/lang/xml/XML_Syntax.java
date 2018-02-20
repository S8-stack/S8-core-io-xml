package com.qx.lang.xml;

public class XML_Syntax {

	public static final char[] BASE_IGNORED_CHARS = {'\n', '\t'};
	
	public static final char START_OF_TAG = '<';
	
	public static final char END_OF_TAG = '>';
	
	public static final char TAG_END_MARKER = '/';
	
	public static final char WHITE_SPACE = ' ';

	public static final char ATTRIBUTE_DEFINITION = '=';

	public static final char[] KEY_CHARS = {
			START_OF_TAG,
			END_OF_TAG,
			TAG_END_MARKER,
			WHITE_SPACE,
			ATTRIBUTE_DEFINITION
			};

}
