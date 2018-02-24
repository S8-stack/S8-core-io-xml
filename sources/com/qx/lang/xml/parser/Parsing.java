package com.qx.lang.xml.parser;

import java.util.Stack;

import com.qx.lang.xml.XML_Syntax;
import com.qx.lang.xml.handler.XML_Context;
import com.qx.lang.xml.reader.XML_StreamReader;

public class Parsing {

	protected State state;
	
	protected XML_Context context;

	protected XML_StreamReader reader;

	protected Stack<ObjectElementBuilder> stack;

	/**
	 * 
	 * @author pc
	 *
	 */
	private abstract class State {

		public abstract void parse(Parsing parsing) throws Exception;

	}


	/**
	 * current tag
	 */
	private String tag;
	
	private State tagStartFound = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			reader.check('<');
			reader.next();
			// closing tag
			if(reader.isCurrent('/')){
				state = readClosingTag;
			}
			else if(reader.isCurrent('-')){
				state = readComment;
			}
			else{
				state = readOpeningTag;
			}
		}
	};
	
	
	private State readOpeningTag = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			String word;
			
			word = reader.until(
					/* stop at */ new char[]{'>', ' ', ':'},
					/* ignore */ new char[]{' '},
					/* forbid */ new char[]{',', '=', '"', '/'},
					/**/true);
			
			// prefix is detected -> field name
			if(reader.isCurrent(':')){
				if(word.isEmpty()){
					throw new Exception("Empty field name");
				}
				String fieldName = word;
				String elementTypeName = reader.until(
						/* stop at */ new char[]{'>', ' ', '/'},
						/* ignore */ new char[]{' '},
						/* forbid */ new char[]{',', '=', '"', '/', ':'},
						/**/true);
				
				ObjectElementBuilder element = context.create(elementTypeName);
				stack.peek().appendElement(fieldName, element.object);
				stack.push(element);
				state = readAttributeName;
			}
			else if(reader.isCurrent(' ')){
				
			}
		}
	};
	
	
	private State readClosingTag = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private State readComment = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			// TODO Auto-generated method stub
			
		}
		
	};
	

	
	
	private String attributeName;
	
	private State readAttributeName = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			reader.skipWhiteSpace();
			if(reader.isCurrent('>')){
				state = readTag;
			}
			else if(reader.isCurrent('/')){
				reader.next();
				if(!reader.isCurrent('>')){
					throw new Exception("Expecting end of tag");
				}
				stack.pop();
				state = readTag;
			}
			else{
				attributeName = reader.read(new char[]{'='}, new char[]{' '}, new char[]{});
				state = readAttributeValue;	
			}
		}
	};
	
	
	private State readAttributeValue = new State() {

		@Override
		public void parse(Parsing parsing) throws Exception {
			reader.read(new char[]{'"', '>', '/'}, new char[]{' '}, new char[]{'>', '<'});
			if(reader.isCurrent('"')){
				String value = reader.read(new char[]{'"'}, new char[]{' '}, new char[]{'>', '<'});
				stack.peek().setAttribute(attributeName, value);
				state = readAttributeName;
			}
			else if(reader.isCurrent('>')){
				state = readTag;
			}
			else if(reader.isCurrent('/')){
				reader.next();
				if(!reader.isCurrent('>')){
					throw new Exception("Expecting end of tag");
				}
				stack.pop();
				state = readTag;
			}
			else {
				throw new Exception("Missing end of tag");
			}
		}
	};
	
}
