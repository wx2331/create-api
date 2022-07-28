package com.sun.tools.internal.ws.api.wsdl;

import org.w3c.dom.Element;
import org.xml.sax.Locator;

public interface TWSDLParserContext {
  void push();
  
  void pop();
  
  String getNamespaceURI(String paramString);
  
  Iterable<String> getPrefixes();
  
  String getDefaultNamespaceURI();
  
  void registerNamespaces(Element paramElement);
  
  Locator getLocation(Element paramElement);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\api\wsdl\TWSDLParserContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */