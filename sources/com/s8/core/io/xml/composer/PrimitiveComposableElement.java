package com.s8.core.io.xml.composer;

import java.io.IOException;

import com.s8.core.io.xml.codebase.XML_Codebase;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveComposableElement implements ComposableScope {
	
	
	protected String tag;
	
	/**
	 * 
	 * @param context
	 * @param object
	 */
	public PrimitiveComposableElement(String tag){
		super();
		this.tag = tag;
	}

	
	/**
	 * boolean
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class BooleanComposableElement extends PrimitiveComposableElement {

		private boolean value;
		
		public BooleanComposableElement(String tag, boolean value) {
			super(tag);
			this.value = value;
		}

		@Override
		public void compose(XML_Codebase context, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Boolean.toString(value));
		}
	}
	
	
	/**
	 * short
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class ShortComposableElement extends PrimitiveComposableElement {

		private short value;
		
		public ShortComposableElement(String tag, short value) {
			super(tag);
			this.value = value;
		}

		@Override
		public void compose(XML_Codebase codebase, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Short.toString(value));
		}
	}
	
	
	/**
	 * int
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class IntegerComposableElement extends PrimitiveComposableElement {

		private int value;
		
		public IntegerComposableElement(String tag, int value) {
			super(tag);
			this.value = value;
		}

		@Override
		public void compose(XML_Codebase codebase, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Integer.toString(value));;
		}
	}
	
	
	/**
	 * long 
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class LongComposableElement extends PrimitiveComposableElement {

		private long value;
		
		public LongComposableElement(String tag, long value) {
			super(tag);
			this.value = value;
		}

		@Override
		public void compose(XML_Codebase codebase, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Long.toString(value));
		}
	}
	
	
	
	/**
	 * float
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class FloatComposableElement extends PrimitiveComposableElement {

		private float value;
		
		public FloatComposableElement(String tag, float value) {
			super(tag);
			this.value = value;
		}

		@Override
		public void compose(XML_Codebase codebase, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Float.toString(value));
		}
	}
	
	
	
	/**
	 * double
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class DoubleComposableElement extends PrimitiveComposableElement {

		private double value;
		
		public DoubleComposableElement(String tag, double value) {
			super(tag);
			this.value = value;
		}

		@Override
		public void compose(XML_Codebase codebase, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Double.toString(value));
		}
	}
	
	
	
	/**
	 * string
	 */
	public static class StringComposableElement extends PrimitiveComposableElement {

		private String value;
		
		public StringComposableElement(String tag, String value) {
			super(tag);
			this.value = value;
		}

		@Override
		public void compose(XML_Codebase codebase, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, value);
		}
	}
	
}
