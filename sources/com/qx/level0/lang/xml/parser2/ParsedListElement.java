package com.qx.level0.lang.xml.parser2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.qx.level0.lang.xml.handler.list.ListHandler;
import com.qx.level0.lang.xml.parser2.ParsedObjectElement.Callback;


/**
 * 
 * @author pc
 *
 */
public class ParsedListElement implements ParsedElement {
	
	/**
	 * Callback method for the Object element
	 * @author pc
	 *
	 */
	public interface Callback {

		public void set(List<Object> objects) throws XML_ParsingException;

	}


	/**
	 * <b>Callback #1</b>: for returning to the parent scope when done
	 */
	private ParsedElement parent;

	/**
	 * <b>Callback #2</b>: for setting the object when done
	 */
	private Callback callback;
	
	private List<Object> elements;
	
	private ListHandler handler;
	
	
	private String tag;
	
	public ParsedListElement(ParsedElement parent, Callback callback, String tag, ListHandler handler) {
		super();
		this.parent = parent;
		this.callback = callback;
		this.handler = handler;
		this.tag = tag;
		this.elements = new ArrayList<>();
	}
	
	
	@Override
	public void parse(XML_Parser parser, XML_StreamReader reader) throws IOException, XML_ParsingException {
		
	}
	
	
	/**
	 * Add hard object
	 * @param object
	 */
	public void add(Object object) {
		elements.add(object);
	}
}
