package com.sun.xml.internal.xsom.parser;

import java.io.IOException;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public interface XMLParser {
  void parse(InputSource paramInputSource, ContentHandler paramContentHandler, ErrorHandler paramErrorHandler, EntityResolver paramEntityResolver) throws SAXException, IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\parser\XMLParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */