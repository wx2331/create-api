package com.sun.javadoc;

public interface AnnotationDesc {
  AnnotationTypeDoc annotationType();
  
  ElementValuePair[] elementValues();
  
  boolean isSynthesized();
  
  public static interface ElementValuePair {
    AnnotationTypeElementDoc element();
    
    AnnotationValue value();
  }
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\AnnotationDesc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */