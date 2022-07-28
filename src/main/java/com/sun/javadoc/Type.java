package com.sun.javadoc;

public interface Type {
  String typeName();
  
  String qualifiedTypeName();
  
  String simpleTypeName();
  
  String dimension();
  
  String toString();
  
  boolean isPrimitive();
  
  ClassDoc asClassDoc();
  
  ParameterizedType asParameterizedType();
  
  TypeVariable asTypeVariable();
  
  WildcardType asWildcardType();
  
  AnnotatedType asAnnotatedType();
  
  AnnotationTypeDoc asAnnotationTypeDoc();
  
  Type getElementType();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */