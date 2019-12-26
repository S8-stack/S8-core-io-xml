package com.qx.level0.lang.xml.handler.type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.qx.level0.lang.xml.handler.list.ListHandler;
import com.qx.level0.lang.xml.parser2.ParsedListElement;
import com.qx.level0.lang.xml.parser2.ParsedObjectElement;
import com.qx.level0.lang.xml.parser2.XML_ParsingException;
import com.qx.level0.lang.xml.parser2.XML_StreamReader.Point;

public abstract class CollectionElementFieldSetter extends ElementFieldSetter {

	
	public static interface Factory {
		
		public Entry createEntry(TypeHandler handler);
		
	}
	
	public static class Entry {
		
		private int index;
		
		private ListHandler handler;

		public Entry(int index, ListHandler handler) {
			super();
			this.index = index;
			this.handler = handler;
		}
		
		public int getIndex() {
			return index;
		}
		
		public ListHandler getListHandler() {
			return handler;
		}
		
	}

	public abstract static class Generator extends ElementFieldSetter.Generator {
		
		private Entry entry;

		private Set<String> contextualTags;
		
		public Generator(String tag, Entry entry) {
			super(tag);
			this.entry = entry;
			contextualTags = entry.getListHandler().getElementTags();
		}

		@Override
		public String getStandardTag() {
			return tag;
		}

		@Override
		public boolean hasContextualTags() {
			return true;
		}
		
		@Override
		public abstract CollectionElementFieldSetter getStandardSetter();

		@Override
		public Set<String> getContextualTags() {
			return contextualTags;
		}
		
		
		public Entry getEntry() {
			return entry;
		}

		@Override
		public void getContextualSetters(Consumer<ElementFieldSetter> consumer) {
			if(areContextualTagsEnabled()) {
				ListHandler listHandler = entry.getListHandler();
				CollectionElementFieldSetter standardSetter = getStandardSetter();
				listHandler.traverseTypeHandler(typeHandler -> {
					consumer.accept(new DirectItemSetter(standardSetter, typeHandler));
				});
			}
		}
		
	}

	private int index;

	private Method method;
	
	private ListHandler handler;

	public CollectionElementFieldSetter(String tag, Method method, Entry entry) {
		super(tag);
		this.method = method;
		this.index = entry.getIndex();
		this.handler = entry.getListHandler();
	}
	
	
	public ListHandler getListHandler() {
		return handler;
	}
	
	public abstract Object buildCollection(List<Object> objects);


	@Override
	public ParsedListElement getParsedElement(ParsedObjectElement parent, Point point) throws XML_ParsingException {
		
		ParsedListElement parsedList = parent.lists[index];
		if(parsedList==null) {
			Object parentObject = parent.getObject();
			ParsedListElement.Callback callback = new ParsedListElement.Callback() {

				@Override
				public void set(List<Object> objects) throws XML_ParsingException {
					try {
						method.invoke(parentObject, buildCollection(objects));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new XML_ParsingException(point, e.getMessage());
					}
				}
			};
			parsedList = new ParsedListElement(parent, callback, tag, handler);
			parent.lists[index] = parsedList;
			return parsedList;
		}
		else {
			return parsedList;
		}
	}

}