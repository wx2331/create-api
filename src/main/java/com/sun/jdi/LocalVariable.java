package com.sun.jdi;

import jdk.Exported;

@Exported
public interface LocalVariable extends Mirror, Comparable<LocalVariable> {
  String name();
  
  String typeName();
  
  Type type() throws ClassNotLoadedException;
  
  String signature();
  
  String genericSignature();
  
  boolean isVisible(StackFrame paramStackFrame);
  
  boolean isArgument();
  
  boolean equals(Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\LocalVariable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */