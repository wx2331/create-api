package com.sun.tools.internal.jxc.gen.config;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface NGCCEventSource {
  int replace(NGCCEventReceiver paramNGCCEventReceiver1, NGCCEventReceiver paramNGCCEventReceiver2);
  
  void sendEnterElement(int paramInt, String paramString1, String paramString2, String paramString3, Attributes paramAttributes) throws SAXException;
  
  void sendLeaveElement(int paramInt, String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void sendEnterAttribute(int paramInt, String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void sendLeaveAttribute(int paramInt, String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void sendText(int paramInt, String paramString) throws SAXException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\jxc\gen\config\NGCCEventSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */