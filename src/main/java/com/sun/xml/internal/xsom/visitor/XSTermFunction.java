package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSElementDecl;
import com.sun.xml.internal.xsom.XSModelGroup;
import com.sun.xml.internal.xsom.XSModelGroupDecl;
import com.sun.xml.internal.xsom.XSWildcard;

public interface XSTermFunction<T> {
  T wildcard(XSWildcard paramXSWildcard);
  
  T modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl);
  
  T modelGroup(XSModelGroup paramXSModelGroup);
  
  T elementDecl(XSElementDecl paramXSElementDecl);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\visitor\XSTermFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */