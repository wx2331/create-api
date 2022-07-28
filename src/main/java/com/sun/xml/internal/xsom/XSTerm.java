package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.visitor.XSTermFunction;
import com.sun.xml.internal.xsom.visitor.XSTermFunctionWithParam;
import com.sun.xml.internal.xsom.visitor.XSTermVisitor;

public interface XSTerm extends XSComponent {
  void visit(XSTermVisitor paramXSTermVisitor);
  
  <T> T apply(XSTermFunction<T> paramXSTermFunction);
  
  <T, P> T apply(XSTermFunctionWithParam<T, P> paramXSTermFunctionWithParam, P paramP);
  
  boolean isWildcard();
  
  boolean isModelGroupDecl();
  
  boolean isModelGroup();
  
  boolean isElementDecl();
  
  XSWildcard asWildcard();
  
  XSModelGroupDecl asModelGroupDecl();
  
  XSModelGroup asModelGroup();
  
  XSElementDecl asElementDecl();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSTerm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */