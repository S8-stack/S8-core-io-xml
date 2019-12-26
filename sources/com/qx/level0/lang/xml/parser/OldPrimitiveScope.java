package com.qx.level0.lang.xml.parser;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.parser2.XML_ParsingException;


/**
 * 
 * @author pc
 *
 */
public abstract class OldPrimitiveScope extends OldElementParsing {

	/**
	 * target (parent) object
	 */
	private Object object;
	
	private Method method;
	
	
	/**
	 * 
	 * @param typeHandler
	 * @param object
	 * @throws Exception 
	 */
	public OldPrimitiveScope(){
		super();
	}
	
	
	
	@Override
	public void close() {
			
		
	}
	
	@Override
	public String getTag() {
		return fieldNameInParent;
	}


	@Override
	public void setAttribute(String name, String value) throws XML_ParsingException {
		throw new XML_ParsingException("Cannot set attribute in primitive element");
	}

	@Override
	public void setElement(String fieldName, Object object) throws XML_ParsingException {
		throw new XML_ParsingException("Cannot set element in primitive element");
	}

	@Override
	public OldElementParsing createField(String tag) throws XML_ParsingException {
		throw new XML_ParsingException("Cannot create field based on primitive element");
	}

	
	public static class BooleanElementBuilder extends OldPrimitiveScope {

		private boolean value;
		
		public BooleanElementBuilder(XML_Context context, OldElementParsing parent, String parentFieldName) {
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws XML_ParsingException {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) {
			this.value = Boolean.valueOf(value);
		}
	}
	
	public static class ShortElementBuilder extends OldPrimitiveScope {

		private short value;
		
		public ShortElementBuilder(XML_Context context, OldElementParsing parent, String parentFieldName) {
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws XML_ParsingException {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) {
			this.value = Short.valueOf(value);
		}
	}
	
	public static class IntegerElementBuilder extends OldPrimitiveScope {

		private int value;
		
		public IntegerElementBuilder(XML_Context context, OldElementParsing parent, String parentFieldName){
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws XML_ParsingException {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) {
			this.value = Integer.valueOf(value);
		}
	}
	
	public static class LongElementBuilder extends OldPrimitiveScope {

		private long value;
		
		public LongElementBuilder(XML_Context context, OldElementParsing parent, String parentFieldName){
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws XML_ParsingException {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) {
			this.value = Long.valueOf(value);
		}
	}
	
	public static class FloatElementBuilder extends OldPrimitiveScope {

		private float value;
		
		public FloatElementBuilder(XML_Context context, OldElementParsing parent, String parentFieldName){
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws XML_ParsingException {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) {
			this.value = Float.valueOf(value);
		}
	}
	
	public static class DoubleElementBuilder extends OldPrimitiveScope {

		private double value;
		
		public DoubleElementBuilder(XML_Context context, OldElementParsing parent, String parentFieldName){
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws XML_ParsingException {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) {
			this.value = Double.valueOf(value);
		}
	}
	
	public static class StringElementBuilder extends OldPrimitiveScope {

		private String value;
		
		public StringElementBuilder(XML_Context context, OldElementParsing parent, String parentFieldName){
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws XML_ParsingException {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) {
			this.value = value;
		}
	}
}
