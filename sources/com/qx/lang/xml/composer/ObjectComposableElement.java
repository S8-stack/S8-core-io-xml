package com.qx.lang.xml.composer;

import com.qx.lang.xml.handler.AttributeGetter;
import com.qx.lang.xml.handler.ElementGetter;
import com.qx.lang.xml.handler.TypeHandler;

public class ObjectComposableElement extends ComposableElement {


	private Object object;

	private TypeHandler typeHandler;

	private Mode mode = Mode.START;

	private enum Mode {
		START, END;
	}

	/**
	 * 
	 * @param context
	 * @param fieldValue
	 */
	public ObjectComposableElement(XML_Composer composer, String fieldName, Object fieldValue){
		super(composer, fieldName);
		this.object = fieldValue;
		this.typeHandler = composer.getTypeHandler(fieldValue.getClass().getName());
	}


	@Override
	public void compose(XML_StreamWriter writer) throws Exception {

		switch(mode){

		case START:

			// start tag
			if(fieldName!=null){
				writer.startTag(fieldName+':'+typeHandler.getSerialName());	
			}
			else{
				writer.startTag(typeHandler.getSerialName());	
			}

			// write attributes
			String attributeValue;
			for(AttributeGetter attributeGetter : typeHandler.getAttributeGetters()){
				attributeValue = attributeGetter.get(object);
				if(attributeValue!=null){
					writer.writeAttribute(attributeGetter.getName(), attributeValue);	
				}
			}

			writer.endTag();

			// write elements
			boolean hasElements = false;
			for(ElementGetter elementGetter : typeHandler.getElementGetters()){
				ComposableElement composable = elementGetter.createComposableElement(composer, object);

				Object value = elementGetter.getValue(object);
				if(value!=null){
					if(!hasElements){
						hasElements = true;
					}
					composer.add(composable);	
				}
			}

			if(hasElements){
				mode = Mode.END;
				composer.add(this);
			}
			else{
				writer.endTag();
			}
			break;

		case END:
			if(fieldName!=null){
				writer.appendClosingTag(fieldName+':'+typeHandler.getSerialName());
			}
			else{
				writer.appendClosingTag(typeHandler.getSerialName());
			}
			break;
		}

	}

}
