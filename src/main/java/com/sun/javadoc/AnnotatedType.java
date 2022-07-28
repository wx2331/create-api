package com.sun.javadoc;

public interface AnnotatedType extends Type {
  AnnotationDesc[] annotations();
  
  Type underlyingType();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\AnnotatedType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */