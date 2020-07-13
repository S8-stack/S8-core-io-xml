package com.s8.lang.xml.parser;


import java.io.IOException;

import com.s8.lang.xml.XML_Syntax;
import com.s8.lang.xml.handler.XML_Context;
import com.s8.lang.xml.handler.type.TypeHandler;


/**
 * 
 * @author pc
 *
 */
public class ObjectParsedScope implements ParsedScope {

	/**
	 * Callback method for the Object element
	 * @author pc
	 *
	 */
	public interface Callback {

		public void set(Object object) throws XML_ParsingException;

	}


	
	/**
	 * the parsing context
	 */
	private XML_Context context;
	
	
	/**
	 * <b>Callback #1</b>: for returning to the parent scope when done
	 */
	private ParsedScope parent;

	/**
	 * <b>Callback #2</b>: for setting the object when done
	 */
	private Callback callback;


	private String tag;


	/**
	 * 
	 */
	private TypeHandler typeHandler;


	/**
	 * the object currently built
	 */
	private Object object;



	/**
	 * current parsing state
	 */
	private State state;
	
	
	/**
	 * element creation point
	 */
	private XML_StreamReader.Point point;


	private boolean isParsing;

	private boolean isClosed;

	/**
	 * CONSTRUCTOR #1: in this case, type is already known
	 * @param tag
	 * @param callback
	 * @param handler
	 * @throws XML_ParsingException
	 */
	public ObjectParsedScope(XML_Context context,
			ParsedScope parent, 
			Callback callback, 
			String tag, 
			TypeHandler handler,
			XML_StreamReader.Point point) throws XML_ParsingException {
		super();
		this.context = context;
		this.parent = parent;
		this.callback = callback;
		
		this.tag = tag;
		this.point = point;
		
		// initialize
		state = new ReadAttributes();

		setType(handler);
	}
	
	
	/**
	 * CONSTRUCTOR #2: in this case, type is NOT known (and will be discovered later on)
	 * @param tag
	 * @param callback
	 * @param handler
	 * @throws XML_ParsingException
	 */
	public ObjectParsedScope(XML_Context context, 
			ParsedScope parent, 
			Callback callback, 
			String tag, 
			XML_StreamReader.Point point) {
		super();
		this.context = context;
		this.parent = parent;
		this.callback = callback;
		
		this.tag = tag;
		

		// initialize
		state = new ReadAttributes();
	}
	
	
	private void setType(TypeHandler typeHandler) throws XML_ParsingException {
		this.typeHandler = typeHandler;
		this.object = typeHandler.create(point);
	}


	public Object getObject() {
		return object;
	}

	
	/**
	 * 
	 * @param name
	 * @param value
	 * @param point
	 * @throws XML_ParsingException
	 */
	private void setAttribute(String name, String value, XML_StreamReader.Point point) throws XML_ParsingException {
		if(typeHandler!=null) {
			typeHandler.setAttribute(object, name, value, point);
		}
		else if(name.equals(XML_Syntax.TYPE_KEYWORD)){ // & typeHandler == null
			typeHandler = context.getTypeHandlerByTag(value);
			if(typeHandler==null) {
				throw new XML_ParsingException(point, "Failed to find marching element for type:"+value);
			}
			setType(typeHandler);
		}
		else {
			throw new XML_ParsingException(point, "No type attached to this element at this point of parsing, "
					+ "so cannot match any attribute names: "+value);
		}
	}
	
	private void setValue(String value, XML_StreamReader.Point point) throws XML_ParsingException {
		if(typeHandler.hasValueSetter()) {
			typeHandler.setValue(object, value, point);	
		}
	}


	@Override
	public void parse(XML_Context context, XML_Parser parser, XML_StreamReader reader) throws IOException, XML_ParsingException {
		if(isClosed) {
			throw new XML_ParsingException(reader.getPoint(), "This scope has already been closed");
		}
		isParsing = true;
		while(isParsing){
			state.parse(context, parser, reader);
		}
	}



	/**
	 * 
	 * @author pc
	 *
	 */
	private interface State {

		public abstract void parse(XML_Context context, XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException;

	}

	private class ReadAttributes implements State {

		@Override
		public void parse(XML_Context context, XML_Parser parser, XML_StreamReader reader) 
				throws XML_ParsingException, IOException {

			reader.skip(' ', '\t' , '\n');

			// find next attribute name if any
			String name = reader.until(
					/* stop at */ new char[]{' ', '=', '/', '>', '\t', '\n'},
					/* ignore */ null,
					/* forbid */ new char[]{',', '<', '"'});

			reader.skip(' ', '\t' , '\n');

			// we found an attribute definition
			if(reader.isCurrent('=')) {
				reader.readNext();
				reader.skip(' ', '\t' , '\n');
				reader.check('"');
				reader.readNext();

				String value = reader.until(
						/* stop at */ new char[]{'"'},
						/* ignore */ null,
						/* forbid */ new char[]{',', '<', '>', '=', '\n'});

				setAttribute(name, value, reader.getPoint());
				reader.readNext();
			}
			else if(reader.isCurrent('>')){
				reader.readNext();
				state = new ReadContent();
			}
			else if(reader.isCurrent('/')){
				reader.readNext();
				reader.check('>');
				//reader.readNext();
				state = new CloseScope();
			}
		}
	}



	private class ReadContent implements State {

		@Override
		public void parse(XML_Context context, XML_Parser parser, XML_StreamReader reader) 
				throws XML_ParsingException, IOException {

			// read value until next tag start
			String value = reader.until(
					/* stop at */ new char[]{'<'},
					/* ignore */ null,
					/* forbid */ null);	

			// set value if any
			if(!ParsedScope.isBlank(value)){
				setValue(value, reader.getPoint());
			}
			state = new ReadOpeningTag();
		}
	}


	private class ReadOpeningTag implements State {

		@Override
		public void parse(XML_Context context, XML_Parser parser, XML_StreamReader reader)
				throws XML_ParsingException, IOException {
			reader.check('<');
			reader.readNext();
			// closing tag
			if(reader.isCurrent('/')){
				reader.readNext();
				state = new ReadClosingTag();
			}
			else if(reader.isCurrent('!')){
				reader.readNext();
				state = new ReadComment();
			}
			else if(reader.isCurrent('?')){
				throw new XML_ParsingException(reader.getPoint(), "Illegal header position within an element");
				//state = new ReadElementHeader();
			}
			else{
				String tag = reader.until(
						/* stop at */ new char[]{'>', ' ', '/', '\n'},
						/* ignore */ null,
						/* forbid */ new char[]{',', '=', '"'});

				/* create new scope */

				// switch to new scope
				parser.scope = typeHandler.createParsedElement(context, ObjectParsedScope.this, tag, reader.getPoint());

				// and escape this scope...
				isParsing = false;

				// ...and come back as reading content
				state = new ReadContent();
			}
		}
	}


	private class ReadClosingTag implements State {

		@Override
		public void parse(XML_Context context, XML_Parser parser, XML_StreamReader reader) throws XML_ParsingException, IOException {
			String tag = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ new char[]{' '},
					/* forbid */ new char[]{',', '=', '"', '/'});

			// check closing tag
			if(!ObjectParsedScope.this.tag.equals(tag)) {
				throw new XML_ParsingException(reader.getPoint(), "Closing tag is not matching: "+tag+ "instead of "
						+ObjectParsedScope.this.tag+".");
			}

			//reader.readNext();
			state = new CloseScope();
		}
	}

	private class ReadComment implements State {

		@Override
		public void parse(XML_Context context, XML_Parser parser, XML_StreamReader reader) 
				throws XML_ParsingException, IOException {
			String comment = reader.until(
					/* stop at */ new char[]{'>'},
					/* ignore */ null,
					/* forbid */ null);
			reader.readNext();
			if(parser.isVerbose) {
				System.out.println("XML COmment: "+comment);	
			}
			state = new ReadContent();
		}
	}


	private class CloseScope implements State {


		@Override
		public void parse(XML_Context context,XML_Parser parser, XML_StreamReader reader) 
				throws XML_ParsingException, IOException {
			// close
			close();

			// stop
			isParsing = false;

			// and go back to parent
			parser.scope = parent;
		}
	}

	/**
	 * close and flush data with callback 2 (setter)
	 * @throws XML_ParsingException 
	 */
	public void close() throws XML_ParsingException {

		if(!isClosed) {
			
			callback.set(object);
			

			// never returning to this scope parser.
			isClosed = true;
		}
	}

}
