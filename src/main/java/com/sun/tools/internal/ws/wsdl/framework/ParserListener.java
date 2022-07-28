package com.sun.tools.internal.ws.wsdl.framework;

import javax.xml.namespace.QName;

public interface ParserListener {
  void ignoringExtension(Entity paramEntity, QName paramQName1, QName paramQName2);
  
  void doneParsingEntity(QName paramQName, Entity paramEntity);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\framework\ParserListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */