package com.s8.lang.xml.composer;


/**
 * 
 * @author pc
 *
 */
public abstract class PrimitiveComposableElement extends ComposableElement {
	
	
	protected Object fieldValue;
	
	/**
	 * 
	 * @param context
	 * @param object
	 */
	public PrimitiveComposableElement(XML_Composer composer, String fieldName, Object fieldValue){
		super(composer, fieldName);
		this.fieldValue = fieldValue;
	}

	
	@Override
	public abstract void compose(XML_StreamWriter writer) throws Exception;
	
	
	public static class BooleanComposableElement extends PrimitiveComposableElement {

		public BooleanComposableElement(XML_Composer composer, String fieldName, Object object) {
			super(composer, fieldName, object);
		}

		@Override
		public void compose(XML_StreamWriter writer) throws Exception {
			writer.writeValueElement(fieldName, Boolean.toString((boolean) fieldValue));
		}
		
	}
	
	public static class ShortComposableElement extends PrimitiveComposableElement {

		public ShortComposableElement(XML_Composer composer, String fieldName, Object object) {
			super(composer, fieldName, object);
		}

		@Override
		public void compose(XML_StreamWriter writer) throws Exception {
			writer.writeValueElement(fieldName, Short.toString((short) fieldValue));
		}
		
	}
	
	public static class IntegerComposableElement extends PrimitiveComposableElement {

		public IntegerComposableElement(XML_Composer composer, String fieldName, Object object) {
			super(composer, fieldName, object);
		}

		@Override
		public void compose(XML_StreamWriter writer) throws Exception {
			writer.writeValueElement(fieldName, Integer.toString((int) fieldValue));
		}
		
	}
	
	public static class LongComposableElement extends PrimitiveComposableElement {

		public LongComposableElement(XML_Composer composer, String fieldName, Object object) {
			super(composer, fieldName, object);
		}

		@Override
		public void compose(XML_StreamWriter writer) throws Exception {
			writer.writeValueElement(fieldName, Long.toString((long) fieldValue));
		}
		
	}
	
	public static class FloatComposableElement extends PrimitiveComposableElement {

		public FloatComposableElement(XML_Composer composer, String fieldName, Object object) {
			super(composer, fieldName, object);
		}

		@Override
		public void compose(XML_StreamWriter writer) throws Exception {
			writer.writeValueElement(fieldName, Float.toString((float) fieldValue));
		}
		
	}
	
	public static class DoubleComposableElement extends PrimitiveComposableElement {

		public DoubleComposableElement(XML_Composer composer, String fieldName, Object object) {
			super(composer, fieldName, object);
		}

		@Override
		public void compose(XML_StreamWriter writer) throws Exception {
			writer.writeValueElement(fieldName, Double.toString((double) fieldValue));
		}
		
	}
	
	public static class StringComposableElement extends PrimitiveComposableElement {

		public StringComposableElement(XML_Composer composer, String fieldName, Object object) {
			super(composer, fieldName, object);
		}

		@Override
		public void compose(XML_StreamWriter writer) throws Exception {
			writer.writeValueElement(fieldName, (String) fieldValue);
		}
		
	}
	
}
