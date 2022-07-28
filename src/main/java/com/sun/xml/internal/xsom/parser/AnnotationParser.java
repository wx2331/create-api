package com.sun.xml.internal.xsom.parser;

import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

public abstract class AnnotationParser {
  public abstract ContentHandler getContentHandler(AnnotationContext paramAnnotationContext, String paramString, ErrorHandler paramErrorHandler, EntityResolver paramEntityResolver);
  
  public abstract Object getResult(Object paramObject);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\parser\AnnotationParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */