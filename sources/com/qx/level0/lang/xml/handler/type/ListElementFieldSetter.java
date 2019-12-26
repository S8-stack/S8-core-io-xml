package com.qx.level0.lang.xml.handler.type;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 
 * @author pc
 *
 */
public class ListElementFieldSetter extends CollectionElementFieldSetter {

	public static class Generator extends CollectionElementFieldSetter.Generator {

		private ListElementFieldSetter standardSetter;

		public Generator(String tag, Method method, Entry entry) {
			super(tag, entry);
			standardSetter = new ListElementFieldSetter(tag, method, entry);
		}

		@Override
		public CollectionElementFieldSetter getStandardSetter() {
			return standardSetter;
		}
	}


	public ListElementFieldSetter(String tag, Method method, Entry entry) {
		super(tag, method, entry);
	}


	@Override
	public Object buildCollection(List<Object> objects) {
		return objects;
	}
}
