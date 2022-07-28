package com.sun.xml.internal.xsom;

public interface XSAttributeDecl extends XSDeclaration {
  XSSimpleType getType();
  
  XmlString getDefaultValue();
  
  XmlString getFixedValue();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSAttributeDecl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */