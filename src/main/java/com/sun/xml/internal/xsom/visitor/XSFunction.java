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

public interface XSFunction<T> extends XSContentTypeFunction<T>, XSTermFunction<T> {
  T annotation(XSAnnotation paramXSAnnotation);
  
  T attGroupDecl(XSAttGroupDecl paramXSAttGroupDecl);
  
  T attributeDecl(XSAttributeDecl paramXSAttributeDecl);
  
  T attributeUse(XSAttributeUse paramXSAttributeUse);
  
  T complexType(XSComplexType paramXSComplexType);
  
  T schema(XSSchema paramXSSchema);
  
  T facet(XSFacet paramXSFacet);
  
  T notation(XSNotation paramXSNotation);
  
  T identityConstraint(XSIdentityConstraint paramXSIdentityConstraint);
  
  T xpath(XSXPath paramXSXPath);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\visitor\XSFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */