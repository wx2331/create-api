package com.sun.javadoc;

public interface SerialFieldTag extends Tag, Comparable<Object> {
  String fieldName();
  
  String fieldType();
  
  ClassDoc fieldTypeDoc();
  
  String description();
  
  int compareTo(Object paramObject);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\SerialFieldTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */