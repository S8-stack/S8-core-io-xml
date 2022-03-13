package com.s8.io.xml;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class XSD_Syntax {

	public final static String PREFIX = "xs:";
	
	public final static String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	public final static String OPENING_TAG = "\n<xs:schema"
			+ "\n xmlns:xs=\"http://www.w3.org/2001/XMLSchema\""
			+ "\n targetNamespace=\"http://s8.com\""
			+ "\n xmlns:tns=\"http://s8.com\""
			+ "\n elementFormDefault=\"qualified\">";
	
	public final static String CLOSING_TAG = "\n</xs:schema>";
	
}
