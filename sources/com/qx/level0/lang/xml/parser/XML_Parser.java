package com.qx.level0.lang.xml.parser;

import java.io.IOException;
import java.util.Stack;

import com.qx.level0.lang.xml.XML_Context;

public class XML_Parser {

	private boolean isVerbose;
	
	private XML_StreamReader reader;

	protected State state;

	private Stack<ParsedElement> stack;

	private RootParsedElement rootBuilder;

	public XML_Parser(XML_Context context, XML_StreamReader reader, boolean isVerbose) {
		super();
		this.isVerbose = isVerbose;
		this.reader = reader;
		rootBuilder = new RootParsedElement(context);
		stack = new Stack<>();
		state = readHeader;
	}

	/**
	 * 
	 * @return
	 * @throws XML_ParsingException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public Object parse() throws XML_ParsingException, IOException {
		while(state!=null){
			try {
				state.parse();
			}
			catch (XML_ParsingException e) {
				e.acquire(reader);
				throw e;
			}
		}
		return rootBuilder.getObject();
	}


	private void push(String tag) throws XML_ParsingException {
		try {

			if(stack.isEmpty()){
				stack.push(rootBuilder.createField(tag));
			}
			else{
				stack.push(stack.peek().createField(tag));

			}
		}
		catch (XML_ParsingException e) {
			throw new XML_ParsingException(e.getMessage()+", for tag: "+tag);
		}
	}


	private void pop(String tag) throws XML_ParsingException {
		if(!tag.equals(stack.peek().getTag())){
			throw new XML_ParsingException("Tag is not matching");
		}
		pop();
	}

	private void pop() throws XML_ParsingException {
		try {
			stack.peek().close();
		}
		catch (XML_ParsingException e) {
			e.acquire(reader);
			e.printStackTrace();
			throw e;
		}
		stack.pop();
		if(stack.isEmpty()){
			state = null;
		}
		else{
			state = readContent;	
		}
	}

	/**
	 * 
	 * @author pc
	 *
	 */
	private abstract class State {

		public abstract void parse() throws XML_ParsingException, IOException;

	}


	/**
	 * read doc header
	 */
	private State readHeader = new State() {

		@Override
		public void parse() throws XML_ParsingException, IOException {
			reader.readNext();
			reader.check("<?xml");
			String value = reader.until(new char[]{'>'}, null, null);
			if(isVerbose) {
				System.out.println("[XML_Parser] read header: "+value);	
			}
			state = readContent;
		}

	};


	private State readContent = new State() {

		@Override
		public void parse() throws XML_ParsingException, IOException {

			// remove new line or leading spaces
			reader.skip(' ', '\n');

			// read value until next tag start
			String value = reader.until(
					/* stop at */ new char[]{'<'},
					/* ignore */ null,
					/* forbid */ null);	

			// set value if any
			if(!isBlank(value) && !stack.isEmpty()){
				stack.peek().setValue(value);
			}
			state = readTag;
		}
	};


	private State readTag = new State() {

		@Override
		public void parse() throws XML_ParsingException, IOException {
			reader.check('<');
			reader.readNext();
			// closing tag
			if(reader.isCurrent('/')){
				reader.readNext();
				state = readClosingTag;
			}
			else if(reader.isCurrent('!')){
				reader.readNext();
				state = readComment;
			}
			else if(reader.isCurrent('?')){
				state = readElementHeader;
			}
			else{
				state = readOpeningTag;
			}
		}
	};


	private State readOpeningTag = new State() {

		@Override
		public void parse() throws XML_ParsingException, IOException {
			String tag = reader.until(
					/* stop at */ new char[]{'>', ' ', '/', '\n'},
					/* ignore */ null,
					/* forbid */ new char[]{',', '=', '"'});

			// prefix is detected -> field name
			if(reader.isCurrent('>')) {
				push(tag);
				reader.readNext();
				state = readContent;
			}
			else if(reader.isCurrent(' ') || reader.isCurrent('\n')){
				push(tag);
				reader.skip(' ', '\n');
				state = readElementAttribute;
			}
			else if(reader.isCurrent('/')){
				reader.readNext();
				reader.check('>');
				pop(tag);
			}
		}
	};


	private State readClosingTag = new State() {

		@Override
		public void parse() throws XML_ParsingException, IOException {
			String tag = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ new char[]{' '},
					/* forbid */ new char[]{',', '=', '"', '/'});
			reader.readNext();
			pop(tag);
		}

	};

	private State readComment = new State() {

		@Override
		public void parse() throws XML_ParsingException, IOException {
			String comment = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ null,
					/* forbid */ null);
			reader.readNext();
			if(isVerbose) {
				System.out.println("XML COmment: "+comment);	
			}
			state = readContent;
		}

	};

	private State readElementHeader = new State() {

		@Override
		public void parse() throws XML_ParsingException, IOException {
			String header = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ null,
					/* forbid */ null);
			if(isVerbose) {
				System.out.println("XML Header: "+header);	
			}
			state = readContent;
		}

	};



	private State readElementAttribute = new State() {

		@Override
		public void parse() throws XML_ParsingException, IOException {

			String name = reader.until(
					/* stop at */ new char[]{'='},
					/* ignore */ new char[]{' ', '\n'},
					/* forbid */ new char[]{',', '<', '>', '"'});

			reader.readNext();
			reader.skip(' ','\t');
			reader.check('"');
			reader.readNext();

			String value = reader.until(
					/* stop at */ new char[]{'"'},
					/* ignore */ new char[]{' ', '\n'},
					/* forbid */ new char[]{',', '<', '>', '='});
			stack.peek().setAttribute(name, value);
			reader.readNext();

			reader.skip(' ', '\n');
			if(reader.isCurrent('>')){
				reader.readNext();
				state = readContent;
			}
			else if(reader.isCurrent('/')){
				reader.readNext();
				reader.check('>');
				reader.readNext();
				pop();
			}
			// else: state = readAttribute
		}
	};


	private static boolean isBlank(String str){
		int n = str.length();
		if(n>0){
			for(int i=0; i<n; i++){
				if(str.charAt(i)!=' '){
					return false;
				}
			}
			return true;	
		}
		else{
			return true;
		}	
	}

}
