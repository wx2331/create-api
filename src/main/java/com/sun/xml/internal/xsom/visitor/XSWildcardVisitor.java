package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSWildcard;

public interface XSWildcardVisitor {
  void any(XSWildcard.Any paramAny);
  
  void other(XSWildcard.Other paramOther);
  
  void union(XSWildcard.Union paramUnion);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\visitor\XSWildcardVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */