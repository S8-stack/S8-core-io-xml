package com.qx.level0.lang.xml.parser;

import java.io.IOException;

import com.qx.level0.lang.xml.parser.ObjectParsing.State;
import com.qx.level0.lang.xml.parser2.XML_ParsingException;

/**
 * <p>
 * On why not recursive: "However, the textbook is incorrect in the context of
 * Java. Current Java compilers do not implement tail-call optimization,
 * apparently because it would interfere with the Java security implementation,
 * and would alter the behaviour of applications that introspect on the call
 * stack for various purposes."
 * See <a href="https://stackoverflow.com/questions/105834/does-the-jvm-prevent-tail-call-optimizations">here</a>.
 * </p>
 * 
 * @author pc
 *
 */
public interface OldElementParsing {
	
	public abstract void parse(OldXML_Parser parser) throws XML_ParsingException;
	
	/**
	 * 
	 * @author pc
	 *
	 */
	public abstract class State {

		public abstract void parse() throws XML_ParsingException, IOException;

	}


	/**
	 * read doc header
	 */
	public abstract class ReadHeader extends State {

		@Override
		public void parse(XML_StreamReader reader, boolean isVerbose) throws XML_ParsingException, IOException {
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

}
