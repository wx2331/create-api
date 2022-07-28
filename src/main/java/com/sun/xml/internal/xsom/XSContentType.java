package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.visitor.XSContentTypeFunction;
import com.sun.xml.internal.xsom.visitor.XSContentTypeVisitor;

public interface XSContentType extends XSComponent {
  XSSimpleType asSimpleType();
  
  XSParticle asParticle();
  
  XSContentType asEmpty();
  
  <T> T apply(XSContentTypeFunction<T> paramXSContentTypeFunction);
  
  void visit(XSContentTypeVisitor paramXSContentTypeVisitor);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSContentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */