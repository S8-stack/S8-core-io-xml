package com.qx.lang.xml.parser;


import com.qx.lang.xml.context.XML_Context;


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
	public PrimitiveParsedElement(XML_Context context, ParsedElement parent, String parentFieldName)
			throws Exception {
		super(context, parent, parentFieldName);
	}
	
	@Override
	public String getTag() {
		return fieldNameInParent;
	}


	@Override
	public void setAttribute(String name, String value) throws Exception {
		throw new Exception("Cannot set attribute in primitive element");
	}

	@Override
	public void setElement(String fieldName, Object object) throws Exception {
		throw new Exception("Cannot set element in primitive element");
	}

	@Override
	public ParsedElement createField(String tag) throws Exception {
		throw new Exception("Cannot create field based on primitive element");
	}

	
	public static class BooleanElementBuilder extends PrimitiveParsedElement {

		private boolean value;
		
		public BooleanElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName)
				throws Exception {
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws Exception {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) throws Exception {
			this.value = Boolean.valueOf(value);
		}
	}
	
	public static class ShortElementBuilder extends PrimitiveParsedElement {

		private short value;
		
		public ShortElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName)
				throws Exception {
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws Exception {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) throws Exception {
			this.value = Short.valueOf(value);
		}
	}
	
	public static class IntegerElementBuilder extends PrimitiveParsedElement {

		private int value;
		
		public IntegerElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName)
				throws Exception {
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws Exception {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) throws Exception {
			this.value = Integer.valueOf(value);
		}
	}
	
	public static class LongElementBuilder extends PrimitiveParsedElement {

		private long value;
		
		public LongElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName)
				throws Exception {
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws Exception {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) throws Exception {
			this.value = Long.valueOf(value);
		}
	}
	
	public static class FloatElementBuilder extends PrimitiveParsedElement {

		private float value;
		
		public FloatElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName)
				throws Exception {
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws Exception {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) throws Exception {
			this.value = Float.valueOf(value);
		}
	}
	
	public static class DoubleElementBuilder extends PrimitiveParsedElement {

		private double value;
		
		public DoubleElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName)
				throws Exception {
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws Exception {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) throws Exception {
			this.value = Double.valueOf(value);
		}
	}
	
	public static class StringElementBuilder extends PrimitiveParsedElement {

		private String value;
		
		public StringElementBuilder(XML_Context context, ParsedElement parent, String parentFieldName)
				throws Exception {
			super(context, parent, parentFieldName);
		}

		@Override
		public void close() throws Exception {
			parent.setElement(fieldNameInParent, value);
		}

		@Override
		public void setValue(String value) throws Exception {
			this.value = value;
		}
	}
}
