package com.sun.tools.internal.jxc.gen.config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface NGCCEventReceiver {
  void enterElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes) throws SAXException;
  
  void leaveElement(String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void text(String paramString) throws SAXException;
  
  void enterAttribute(String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void leaveAttribute(String paramString1, String paramString2, String paramString3) throws SAXException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\gen\config\NGCCEventReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */