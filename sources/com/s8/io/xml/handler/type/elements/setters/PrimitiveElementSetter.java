package com.s8.io.xml.handler.type.elements.setters;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.s8.io.xml.codebase.XML_CodebaseBuilder;
import com.s8.io.xml.handler.type.TypeBuilder;
import com.s8.io.xml.handler.type.XML_TypeCompilationException;
import com.s8.io.xml.parser.ObjectParsedScope;
import com.s8.io.xml.parser.PrimitiveParsedScope;
import com.s8.io.xml.parser.XML_ParsingException;
import com.s8.io.xml.parser.XML_StreamReader;


/**
 * 
 *
 *
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveElementSetter extends ElementSetter {




	public static abstract class Builder extends ElementSetter.Builder {

		public Builder(Method method) {
			super(method);
		}

		
		public abstract ElementSetter createSetter();

		
		@Override
		public void explore(XML_CodebaseBuilder codebaseBuilder) throws XML_TypeCompilationException {
			// nothing to explore
		}
		

		@Override
		public void link(XML_CodebaseBuilder contextBuilder) throws XML_TypeCompilationException {
			/* nothing to link */
		}
		
		
		@Override
		public boolean hasSubstitutionGroup() {
			return false;
		}
		
		
		@Override
		public Set<String> getSubstitutionGroup() {
			return new HashSet<>();
		}
		

		@Override
		public boolean isColliding(Set<String> substitutionGroup) {
			return false; /* since set is empty, alsways non-colliding */
		}

		
		@Override
		public void build(TypeBuilder declaringTypeBuilder, boolean isColliding) throws XML_TypeCompilationException {
			
			declaringTypeBuilder.putElementSetter(declaredTag, createSetter());
		}

		
	}


	public PrimitiveElementSetter(Method method) {
		super(method);
	}
	
	

	@Override
	public PrimitiveParsedScope createParsedElement(String tag, 
			ObjectParsedScope parent, 
			XML_StreamReader.Point point) throws XML_ParsingException {
		Object parentObject = parent.getObject();
		return new PrimitiveParsedScope(tag, parent, getCallback(parentObject, point));
	}


	protected abstract PrimitiveParsedScope.Callback getCallback(Object object, XML_StreamReader.Point point);


	@Override
	public Method getMethod() {
		return method;
	}


	@Override
	public void xsd_write(String tag, Writer writer) throws IOException {
		writer.write("\n\t\t\t<xs:element name=\""+tag+"\"");
		writer.write(" type=\"xs:string\"");
		writer.write(" minOccurs=\"0\" maxOccurs=\"unbounded\" />");
	}
	

}
