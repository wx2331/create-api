package com.sun.tools.internal.xjc.api;

import com.sun.istack.internal.NotNull;
import com.sun.tools.internal.xjc.Options;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public interface SchemaCompiler {
  ContentHandler getParserHandler(String paramString);
  
  void parseSchema(InputSource paramInputSource);
  
  void setTargetVersion(SpecVersion paramSpecVersion);
  
  void parseSchema(String paramString, Element paramElement);
  
  void parseSchema(String paramString, XMLStreamReader paramXMLStreamReader) throws XMLStreamException;
  
  void setErrorListener(ErrorListener paramErrorListener);
  
  void setEntityResolver(EntityResolver paramEntityResolver);
  
  void setDefaultPackageName(String paramString);
  
  void forcePackageName(String paramString);
  
  void setClassNameAllocator(ClassNameAllocator paramClassNameAllocator);
  
  void resetSchema();
  
  S2JJAXBModel bind();
  
  @NotNull
  Options getOptions();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\SchemaCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */