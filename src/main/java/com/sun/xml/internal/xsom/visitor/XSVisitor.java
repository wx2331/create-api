package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSAnnotation;
import com.sun.xml.internal.xsom.XSAttGroupDecl;
import com.sun.xml.internal.xsom.XSAttributeDecl;
import com.sun.xml.internal.xsom.XSAttributeUse;
import com.sun.xml.internal.xsom.XSComplexType;
import com.sun.xml.internal.xsom.XSFacet;
import com.sun.xml.internal.xsom.XSIdentityConstraint;
import com.sun.xml.internal.xsom.XSNotation;
import com.sun.xml.internal.xsom.XSSchema;
import com.sun.xml.internal.xsom.XSXPath;

public interface XSVisitor extends XSTermVisitor, XSContentTypeVisitor {
  void annotation(XSAnnotation paramXSAnnotation);
  
  void attGroupDecl(XSAttGroupDecl paramXSAttGroupDecl);
  
  void attributeDecl(XSAttributeDecl paramXSAttributeDecl);
  
  void attributeUse(XSAttributeUse paramXSAttributeUse);
  
  void complexType(XSComplexType paramXSComplexType);
  
  void schema(XSSchema paramXSSchema);
  
  void facet(XSFacet paramXSFacet);
  
  void notation(XSNotation paramXSNotation);
  
  void identityConstraint(XSIdentityConstraint paramXSIdentityConstraint);
  
  void xpath(XSXPath paramXSXPath);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\visitor\XSVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */