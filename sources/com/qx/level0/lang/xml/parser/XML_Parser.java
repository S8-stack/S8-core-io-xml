package com.qx.level0.lang.xml.parser;

import java.io.IOException;

import com.qx.level0.lang.xml.XML_Context;

/**
 * 
 * @author pc
 *
 */
public class XML_Parser {

	public boolean isVerbose;
	
	private XML_StreamReader reader;

	private ParsedDocument rootScope;
	public Parsed scope;

	public XML_Parser(XML_Context context, XML_StreamReader reader, boolean isVerbose) {
		super();
		this.isVerbose = isVerbose;
		this.reader = reader;
		rootScope = new ParsedDocument(context);
	}

	/**
	 * 
	 * @return
	 * @throws XML_ParsingException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public Object parse() throws XML_ParsingException, IOException {
		scope = rootScope;
		while(scope!=null){
			scope.parse(this, reader);
		}
		return rootScope.getRootObject();
	}


}
