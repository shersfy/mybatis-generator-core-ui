package org.shersfy.utils;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class SAXReaderUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SAXReaderUtil.class);
	
	private static SAXReader reader;
	
	static {
		reader = new SAXReader();
		try {
			reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		} catch (SAXException e) {
			LOGGER.error("", e);
		}
	}
	
	public static Document getDocument(File xml) throws DocumentException {
		Document doc = reader.read(xml);
		return doc;
	}

}
