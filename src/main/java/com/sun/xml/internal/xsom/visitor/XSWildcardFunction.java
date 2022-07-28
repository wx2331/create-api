package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSWildcard;

public interface XSWildcardFunction<T> {
  T any(XSWildcard.Any paramAny);
  
  T other(XSWildcard.Other paramOther);
  
  T union(XSWildcard.Union paramUnion);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\visitor\XSWildcardFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */