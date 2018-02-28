package com.qx.lang.xml.composer;

import java.lang.reflect.Array;

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

			for(int index=0; index<length; index++){
				Object item = Array.get(array, index);
				if(item!=null){
					composer.add(new ObjectComposableElement(composer, null, item));
				}
			}

			mode = Mode.END;
			composer.add(this);

		case END:
			// start tag
			writer.appendClosingTag(fieldName);
			break;
		}

	}

}
