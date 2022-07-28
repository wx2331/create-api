package com.sun.tools.internal.ws.api.wsdl;

import javax.xml.namespace.QName;

public interface TWSDLExtensible {
  String getNameValue();
  
  String getNamespaceURI();
  
  QName getWSDLElementName();
  
  void addExtension(TWSDLExtension paramTWSDLExtension);
  
  Iterable<? extends TWSDLExtension> extensions();
  
  TWSDLExtensible getParent();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\api\wsdl\TWSDLExtensible.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */