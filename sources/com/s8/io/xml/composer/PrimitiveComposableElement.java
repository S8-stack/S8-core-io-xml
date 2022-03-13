package com.s8.io.xml.composer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import com.s8.io.xml.handler.XML_Lexicon;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveComposableElement extends ComposableScope {
	
	
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


	@Override
	public boolean compose(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws Exception {
		return false;
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
		public boolean insert(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
				Exception {
			writer.writeValueElement(tag, Boolean.toString(value));
			
			// no content to be stacked
			return false;
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
		public boolean insert(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Short.toString(value));
			return false;
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
		public boolean insert(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Integer.toString(value));
			return false;
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
		public boolean insert(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Long.toString(value));
			return false;
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
		public boolean insert(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Float.toString(value));
			return false;
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
		public boolean insert(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, Double.toString(value));
			return false;
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
		public boolean insert(XML_Lexicon context, Stack<ComposableScope> stack, XML_StreamWriter writer) throws IOException {
			writer.writeValueElement(tag, value);
			return false;
		}
	}
	
}
