package com.sun.xml.internal.xsom;

public interface XSDeclaration extends XSComponent {
  String getTargetNamespace();
  
  String getName();
  
  boolean isAnonymous();
  
  boolean isGlobal();
  
  boolean isLocal();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */