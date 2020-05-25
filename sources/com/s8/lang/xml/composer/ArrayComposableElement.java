package com.s8.lang.xml.composer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayComposableElement extends ComposableElement {


	private Object array;

	private Mode mode = Mode.START;

	private enum Mode {
		START, END;
	}

	/**
	 * 
	 * @param context
	 * @param fieldValue
	 */
	public ArrayComposableElement(XML_Composer composer, String fieldName, Object array){
		super(composer, fieldName);
		this.array = array;
	}


	@Override
	public void compose(XML_StreamWriter writer) throws Exception {

		switch(mode){

		case START:

			// start tag
			writer.appendOpeningTag(fieldName);

			int length = Array.getLength(array);

			List<ComposableElement> composables = new ArrayList<>();
			for(int index=0; index<length; index++){
				Object item = Array.get(array, index);
				if(item!=null){
					composables.add(new ObjectComposableElement(composer, null, item));
				}
			}

			mode = Mode.END;
			composables.add(this);
			composer.add(composables);
			break;

		case END:
			// start tag
			writer.appendClosingTag(fieldName);
			break;
		}

	}

}
