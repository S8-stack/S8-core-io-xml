package com.s8.lang.xml.composer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import com.s8.lang.xml.handler.XML_Context;
import com.s8.lang.xml.handler.type.TypeHandler;
import com.s8.lang.xml.handler.type.attributes.getters.AttributeGetter;
import com.s8.lang.xml.handler.type.elements.getters.ElementGetter;

/**
 * 
 * @author pierreconvert
 *
 */
public class ObjectComposableScope extends ComposableScope {


	private boolean hasExpanded;

	private ComposableScope head;

	private ComposableScope tail;	

	private String tag;

	private Object object;

	/**
	 * 
	 * @param context
	 * @param fieldValue
	 */
	public ObjectComposableScope(String tag, Object object){
		super();
		this.tag = tag;
		this.object = object;
		hasExpanded = false;
	}


	public Object getObject() {
		return object;
	}



	/**
	 * 
	 * 
	 * has inner scope
	 * <scope1>
	 * 	<scope2></scope2>
	 *  <scope2></scope2>
	 * </scope1>
	 * 
	 * <scope>string</scope> -> no stacking
	 * <scope><inner-scope> -> stacking
	 */

	@Override
	public boolean insert(XML_Context context, Stack<ComposableScope> stack, XML_StreamWriter writer)
			throws 
			IllegalAccessException, 
			IllegalArgumentException, 
			InvocationTargetException, 
			IOException, 
			Exception {

		if(!hasExpanded) {
			// type handler
			TypeHandler typeHandler = context.getTypeHandlerByClass(object.getClass());

			// start tag
			writer.startTag(tag);	

			// write attributes
			String attributeValue;
			for(AttributeGetter attributeGetter : typeHandler.getAttributeGetters()){
				attributeValue = attributeGetter.get(object);
				if(attributeValue!=null){
					writer.writeAttribute(attributeGetter.getName(), attributeValue);	
				}
			}

			for(ElementGetter elementGetter : typeHandler.getElementGetters()){
				elementGetter.createComposableElement(this);
			}

			if(isEmpty()) {
				writer.append("/>");
				hasExpanded = true;
				return false; // this scope is depleted
			}
			else {
				writer.append(">");
				// stack this scope
				// we are done with this scope
				stack.push(this);

				// so we diretly move to closing the tag
				hasExpanded = true;

				return true; // has stacked this scope
			}
		}
		else { // hasExpanded = true;
			writer.append("</"+tag+">");
			return false;
		}
	}



	/**
	 * 
	 * @param context
	 * @param composer
	 * @param writer
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean compose(XML_Context context, Stack<ComposableScope> stack, XML_StreamWriter writer) 
			throws Exception {
		while(head!=null) {
			boolean isStackedComposedRequired = head.insert(context, stack, writer);
			if(isStackedComposedRequired) {
				// stay on the same node to finish the writing of the trailing tag
				return true;
			}
			else {
				head = head.next;	
			}
		}
		return false;
	}




	public boolean isEmpty() {
		return head==null;
	}

	/**
	 * 
	 * @param subScope
	 */
	public void append(ComposableScope subScope) {
		if(head==null) {
			head = subScope;
			tail = subScope;
		}
		else {
			tail.next = subScope;
			tail = subScope;
		}
	}

}
