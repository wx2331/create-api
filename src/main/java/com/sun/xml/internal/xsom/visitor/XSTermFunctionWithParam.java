package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSElementDecl;
import com.sun.xml.internal.xsom.XSModelGroup;
import com.sun.xml.internal.xsom.XSModelGroupDecl;
import com.sun.xml.internal.xsom.XSWildcard;

public interface XSTermFunctionWithParam<T, P> {
  T wildcard(XSWildcard paramXSWildcard, P paramP);
  
  T modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl, P paramP);
  
  T modelGroup(XSModelGroup paramXSModelGroup, P paramP);
  
  T elementDecl(XSElementDecl paramXSElementDecl, P paramP);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\visitor\XSTermFunctionWithParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */