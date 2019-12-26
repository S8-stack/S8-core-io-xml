package com.qx.level0.lang.xml.handler.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import com.qx.level0.lang.xml.XML_Context;
import com.qx.level0.lang.xml.parser.ParsedObjectElement;
import com.qx.level0.lang.xml.parser.PrimitiveParsedElement;
import com.qx.level0.lang.xml.parser.PrimitiveParsedElement.Callback;
import com.qx.level0.lang.xml.parser.XML_ParsingException;
import com.qx.level0.lang.xml.parser.XML_StreamReader;

public abstract class PrimitiveElementFieldSetter extends ElementFieldSetter {


	public static boolean isPrimitive(Class<?> type) {
		return type.isPrimitive() || type==String.class;
	}

	public static ElementFieldSetter.Generator create(XML_Context context, Method method, 
			Class<?> fieldType, String tag) throws Exception {
		if(fieldType==boolean.class){
			return new BooleanElementSetterGenerator(tag, method);
		}
		else if(fieldType==short.class){
			return new ShortElementSetterGenerator(tag, method);
		}
		else if(fieldType==int.class){
			return new IntegerElementSetterGenerator(tag, method);
		}
		else if(fieldType==long.class){
			return new LongElementSetterGenerator(tag, method);
		}
		else if(fieldType==float.class){
			return new FloatElementSetterGenerator(tag, method);
		}
		else if(fieldType==double.class){
			return new DoubleElementSetterGenerator(tag, method);
		}
		else if(fieldType==String.class){
			return new StringElementSetterGenerator(tag, method);
		}
		else {
			throw new Exception("Primitive type not supported");
		}
	}


	public static abstract class PrimitiveElementSetterGenerator extends ElementFieldSetter.Generator {

		protected Method method;

		public PrimitiveElementSetterGenerator(String tag, Method method) {
			super(tag);
			this.method = method;
		}


		@Override
		public boolean hasContextualTags() {
			return false;
		}

		@Override
		public Set<String> getContextualTags() {
			return null;
		}

		
		public abstract ElementFieldSetter getStandardSetter();
		

		@Override
		public void getStandardSetters(TypeHandler.Putter putter) throws XML_TypeCompilationException {
			putter.put(getStandardSetter());
		}

		@Override
		public void getContextualSetters(TypeHandler.Putter putter) {
			// no contextual setter
		}

	}

	protected Method method;
	
	public PrimitiveElementFieldSetter(String tag, Method method) {
		super(tag);
		this.method = method;
	}

	@Override
	public PrimitiveParsedElement getParsedElement(ParsedObjectElement parent, XML_StreamReader.Point point) throws XML_ParsingException {
		Object parentObject = parent.getObject();
		return new PrimitiveParsedElement(tag, parent, getCallback(parentObject, point));
	}


	protected abstract PrimitiveParsedElement.Callback getCallback(Object object, XML_StreamReader.Point point);


	public static class BooleanElementSetterGenerator extends PrimitiveElementSetterGenerator {

		public BooleanElementSetterGenerator(String tag, Method method) {
			super(tag, method);
		}

		@Override
		public ElementFieldSetter getStandardSetter() {
			return new PrimitiveElementFieldSetter.BooleanElementSetter(tag, method);
		}

	}

	public static class BooleanElementSetter extends PrimitiveElementFieldSetter {

		public BooleanElementSetter(String tag, Method method) {
			super(tag, method);
		}

		@Override
		protected Callback getCallback(Object object, XML_StreamReader.Point point) {

			return new PrimitiveParsedElement.Callback() {

				@Override
				public void set(String value) throws XML_ParsingException {
					boolean var = Boolean.valueOf(value);
					try {
						method.invoke(object, var);
					} 
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new XML_ParsingException(point, e.getMessage());
					}
				}
			};
		}
	}

	public static class ShortElementSetterGenerator extends PrimitiveElementSetterGenerator {

		public ShortElementSetterGenerator(String tag, Method method) {
			super(tag, method);
		}

		@Override
		public ElementFieldSetter getStandardSetter() {
			return new PrimitiveElementFieldSetter.ShortElementSetter(tag, method);
		}	
	}

	public static class ShortElementSetter extends PrimitiveElementFieldSetter {

		public ShortElementSetter(String tag, Method method) {
			super(tag, method);
		}

		@Override
		protected Callback getCallback(Object object, XML_StreamReader.Point point) {

			return new PrimitiveParsedElement.Callback() {

				@Override
				public void set(String value) throws XML_ParsingException {
					short var = Short.valueOf(value);
					try {
						method.invoke(object, var);
					} 
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new XML_ParsingException(point, e.getMessage());
					}
				}
			};
		}
	}

	public static class IntegerElementSetterGenerator extends PrimitiveElementSetterGenerator {

		public IntegerElementSetterGenerator(String tag, Method method) {
			super(tag, method);
		}

		@Override
		public ElementFieldSetter getStandardSetter() {
			return new PrimitiveElementFieldSetter.IntegerElementSetter(tag, method);
		}
	}

	public static class IntegerElementSetter extends PrimitiveElementFieldSetter {

		public IntegerElementSetter(String tag, Method method) {
			super(tag, method);
		}

		@Override
		protected Callback getCallback(Object object, XML_StreamReader.Point point) {

			return new PrimitiveParsedElement.Callback() {

				@Override
				public void set(String value) throws XML_ParsingException {
					int var = Integer.valueOf(value);
					try {
						method.invoke(object, var);
					} 
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new XML_ParsingException(point, e.getMessage());
					}
				}
			};
		}
	}



	public static class LongElementSetterGenerator extends PrimitiveElementSetterGenerator {

		public LongElementSetterGenerator(String tag, Method method) {
			super(tag, method);
		}

		@Override
		public ElementFieldSetter getStandardSetter() {
			return new PrimitiveElementFieldSetter.LongElementSetter(tag, method);
		}
	}

	public static class LongElementSetter extends PrimitiveElementFieldSetter {

		public LongElementSetter(String tag, Method method) {
			super(tag, method);
		}

		@Override
		protected Callback getCallback(Object object, XML_StreamReader.Point point) {

			return new PrimitiveParsedElement.Callback() {

				@Override
				public void set(String value) throws XML_ParsingException {
					long var = Long.valueOf(value);
					try {
						method.invoke(object, var);
					} 
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new XML_ParsingException(point, e.getMessage());
					}
				}
			};
		}
	}


	public static class FloatElementSetterGenerator extends PrimitiveElementSetterGenerator {

		public FloatElementSetterGenerator(String tag, Method method) {
			super(tag, method);
		}

		@Override
		public ElementFieldSetter getStandardSetter() {
			return new PrimitiveElementFieldSetter.FloatElementSetter(tag, method);
		}
	}

	public static class FloatElementSetter extends PrimitiveElementFieldSetter {

		public FloatElementSetter(String tag, Method method) {
			super(tag, method);
		}

		@Override
		protected Callback getCallback(Object object, XML_StreamReader.Point point) {

			return new PrimitiveParsedElement.Callback() {

				@Override
				public void set(String value) throws XML_ParsingException {
					float var = Float.valueOf(value);
					try {
						method.invoke(object, var);
					} 
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new XML_ParsingException(point, e.getMessage());
					}
				}
			};
		}
	}

	public static class DoubleElementSetterGenerator extends PrimitiveElementSetterGenerator {

		public DoubleElementSetterGenerator(String tag, Method method) {
			super(tag, method);
		}

		@Override
		public ElementFieldSetter getStandardSetter() {
			return new PrimitiveElementFieldSetter.DoubleElementSetter(tag, method);
		}


	}

	public static class DoubleElementSetter extends PrimitiveElementFieldSetter {

		public DoubleElementSetter(String tag, Method method) {
			super(tag, method);
		}

		@Override
		protected Callback getCallback(Object object, XML_StreamReader.Point point) {

			return new PrimitiveParsedElement.Callback() {

				@Override
				public void set(String value) throws XML_ParsingException {
					double var = Double.valueOf(value);
					try {
						method.invoke(object, var);
					} 
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new XML_ParsingException(point, e.getMessage());
					}
				}
			};
		}
	}

	public static class StringElementSetterGenerator extends PrimitiveElementSetterGenerator {

		public StringElementSetterGenerator(String tag, Method method) {
			super(tag, method);
		}

		@Override
		public ElementFieldSetter getStandardSetter() {
			return new PrimitiveElementFieldSetter.StringElementSetter(tag, method);
		}
	}

	public static class StringElementSetter extends PrimitiveElementFieldSetter {

		public StringElementSetter(String tag, Method method) {
			super(tag, method);
		}

		@Override
		protected Callback getCallback(Object object, XML_StreamReader.Point point) {

			return new PrimitiveParsedElement.Callback() {

				@Override
				public void set(String value) throws XML_ParsingException {
					try {
						method.invoke(object, value);
					} 
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new XML_ParsingException(point, e.getMessage());
					}
				}
			};
		}
	}


}
