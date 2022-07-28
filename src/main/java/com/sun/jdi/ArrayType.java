package com.sun.jdi;

import jdk.Exported;

@Exported
public interface ArrayType extends ReferenceType {
  ArrayReference newInstance(int paramInt);
  
  String componentSignature();
  
  String componentTypeName();
  
  Type componentType() throws ClassNotLoadedException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ArrayType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */