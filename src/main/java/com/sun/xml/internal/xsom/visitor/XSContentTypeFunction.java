package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSContentType;
import com.sun.xml.internal.xsom.XSParticle;
import com.sun.xml.internal.xsom.XSSimpleType;

public interface XSContentTypeFunction<T> {
  T simpleType(XSSimpleType paramXSSimpleType);
  
  T particle(XSParticle paramXSParticle);
  
  T empty(XSContentType paramXSContentType);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\visitor\XSContentTypeFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */