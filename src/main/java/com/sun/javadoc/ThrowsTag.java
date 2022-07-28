package com.sun.javadoc;

public interface ThrowsTag extends Tag {
  String exceptionName();
  
  String exceptionComment();
  
  ClassDoc exception();
  
  Type exceptionType();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\ThrowsTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */