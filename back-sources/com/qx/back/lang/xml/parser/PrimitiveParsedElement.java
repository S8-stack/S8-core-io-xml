package com.qx.back.lang.xml.parser;


import com.qx.back.lang.xml.XML_Context;


/**
 * 
 * @author pc
 *
 */
public abstract class PrimitiveParsedElement extends ParsedElement {

	
	/**
	 * 
	 * @param typeHandler
	 * @param object
	 * @throws Exception 
	 */
	public PrimitiveParsedElement(XML_Context context, ParsedElement parent, String parentFieldName){
		super(context, parent, parentFieldName);
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
	public ParsedElement createField(String tag) throws XML_ParsingException {
		throw new XML_ParsingException("Cannot create field based on primitive element");
	}

	
	public static class BooleanElementBuilder extends PrimitiveParsedElement {

		private boolean value;
		
		public BooleanElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName) {
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
	
	public static class ShortElementBuilder extends PrimitiveParsedElement {

		private short value;
		
		public ShortElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName) {
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
	
	public static class IntegerElementBuilder extends PrimitiveParsedElement {

		private int value;
		
		public IntegerElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName){
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
	
	public static class LongElementBuilder extends PrimitiveParsedElement {

		private long value;
		
		public LongElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName){
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
	
	public static class FloatElementBuilder extends PrimitiveParsedElement {

		private float value;
		
		public FloatElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName){
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
	
	public static class DoubleElementBuilder extends PrimitiveParsedElement {

		private double value;
		
		public DoubleElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName){
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
	
	public static class StringElementBuilder extends PrimitiveParsedElement {

		private String value;
		
		public StringElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName){
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
