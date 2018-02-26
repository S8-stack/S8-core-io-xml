package com.qx.lang.xml.parser;


import com.qx.lang.xml.context.XML_Context;


/**
 * 
 * @author pc
 *
 */
public abstract class PrimitiveElementBuilder extends ElementBuilder {

	
	/**
	 * 
	 * @param typeHandler
	 * @param object
	 * @throws Exception 
	 */
	public PrimitiveElementBuilder(XML_Context context, ElementBuilder parent, String parentFieldName)
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
	public ElementBuilder createField(String tag) throws Exception {
		throw new Exception("Cannot create field based on primitive element");
	}

	
	public static class BooleanElementBuilder extends PrimitiveElementBuilder {

		private boolean value;
		
		public BooleanElementBuilder(XML_Context context, ElementBuilder parent, String parentFieldName)
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
	
	public static class ShortElementBuilder extends PrimitiveElementBuilder {

		private short value;
		
		public ShortElementBuilder(XML_Context context, ElementBuilder parent, String parentFieldName)
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
	
	public static class IntegerElementBuilder extends PrimitiveElementBuilder {

		private int value;
		
		public IntegerElementBuilder(XML_Context context, ElementBuilder parent, String parentFieldName)
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
	
	public static class LongElementBuilder extends PrimitiveElementBuilder {

		private long value;
		
		public LongElementBuilder(XML_Context context, ElementBuilder parent, String parentFieldName)
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
	
	public static class FloatElementBuilder extends PrimitiveElementBuilder {

		private float value;
		
		public FloatElementBuilder(XML_Context context, ElementBuilder parent, String parentFieldName)
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
	
	public static class DoubleElementBuilder extends PrimitiveElementBuilder {

		private double value;
		
		public DoubleElementBuilder(XML_Context context, ElementBuilder parent, String parentFieldName)
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
	
	public static class StringElementBuilder extends PrimitiveElementBuilder {

		private String value;
		
		public StringElementBuilder(XML_Context context, ElementBuilder parent, String parentFieldName)
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
