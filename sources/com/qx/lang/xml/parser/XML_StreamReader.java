package com.qx.lang.xml.parser;

import java.io.IOException;
import java.io.Reader;

import com.qx.lang.xml.XML_Syntax;


public class XML_StreamReader {

	public static final boolean IS_DEBUG_ENABLED = true;

	private Reader reader;

	private int line;

	private int column;

	private boolean isRunning;

	/**
	 * 
	 */
	private int c;

	public XML_StreamReader(Reader reader) throws Exception {
		super();
		this.reader = reader;
		line = 1;
		column = 1;
		isRunning = true;
	}


	/**
	 * check (do not read next)
	 * 
	 * @param sequence
	 * @throws Exception
	 */
	public void check(String sequence) throws Exception {
		int n=sequence.length();
		for(int i=0; i<n; i++){
			if(c!=sequence.charAt(i)){
				throw new Exception("Unexpected sequence encountered while deserializing");
			}
			if(i<n-1){
				readNext();	
			}
		}
	}

	/**
	 * check (do not read next)
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void check(char c) throws Exception {
		if(this.c!=c){
			throw new Exception("Unexpected sequence encountered while deserializing");
		}
	}


	/**
	 * check (do not read next)
	 * 
	 * @param expectedChars
	 * @throws Exception
	 */
	public void check(char... expectedChars) throws Exception{
		if(!isOneOf(expectedChars)){
			throw new Exception("Unexpected sequence encountered while deserializing");
		}
	}




	/**
	 * WARNING : /!\Read from current char. Stop on one of <code>stopping</code>.
	 * @param stopping
	 * @param ignored
	 * @param forbidden
	 * @return
	 * @throws Exception
	 */
	public String until(char[] stopping, char[] ignored, char[] forbidden) throws XML_ParsingException, IOException{
		StringBuilder builder = new StringBuilder();
		while(true) {
			if(isOneOf(stopping)){
				return builder.toString();
			}
			else if(isOneOf(forbidden)){
				throw new XML_ParsingException("Forbidden char has been found>"+((char)c)+"<", line, column);
			}
			else if(isOneOf(ignored)){
				// skipped
			}
			else{
				builder.append((char) c);
			}
			readNext();
		}
	}


	/**
	 * 
	 * @param ignored
	 * @param forbidden
	 * @throws Exception
	 */
	public void next(char[] ignored, char[] forbidden) throws XML_ParsingException, IOException {
		boolean isNext = false;
		while(!isNext) {
			readNext();

			if(isOneOf(forbidden)){
				throw new XML_ParsingException("Forbidden char has been found", line, column);
			}
			else if(isOneOf(ignored)){
				// skipped
			}
			else{
				isNext = true;
			}
		}
	}

	public void next(char[] ignored) throws XML_ParsingException, IOException {
		boolean isNext = false;
		while(!isNext) {
			readNext();
			if(isOneOf(ignored)){
				// skipped
			}
			else{
				isNext = true;
			}
		}
	}


	/**
	 * base methof for reading next char
	 * @throws IOException 
	 * @throws Exception
	 */
	public void readNext() throws XML_ParsingException, IOException {
		if(isRunning){
			boolean isNext = false;
			while(!isNext) {
				c = reader.read();
				if(IS_DEBUG_ENABLED){
					System.out.print((char) c);
				}
				if(c==-1){
					isRunning = false;
					isNext = true;
					// end normally
				}
				else if(c=='\n'){
					line++;
					column=0;
					isNext = true; // not skipped
				}
				else if(c=='\r'){
					// skipped
				}
				else if(c=='\t'){
					column+=5;
					// skipped
				}
				else{
					isNext = true;
				}
			}	
		}
		else{
			throw new XML_ParsingException("Attempting to read closed stream", line, column);
		}
	}





	/**
	 * read next char until reading end char
	 * 
	 * @param stoppingChars
	 * @return
	 * @throws Exception
	 */
	public String readUntilOneOf(char... stoppingChars) throws Exception{
		StringBuilder builder = new StringBuilder();
		while(true){
			readNext();
			if(isOneOf(stoppingChars)){
				return builder.toString();	
			}
			else if(c==-1){
				throw new Exception("Unexpected end of stream");
			}
			else{
				builder.append((char) c);
			}
		}
	}


	/**
	 * 
	 * @param values
	 * @return
	 */
	public boolean isOneOf(char... values){
		if(values!=null){
			for(char value : values){
				if(c==value){
					return true;
				}
			}
			return false;
		}
		else{
			return false;
		}
	}


	public void readNextWhileIgnoring(char... ignoredChars) throws IOException, XML_ParsingException {
		readNext();
		if(IS_DEBUG_ENABLED){
			System.out.print((char) c);
		}
		while(isOneOf(ignoredChars)){
			c = reader.read();
		}
	}

	public void skipWhiteSpace() throws IOException, XML_ParsingException {
		while(c==XML_Syntax.WHITE_SPACE){
			readNext();
		}
	}


	/**
	 * WARNING : /!\Read from current char
	 * 
	 * @param skipped
	 * @throws IOException
	 * @throws XML_ParsingException
	 */
	public void skip(char... skipped) throws IOException, XML_ParsingException {
		while(isOneOf(skipped)){
			readNext();
		}
	}



	public char getCurrentChar() {
		return (char) c;
	}

	public boolean isCurrent(char comparedChar){
		return ((char) c)==comparedChar;
	}


	/**
	 * Closes the stream and releases any system resources associated with it.
	 * Once the stream has been closed, further read(), ready(), mark(), reset(), 
	 * or skip() invocations will throw an IOException.
	 * Closing a previously closed stream has no effect.
	 * @throws IOException
	 */
	public void close() throws IOException {
		reader.close();
	}


	public boolean isFinished(){
		return c==-1;
	}
}
