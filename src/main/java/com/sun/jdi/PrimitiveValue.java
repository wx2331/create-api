package com.sun.jdi;

import jdk.Exported;

@Exported
public interface PrimitiveValue extends Value {
  boolean booleanValue();
  
  byte byteValue();
  
  char charValue();
  
  short shortValue();
  
  int intValue();
  
  long longValue();
  
  float floatValue();
  
  double doubleValue();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\PrimitiveValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */