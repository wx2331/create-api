package com.sun.xml.internal.xsom.impl.parser;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public interface PatcherManager {
  void addPatcher(Patch paramPatch);
  
  void addErrorChecker(Patch paramPatch);
  
  void reportError(String paramString, Locator paramLocator) throws SAXException;
  
  public static interface Patcher {
    void run() throws SAXException;
  }
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\parser\PatcherManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */