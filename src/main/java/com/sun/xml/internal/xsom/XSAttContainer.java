package com.sun.xml.internal.xsom;

import java.util.Collection;
import java.util.Iterator;

public interface XSAttContainer extends XSDeclaration {
  XSWildcard getAttributeWildcard();
  
  XSAttributeUse getAttributeUse(String paramString1, String paramString2);
  
  Iterator<? extends XSAttributeUse> iterateAttributeUses();
  
  Collection<? extends XSAttributeUse> getAttributeUses();
  
  XSAttributeUse getDeclaredAttributeUse(String paramString1, String paramString2);
  
  Iterator<? extends XSAttributeUse> iterateDeclaredAttributeUses();
  
  Collection<? extends XSAttributeUse> getDeclaredAttributeUses();
  
  Iterator<? extends XSAttGroupDecl> iterateAttGroups();
  
  Collection<? extends XSAttGroupDecl> getAttGroups();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSAttContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */