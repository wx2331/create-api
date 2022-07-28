package com.sun.xml.internal.xsom;

import java.util.List;

public interface XSIdentityConstraint extends XSComponent {
  public static final short KEY = 0;
  
  public static final short KEYREF = 1;
  
  public static final short UNIQUE = 2;
  
  XSElementDecl getParent();
  
  String getName();
  
  String getTargetNamespace();
  
  short getCategory();
  
  XSXPath getSelector();
  
  List<XSXPath> getFields();
  
  XSIdentityConstraint getReferencedKey();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSIdentityConstraint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */