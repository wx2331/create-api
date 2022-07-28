package com.sun.xml.internal.xsom.visitor;

import com.sun.xml.internal.xsom.XSListSimpleType;
import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
import com.sun.xml.internal.xsom.XSUnionSimpleType;

public interface XSSimpleTypeFunction<T> {
  T listSimpleType(XSListSimpleType paramXSListSimpleType);
  
  T unionSimpleType(XSUnionSimpleType paramXSUnionSimpleType);
  
  T restrictionSimpleType(XSRestrictionSimpleType paramXSRestrictionSimpleType);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\visitor\XSSimpleTypeFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */