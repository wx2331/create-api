package com.sun.xml.internal.xsom;

public interface XSAttributeUse extends XSComponent {
  boolean isRequired();
  
  XSAttributeDecl getDecl();
  
  XmlString getDefaultValue();
  
  XmlString getFixedValue();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSAttributeUse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */