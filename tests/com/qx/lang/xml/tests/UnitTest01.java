package com.qx.lang.xml.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.qx.lang.xml.example.Wrapper;
import com.qx.level0.lang.xml.XML_Context;

public class UnitTest01 {

	public static void main(String[] args) throws Exception {
		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input/test02.xml"))));
		XML_Context context = new XML_Context(new Class<?>[] { Wrapper.class });
		
		Object object = context.deserialize(reader);
		System.out.println("done: "+object);
	}

}
