package com.sun.xml.internal.rngom.xml.sax;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class AbstractLexicalHandler implements LexicalHandler {
  public void startDTD(String s, String s1, String s2) throws SAXException {}
  
  public void endDTD() throws SAXException {}
  
  public void startEntity(String s) throws SAXException {}
  
  public void endEntity(String s) throws SAXException {}
  
  public void startCDATA() throws SAXException {}
  
  public void endCDATA() throws SAXException {}
  
  public void comment(char[] chars, int start, int length) throws SAXException {}
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\xml\sax\AbstractLexicalHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */