package com.sun.tools.internal.ws.wsdl.framework;

import javax.xml.namespace.QName;
import org.xml.sax.Locator;

public interface Elemental {
  QName getElementName();
  
  Locator getLocator();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\Elemental.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */