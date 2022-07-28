package com.sun.xml.internal.xsom;

import java.util.List;

public interface XSComplexType extends XSType, XSAttContainer {
  boolean isAbstract();
  
  boolean isFinal(int paramInt);
  
  boolean isSubstitutionProhibited(int paramInt);
  
  XSElementDecl getScope();
  
  XSContentType getContentType();
  
  XSContentType getExplicitContent();
  
  boolean isMixed();
  
  XSComplexType getRedefinedBy();
  
  List<XSComplexType> getSubtypes();
  
  List<XSElementDecl> getElementDecls();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSComplexType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */