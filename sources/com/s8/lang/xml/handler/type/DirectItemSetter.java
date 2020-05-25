package com.s8.lang.xml.handler.type;

import com.s8.lang.xml.parser.Parsed;
import com.s8.lang.xml.parser.ParsedListElement;
import com.s8.lang.xml.parser.ParsedObjectElement;
import com.s8.lang.xml.parser.XML_ParsingException;
import com.s8.lang.xml.parser.XML_StreamReader.Point;

public class DirectItemSetter extends ElementFieldSetter {

	private CollectionElementFieldSetter collectionSetter;
	
	private TypeHandler typehandler;
	
	
	/**
	 * 
	 * @param method
	 * @param tag
	 */
	public DirectItemSetter(CollectionElementFieldSetter arraySetter, TypeHandler typehandler) {
		super(typehandler.getXmlTag());
		this.collectionSetter = arraySetter;
		this.typehandler = typehandler;
	}
	
	
	@Override
	public Parsed getParsedElement(ParsedObjectElement parent, Point point) throws XML_ParsingException {
		
		ParsedObjectElement.Callback callback = new ParsedObjectElement.Callback() {
			
			@Override
			public void set(Object object) throws XML_ParsingException {
				ParsedListElement parsedList = collectionSetter.getParsedElement(parent, point);
				parsedList.add(object);
			}
		};
		
		return new ParsedObjectElement(parent, callback, tag, typehandler, point);
	}


}
