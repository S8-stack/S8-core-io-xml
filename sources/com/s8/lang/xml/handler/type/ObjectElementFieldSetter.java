package com.s8.lang.xml.handler.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.s8.lang.xml.parser.Parsed;
import com.s8.lang.xml.parser.ParsedObjectElement;
import com.s8.lang.xml.parser.XML_ParsingException;
import com.s8.lang.xml.parser.XML_StreamReader;

public class ObjectElementFieldSetter extends ElementFieldSetter {

	public static class Generator extends ElementFieldSetter.Generator {

		private Method method;
		
		private TypeHandler handler;

		private Set<String> contextualTags;

		public Generator(String tag, Method method, TypeHandler handler) {
			super(tag);
			this.method = method;
			this.handler = handler;
			contextualTags = new HashSet<>();
			if(!handler.hasSubTypes()) { // no override
				contextualTags.add(tag);
			}
		}
		

		@Override
		public boolean hasContextualTags() {
			return true;
		}

		@Override
		public Set<String> getContextualTags() {
			return contextualTags;
		}
		
		@Override
		public void getStandardSetters(TypeHandler.Putter putter) throws XML_TypeCompilationException {
			putter.put(new ObjectElementFieldSetter(tag+':'+handler.getXmlTag(), method, handler));
			for(TypeHandler handler : handler.getSubTypes()) {
				putter.put(new ObjectElementFieldSetter(tag+':'+handler.getXmlTag(), method, handler));
			}
		}

		@Override
		public void getContextualSetters(TypeHandler.Putter putter) throws XML_TypeCompilationException {
			if(!contextualTags.isEmpty() && areContextualTagsEnabled()) { // no override
				for(String tag : contextualTags) {
					putter.put(new ObjectElementFieldSetter(tag, method, handler));
				}
			}
		}
	}

	
	private Method method;
	
	private TypeHandler handler;

	public ObjectElementFieldSetter(String tag, Method method, TypeHandler handler) {
		super(tag);
		this.method = method;
		this.handler = handler;
	}
	

	public TypeHandler getTypeHandler() {
		return handler;
	}

	/**
	 * 
	 */
	@Override
	public Parsed getParsedElement(ParsedObjectElement parent, XML_StreamReader.Point point) throws XML_ParsingException {

		// retrieve parentObject
		Object parentObject = parent.getObject();

		// setter instance
		ParsedObjectElement.Callback callback = new ParsedObjectElement.Callback() {

			public @Override void set(Object value) throws XML_ParsingException {
				try {
					method.invoke(parentObject, value);
				} 
				catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					throw new XML_ParsingException(point, e.getMessage());
				}
			}
		};

		return new ParsedObjectElement(parent, callback, tag, handler, point);
	}

}
