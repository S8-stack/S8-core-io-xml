package com.qx.level0.lang.xml.handler.list;

import com.qx.level0.lang.xml.handler.type.TypeHandler;
import com.qx.level0.lang.xml.parser.ParsedListElement;
import com.qx.level0.lang.xml.parser.ParsedObjectElement;
import com.qx.level0.lang.xml.parser.XML_ParsingException;
import com.qx.level0.lang.xml.parser.XML_StreamReader;


/**
 * 
 * @author pc
 *
 */
public class ElementItemSetter {

	private TypeHandler handler;

	/**
	 * 
	 * @param handler
	 */
	public ElementItemSetter(TypeHandler handler) {
		super();
		this.handler = handler;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getTag() {
		return handler.getXmlTag();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public TypeHandler getTypeHanlder() {
		return handler;
	}
	
	
	/**
	 * 
	 * @param parent
	 * @param point
	 * @return
	 * @throws XML_ParsingException
	 */
	public ParsedObjectElement createParseElement(ParsedListElement parent, XML_StreamReader.Point point) 
			throws XML_ParsingException {
		
		ParsedObjectElement.Callback callback = new ParsedObjectElement.Callback() {
			@Override
			public void set(Object object) throws XML_ParsingException {
				parent.add(object);
			}
		};
		
		return new ParsedObjectElement(parent, callback, getTag(), handler, point);
	}
}
