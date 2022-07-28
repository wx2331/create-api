package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSElementDecl;
import com.sun.xml.internal.xsom.XSModelGroup;
import com.sun.xml.internal.xsom.XSModelGroupDecl;
import com.sun.xml.internal.xsom.XSWildcard;

public interface XSTermVisitor {
  void wildcard(XSWildcard paramXSWildcard);
  
  void modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl);
  
  void modelGroup(XSModelGroup paramXSModelGroup);
  
  void elementDecl(XSElementDecl paramXSElementDecl);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\visitor\XSTermVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */