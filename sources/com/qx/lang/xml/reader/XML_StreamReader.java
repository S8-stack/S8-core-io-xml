package com.qx.lang.xml.reader;

import java.io.IOException;
import java.io.Reader;

import com.qx.lang.xml.XML_Syntax;


public class XML_StreamReader {


	private Reader reader;

	/**
	 * 
	 */
	private int c;


	public XML_StreamReader(Reader reader) throws IOException {
		super();
		this.reader = reader;

		// start reading
		readNext();
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
	 * read next char until reading end char
	 * @param b
	 * @return
	 * @throws Exception
	 */
	public String readUntil(char b) throws Exception {
		StringBuilder builder = new StringBuilder();
		while(true) {
			readNext();
			if(c==b){
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
	 * @param stopping
	 * @param ignored
	 * @param forbidden
	 * @return
	 * @throws Exception
	 */
	public String read(char stopping, char[] ignored, char[] forbidden) throws Exception{
		StringBuilder builder = new StringBuilder();
		while(true) {
			readNext();
			if(c==stopping){
				return builder.toString();
			}
			else if(isOneOf(forbidden)){
				throw new Exception("Forbidden char has been found");
			}
			else if(c==-1){
				throw new Exception("Unexpected end of stream");
			}
			else if(isOneOf(ignored)){
				// skipped
			}
			else{
				builder.append((char) c);
			}
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
		for(char value : values){
			if(c==value){
				return true;
			}
		}
		return false;
	}


	/**
	 * @throws IOException 
	 * @throws Exception 
	 * 
	 */
	public void readNext() throws IOException {
		c = reader.read();
		while(isOneOf(XML_Syntax.IGNORED_CHARS)){
			c = reader.read();
		}
	}
	
	public void readNextWhileIgnoring(char... ignoredChars) throws IOException {
		c = reader.read();
		while(isOneOf(ignoredChars)){
			c = reader.read();
		}
	}
	
	public void skipWhiteSpace() throws IOException {
		c = reader.read();
		while(c==XML_Syntax.WHITE_SPACE){
			c = reader.read();
		}
	}
	

	
	
	/**
	 * read next char for <code>length</code>.
	 * 
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public String readString(int length) throws IOException{
		char[] buffer = new char[length];
		reader.read(buffer, 0, length);
		
		readNext();
		
		return new String(buffer);
	}


	public char getCurrentChar() {
		return (char) c;
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
