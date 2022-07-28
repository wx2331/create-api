package com.sun.javadoc;

public interface Parameter {
  Type type();
  
  String name();
  
  String typeName();
  
  String toString();
  
  AnnotationDesc[] annotations();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\Parameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */