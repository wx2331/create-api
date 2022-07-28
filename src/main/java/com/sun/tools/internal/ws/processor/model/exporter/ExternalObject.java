package com.sun.tools.internal.ws.processor.model.exporter;

import org.xml.sax.ContentHandler;

public interface ExternalObject {
  String getType();
  
  void saveTo(ContentHandler paramContentHandler);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\exporter\ExternalObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */