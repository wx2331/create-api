package com.sun.jdi;

import jdk.Exported;

@Exported
public interface Field extends TypeComponent, Comparable<Field> {
  String typeName();
  
  Type type() throws ClassNotLoadedException;
  
  boolean isTransient();
  
  boolean isVolatile();
  
  boolean isEnumConstant();
  
  boolean equals(Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\Field.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */