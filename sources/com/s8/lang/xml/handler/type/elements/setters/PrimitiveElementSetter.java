package com.s8.lang.xml.handler.type.elements.setters;

import java.lang.reflect.Method;

import com.s8.lang.xml.handler.XML_Context;
import com.s8.lang.xml.handler.XML_ContextBuilder;
import com.s8.lang.xml.handler.type.TypeBuilder;
import com.s8.lang.xml.handler.type.XML_TypeCompilationException;
import com.s8.lang.xml.parser.ObjectParsedScope;
import com.s8.lang.xml.parser.PrimitiveParsedScope;
import com.s8.lang.xml.parser.XML_ParsingException;
import com.s8.lang.xml.parser.XML_StreamReader;


/**
 * 
 * @author pierreconvert
 *
 */
public abstract class PrimitiveElementSetter extends ElementSetter {




	public static abstract class Builder extends ElementSetter.Builder {

		protected Method method;

		public Builder(String tag, Method method) {
			super(tag);
			this.method = method;
		}

		public abstract ElementSetter getStandardSetter();

		@Override
		public void explore(XML_ContextBuilder contextBuilder) throws XML_TypeCompilationException {
			// nothing to explore
		}

		@Override
		public boolean build0(XML_ContextBuilder contextBuilder, TypeBuilder typeBuilder)
				throws XML_TypeCompilationException {
			typeBuilder.setElementSetter(getStandardSetter());
			return false;
		}

		@Override
		public boolean build1(XML_ContextBuilder contextBuilder, TypeBuilder typeBuilder) 
				throws XML_TypeCompilationException {
			return false;
		}
	}

	protected Method method;

	public PrimitiveElementSetter(String tag, Method method) {
		super(tag);
		this.method = method;
	}

	@Override
	public PrimitiveParsedScope createParsedElement(XML_Context context, 
			ObjectParsedScope parent, XML_StreamReader.Point point) throws XML_ParsingException {
		Object parentObject = parent.getObject();
		return new PrimitiveParsedScope(tag, parent, getCallback(parentObject, point));
	}


	protected abstract PrimitiveParsedScope.Callback getCallback(Object object, XML_StreamReader.Point point);





}
