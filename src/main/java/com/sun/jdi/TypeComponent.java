package com.sun.jdi;

import jdk.Exported;

@Exported
public interface TypeComponent extends Mirror, Accessible {
  String name();
  
  String signature();
  
  String genericSignature();
  
  ReferenceType declaringType();
  
  boolean isStatic();
  
  boolean isFinal();
  
  boolean isSynthetic();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\TypeComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */