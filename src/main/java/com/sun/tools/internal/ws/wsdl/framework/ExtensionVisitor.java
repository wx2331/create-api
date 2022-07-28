package com.sun.tools.internal.ws.wsdl.framework;

import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;

public interface ExtensionVisitor {
  void preVisit(TWSDLExtension paramTWSDLExtension) throws Exception;
  
  void postVisit(TWSDLExtension paramTWSDLExtension) throws Exception;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\ExtensionVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */