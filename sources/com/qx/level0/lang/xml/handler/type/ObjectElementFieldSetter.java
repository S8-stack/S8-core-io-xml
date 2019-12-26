package com.qx.level0.lang.xml.handler.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.qx.level0.lang.xml.parser2.ParsedElement;
import com.qx.level0.lang.xml.parser2.ParsedObjectElement;
import com.qx.level0.lang.xml.parser2.XML_ParsingException;
import com.qx.level0.lang.xml.parser2.XML_StreamReader;

public class ObjectElementFieldSetter extends ElementFieldSetter {

	public static class Generator extends ElementFieldSetter.Generator {

		private Method method;
		
		private TypeHandler handler;

		private String standardTag;

		private Set<String> contextualTags;

		public Generator(String tag, Method method, TypeHandler handler) {
			super(tag);
			this.method = method;
			this.handler = handler;

			// standard
			standardTag = tag+':'+handler.getXmlName();

			// contextual
			contextualTags = new HashSet<>();
			if(handler.getSubTypes().isEmpty()) { // no override
				contextualTags.add(handler.getXmlName());
			}
		}

		@Override
		public String getStandardTag() {
			return standardTag;
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
		public ElementFieldSetter getStandardSetter() {
			return new ObjectElementFieldSetter(standardTag, method, handler);
		}

		@Override
		public void getContextualSetters(Consumer<ElementFieldSetter> consumer) {
			
			// contextual
			if(!contextualTags.isEmpty() && areContextualTagsEnabled()) { // no override
				for(String tag : contextualTags) {
					consumer.accept(new ObjectElementFieldSetter(tag, method, handler));
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
	public ParsedElement getParsedElement(ParsedObjectElement parent, XML_StreamReader.Point point) throws XML_ParsingException {

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
