package com.s8.lang.xml.handler.type;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 
 * @author pc
 *
 */
public class ArrayElementFieldSetter extends CollectionElementFieldSetter {


	public static class Generator extends CollectionElementFieldSetter.Generator {

		private ArrayElementFieldSetter standardSetter;

		public Generator(String tag, Method method, CollectionElementFieldSetter.Entry entry) {
			super(tag, entry);
			standardSetter = new ArrayElementFieldSetter(tag, method, entry);
		}

		@Override
		public CollectionElementFieldSetter getStandardSetter() {
			return standardSetter;
		}
	}


	public ArrayElementFieldSetter(String tag, Method method, Entry entry) {
		super(tag, method, entry);
	}


	@Override
	public Object buildCollection(List<Object> objects) {
		int length = objects.size();
		Object array = Array.newInstance(getListHandler().getType(), length);
		for(int index=0; index<length; index++) {
			Array.set(array, index, objects.get(index));	
		}
		return array;
	}

}
