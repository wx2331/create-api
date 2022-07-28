package com.sun.tools.internal.xjc.api;

import com.sun.xml.internal.bind.api.ErrorListener;
import org.xml.sax.SAXParseException;

public interface ErrorListener extends ErrorListener {
  void error(SAXParseException paramSAXParseException);
  
  void fatalError(SAXParseException paramSAXParseException);
  
  void warning(SAXParseException paramSAXParseException);
  
  void info(SAXParseException paramSAXParseException);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\ErrorListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */