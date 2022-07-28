package com.sun.xml.internal.dtdparser;

import java.util.EventListener;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public interface DTDEventListener extends EventListener {
  public static final short CONTENT_MODEL_EMPTY = 0;
  
  public static final short CONTENT_MODEL_ANY = 1;
  
  public static final short CONTENT_MODEL_MIXED = 2;
  
  public static final short CONTENT_MODEL_CHILDREN = 3;
  
  public static final short USE_NORMAL = 0;
  
  public static final short USE_IMPLIED = 1;
  
  public static final short USE_FIXED = 2;
  
  public static final short USE_REQUIRED = 3;
  
  public static final short CHOICE = 0;
  
  public static final short SEQUENCE = 1;
  
  public static final short OCCURENCE_ZERO_OR_MORE = 0;
  
  public static final short OCCURENCE_ONE_OR_MORE = 1;
  
  public static final short OCCURENCE_ZERO_OR_ONE = 2;
  
  public static final short OCCURENCE_ONCE = 3;
  
  void setDocumentLocator(Locator paramLocator);
  
  void processingInstruction(String paramString1, String paramString2) throws SAXException;
  
  void notationDecl(String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void unparsedEntityDecl(String paramString1, String paramString2, String paramString3, String paramString4) throws SAXException;
  
  void internalGeneralEntityDecl(String paramString1, String paramString2) throws SAXException;
  
  void externalGeneralEntityDecl(String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void internalParameterEntityDecl(String paramString1, String paramString2) throws SAXException;
  
  void externalParameterEntityDecl(String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void startDTD(InputEntity paramInputEntity) throws SAXException;
  
  void endDTD() throws SAXException;
  
  void comment(String paramString) throws SAXException;
  
  void characters(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException;
  
  void ignorableWhitespace(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException;
  
  void startCDATA() throws SAXException;
  
  void endCDATA() throws SAXException;
  
  void fatalError(SAXParseException paramSAXParseException) throws SAXException;
  
  void error(SAXParseException paramSAXParseException) throws SAXException;
  
  void warning(SAXParseException paramSAXParseException) throws SAXException;
  
  void startContentModel(String paramString, short paramShort) throws SAXException;
  
  void endContentModel(String paramString, short paramShort) throws SAXException;
  
  void attributeDecl(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, short paramShort, String paramString4) throws SAXException;
  
  void childElement(String paramString, short paramShort) throws SAXException;
  
  void mixedElement(String paramString) throws SAXException;
  
  void startModelGroup() throws SAXException;
  
  void endModelGroup(short paramShort) throws SAXException;
  
  void connector(short paramShort) throws SAXException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\dtdparser\DTDEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */