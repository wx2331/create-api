package com.sun.xml.internal.xsom;

public interface XSUnionSimpleType extends XSSimpleType, Iterable<XSSimpleType> {
  XSSimpleType getMember(int paramInt);
  
  int getMemberSize();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSUnionSimpleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */